package com.schlock.bot.services.commands.pokemon.bet.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.entities.pokemon.ShinyBetLetsGo;
import com.schlock.bot.entities.pokemon.ShinyGetLetsGo;
import com.schlock.bot.entities.pokemon.ShinyGetType;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.AbstractListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.pokemon.bet.ShinyPayoutLetsGoService;
import com.schlock.bot.services.database.adhoc.DatabaseManager;
import com.schlock.bot.services.database.pokemon.ShinyBetLetsGoDAO;
import com.schlock.bot.services.database.pokemon.ShinyGetLetsGoDAO;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.ShinyGetFormatter;
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.ioc.Messages;

import java.util.*;

public class ShinyPayoutLetsGoServiceImpl extends AbstractListenerService implements ShinyPayoutLetsGoService
{
    protected static final String NO_BETS_NO_WINNERS_KEY = "no-winners-no-bets";

    protected static final String WINNERS_POKEMON_KEY = "payout-pokemon-winners";
    protected static final String WINNERS_POKEMON_NONE_KEY = "payout-pokemon-winners-none";
    protected static final String WINNERS_TIME_KEY = "payout-time-winners";
    protected static final String WINNERS_BOTH_KEY = "payout-both-winners";

    protected static final String BIGGEST_WINNER_KEY = "payout-biggest-winner";
    protected static final String USER_UPDATE_KEY = "payout-user-update";

    protected static final String BAD_FORMAT_MESSAGE_KEY = "get-letsgo-wrong-format";

    protected static final String DOUBLER_BONUS = "doubler-bonus";

    private static final String SHINY_GET_COMMAND = "!shinyget ";

    private final PokemonManagement pokemonManagement;
    private final ShinyGetFormatter shinyFormatter;

    private final DatabaseManager database;

    private final DeploymentConfiguration config;


    public ShinyPayoutLetsGoServiceImpl(PokemonManagement pokemonManagement,
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

            List<ShinyBetLetsGo> bets = database.get(ShinyBetLetsGoDAO.class).getAllCurrent();
            if (bets.size() == 0)
            {
                return response.addMessage(messages.get(NO_BETS_NO_WINNERS_KEY));
            }

            Map<User, Long> totalWinnings = new HashMap<>();
            Set<String> usersWinningPokemon = new HashSet<>();
            Set<String> usersWinningTime = new HashSet<>();
            Set<String> usersWinningBoth = new HashSet<>();

            Integer closestRange = calculateClosestRange(get.getTimeInMinutes(), bets);
            for (ShinyBetLetsGo bet : bets)
            {
                bet.setShiny(get);
                database.save(bet);

                boolean winningPokemon = isWinningPokemon(bet, get.getPokemonId());
                boolean winningTime = isWinningTime(bet, get.getTimeInMinutes(), closestRange);

                Long winnings = 0l;
                if (winningPokemon)
                {
                    Double wins = bet.getBetAmount() * config.getBetsLetsGoPokemonWinFactor();
                    winnings += wins.longValue();

                    usersWinningPokemon.add(bet.getUser().getUsername());
                }
                if (winningTime)
                {
                    Double wins = bet.getBetAmount() * config.getBetsLetsGoTimeWinFactor();
                    winnings += wins.longValue();

                    usersWinningTime.add(bet.getUser().getUsername());
                }
                if (winningPokemon && winningTime)
                {
                    winnings = winnings * config.getBetsLetsGoBothWinFactor().longValue();

                    usersWinningBoth.add(bet.getUser().getUsername());
                }

                Long total = totalWinnings.get(bet.getUser());
                if (total == null)
                {
                    totalWinnings.put(bet.getUser(), winnings);
                }
                else
                {
                    totalWinnings.put(bet.getUser(), total + winnings);
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
                Long winnings = totalWinnings.get(user);

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

    protected Integer calculateClosestRange(final Integer ACTUAL, List<ShinyBetLetsGo> bets)
    {
        int closestRange = 10000;

        for (ShinyBetLetsGo bet : bets)
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

    private boolean isWinningPokemon(ShinyBetLetsGo bet, String pokemonId)
    {
        return pokemonId.equalsIgnoreCase(bet.getPokemonId());
    }

    private boolean isWinningTime(ShinyBetLetsGo bet, final Integer ACTUAL, Integer closestTimeRange)
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

            Integer time = null;
            if (!ShinyGetType.RESET.equals(type))
            {
                time = Integer.parseInt(p[2]);
            }

            Integer checks = null;
            if (p.length == 4 || ShinyGetType.RESET.equals(type))
            {
                checks = Integer.parseInt(p[3]);
            }

            if (type == null || pokemon == null)
            {
                return null;
            }

            boolean alolan = false;
            if (ShinyGetType.RESET.equals(type))
            {
                alolan = pokemonManagement.isAlolanAvailable(pokemon);
            }

            Integer shinyNumber = database.get(ShinyGetLetsGoDAO.class).getCurrentShinyNumber();

            ShinyGetLetsGo get = new ShinyGetLetsGo();
            get.setType(type);
            get.setPokemonId(pokemon.getId());
            get.setTimeInMinutes(time);
            get.setNumOfRareChecks(checks);
            get.setShinyNumber(shinyNumber);
            get.setAlolan(alolan);

            return get;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
