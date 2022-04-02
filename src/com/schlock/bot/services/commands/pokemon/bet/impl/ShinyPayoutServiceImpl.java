package com.schlock.bot.services.commands.pokemon.bet.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.entities.pokemon.ShinyBet;
import com.schlock.bot.entities.pokemon.ShinyGetLetsGo;
import com.schlock.bot.entities.pokemon.ShinyGetType;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.AbstractListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.pokemon.bet.ShinyPayoutService;
import com.schlock.bot.services.database.adhoc.DatabaseManager;
import com.schlock.bot.services.database.pokemon.ShinyBetDAO;
import com.schlock.bot.services.database.pokemon.ShinyGetLetsGoDAO;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.ShinyGetFormatter;
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.ioc.Messages;

import java.util.*;

public class ShinyPayoutServiceImpl extends AbstractListenerService implements ShinyPayoutService
{
    protected static final String NO_BETS_NO_WINNERS_KEY = "no-bets-no-winners";

    protected static final String WINNERS_POKEMON_KEY = "payout-pokemon-winners";
    protected static final String WINNERS_POKEMON_NONE_KEY = "payout-pokemon-winners-none";
    protected static final String WINNERS_TIME_KEY = "payout-time-winners";
    protected static final String WINNERS_BOTH_KEY = "payout-both-winners";

    protected static final String BIGGEST_WINNER_KEY = "payout-biggest-winner";
    protected static final String USER_UPDATE_KEY = "payout-user-update";

    protected static final String BAD_FORMAT_MESSAGE_KEY = "get-wrong-format";

    protected static final String DOUBLER_BONUS = "doubler-bonus";

    private static final String SHINY_GET_COMMAND = "!shinyget ";

    private final PokemonManagement pokemonManagement;
    private final ShinyGetFormatter shinyFormatter;

    private final DatabaseManager database;

    private final DeploymentConfiguration config;


    public ShinyPayoutServiceImpl(PokemonManagement pokemonManagement,
                                  ShinyGetFormatter shinyFormatter,
                                  DatabaseManager database,
                                  Messages messages,
                                  DeploymentConfiguration config)
    {
        super(messages);

        this.pokemonManagement = pokemonManagement;
        this.shinyFormatter = shinyFormatter;

        this.database = database;

        this.config = config;
    }

    public boolean isAcceptRequest(String username, String in)
    {
        String admin = config.getOwnerUsername();

        return username.equals(admin) &&
                in != null &&
                in.toLowerCase().startsWith(SHINY_GET_COMMAND);
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    protected ListenerResponse processRequest(String username, String in)
    {
        final String MARK = config.getCurrencyMark();
        final String ADMIN = config.getOwnerUsername();

        if (!username.equals(ADMIN)) return ListenerResponse.relayNothing();

        if (in.toLowerCase().trim().startsWith(SHINY_GET_COMMAND))
        {
            String params = in.substring(SHINY_GET_COMMAND.length()).trim();

            ShinyGetLetsGo get = createShinyGetFromParams(params);;
            if(get == null)
            {
                return formatSingleResponse(BAD_FORMAT_MESSAGE_KEY);
            }

            database.save(get);

            ListenerResponse response = ListenerResponse.relayAll();
            response.addMessage(shinyFormatter.formatNewlyCaughtLetsGo(get));

            List<ShinyBet> bets = database.get(ShinyBetDAO.class).getAllCurrent();
            if (bets.size() == 0)
            {
                return response.addMessage(messages.get(NO_BETS_NO_WINNERS_KEY));
            }

            Map<User, Integer> totalWinnings = new HashMap<>();
            Set<String> usersWinningPokemon = new HashSet<>();
            Set<String> usersWinningTime = new HashSet<>();
            Set<String> usersWinningBoth = new HashSet<>();

            Integer closestRange = calculateClosestRange(get.getTimeInMinutes(), bets);
            for (ShinyBet bet : bets)
            {
                bet.setShiny(get);
                database.save(bet);

                boolean winningPokemon = isWinningPokemon(bet, get.getPokemonId());
                boolean winningTime = isWinningTime(bet, get.getTimeInMinutes(), closestRange);

                Double winnings = 0.0;
                if (winningPokemon)
                {
                    winnings += bet.getBetAmount() * config.getBetsPokemonWinFactor();

                    usersWinningPokemon.add(bet.getUser().getUsername());
                }
                if (winningTime)
                {
                    winnings += bet.getBetAmount() * config.getBetsTimeWinFactor();

                    usersWinningTime.add(bet.getUser().getUsername());
                }
                if (winningPokemon && winningTime)
                {
                    winnings = winnings * config.getBetsBothWinFactor();

                    usersWinningBoth.add(bet.getUser().getUsername());
                }

                Integer total = totalWinnings.get(bet.getUser());
                if (total == null)
                {
                    totalWinnings.put(bet.getUser(), winnings.intValue());
                }
                else
                {
                    totalWinnings.put(bet.getUser(), total + winnings.intValue());
                }
            }

            if (usersWinningPokemon.size() != 0)
            {
                response.addMessage(messages.format(WINNERS_POKEMON_KEY, StringUtils.join(usersWinningPokemon, ", ")));
            }
            else
            {
                response.addMessage(messages.get(WINNERS_POKEMON_NONE_KEY));
            }
            if (usersWinningTime.size() != 0)
            {
                response.addMessage(messages.format(WINNERS_TIME_KEY, StringUtils.join(usersWinningTime, ", ")));
            }
            if (usersWinningBoth.size() != 0)
            {
                response.addMessage(messages.format(WINNERS_BOTH_KEY, StringUtils.join(usersWinningBoth, ", ")));
            }


            for (User user : totalWinnings.keySet())
            {
                String doublerMsg = "";
                Integer winnings = totalWinnings.get(user);

                if (user.hasDoubler())
                {
                    winnings = user.modifyPointsWithDoubler(winnings);
                    doublerMsg = " " + messages.format(DOUBLER_BONUS, user.getPointsDoubler());
                }

                user.incrementBalance(winnings);

                response.addMessage(messages.format(USER_UPDATE_KEY, user.getUsername(), winnings, MARK, user.getBalance(), MARK) + doublerMsg);

                database.save(user);
            }

            return response;
        }
        return nullResponse();
    }

    protected Integer calculateClosestRange(final Integer ACTUAL, List<ShinyBet> bets)
    {
        int closestRange = 10000;

        for (ShinyBet bet : bets)
        {
            int range = ACTUAL - bet.getTimeMinutes();
            if (range < 0)
            {
                range = range*-1;
            }

            if (range < closestRange)
            {
                closestRange = range;
            }
        }
        return closestRange;
    }

    private boolean isWinningPokemon(ShinyBet bet, String pokemonId)
    {
        return pokemonId.equalsIgnoreCase(bet.getPokemonId());
    }

    private boolean isWinningTime(ShinyBet bet, final Integer ACTUAL, Integer closestTimeRange)
    {
        int range = ACTUAL - bet.getTimeMinutes();
        if (range < 0)
        {
            range = range * -1;
        }
        return closestTimeRange.equals(range);
    }

    protected ShinyGetLetsGo createShinyGetFromParams(String params)
    {
        //[type] [pokemon] [time] [checks]
        String[] p = params.split(" ");

        try
        {
            Pokemon pokemon = pokemonManagement.getPokemonFromText(p[1]);
            if (pokemon == null)
            {
                return null;
            }

            ShinyGetType type = ShinyGetType.valueOf(p[0].toUpperCase());
            Integer time = Integer.parseInt(p[2]);

            Integer checks = null;
            if (p.length == 4)
            {
                checks = Integer.parseInt(p[3]);
            }

            if (type == null || pokemon == null || time == null)
            {
                return null;
            }

            Integer shinyNumber = database.get(ShinyGetLetsGoDAO.class).getCurrentShinyNumber();

            ShinyGetLetsGo get = new ShinyGetLetsGo();
            get.setType(type);
            get.setPokemonId(pokemon.getId());
            get.setTimeInMinutes(time);
            get.setNumOfRareChecks(checks);
            get.setShinyNumber(shinyNumber);

            return get;
        }
        catch(Exception e)
        {
        }
        return null;
    }
}
