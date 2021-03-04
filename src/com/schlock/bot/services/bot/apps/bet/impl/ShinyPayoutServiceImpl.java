package com.schlock.bot.services.bot.apps.bet.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.bet.ShinyBet;
import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.entities.apps.pokemon.ShinyGet;
import com.schlock.bot.entities.apps.pokemon.ShinyGetType;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.bot.apps.AbstractListenerService;
import com.schlock.bot.services.bot.apps.ListenerResponse;
import com.schlock.bot.services.bot.apps.bet.ShinyPayoutService;
import com.schlock.bot.services.bot.apps.pokemon.PokemonService;
import com.schlock.bot.services.database.apps.ShinyBetDAO;
import com.schlock.bot.services.database.apps.ShinyGetDAO;
import com.schlock.bot.services.database.apps.UserDAO;
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

    private static final String SHINY_GET_COMMAND = "!shinyget ";

    private final PokemonService pokemonService;

    private final ShinyBetDAO shinyBetDAO;
    private final ShinyGetDAO shinyGetDAO;
    private final UserDAO userDAO;

    private final DeploymentConfiguration config;


    public ShinyPayoutServiceImpl(PokemonService pokemonService,
                                    ShinyBetDAO shinyBetDAO,
                                    ShinyGetDAO shinyGetDAO,
                                    UserDAO userDAO,
                                    Messages messages,
                                    DeploymentConfiguration config)
    {
        super(messages);

        this.pokemonService = pokemonService;

        this.shinyBetDAO = shinyBetDAO;
        this.shinyGetDAO = shinyGetDAO;
        this.userDAO = userDAO;

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

    public ListenerResponse process(String username, String in)
    {
        final String MARK = config.getCurrencyMark();
        final String ADMIN = config.getOwnerUsername();

        if (!username.equals(ADMIN)) return ListenerResponse.empty();

        if (in.toLowerCase().trim().startsWith(SHINY_GET_COMMAND))
        {
            String params = in.substring(SHINY_GET_COMMAND.length()).trim();

            ShinyGet get = createShinyGetFromParams(params);;
            if(get == null)
            {
                return singleResponseByKey(BAD_FORMAT_MESSAGE_KEY);
            }

            shinyGetDAO.save(get);

            Map<User, Integer> totalWinnings = new HashMap<>();
            Set<String> usersWinningPokemon = new HashSet<>();
            Set<String> usersWinningTime = new HashSet<>();
            Set<String> usersWinningBoth = new HashSet<>();

            List<ShinyBet> bets = shinyBetDAO.getAllCurrent();
            if (bets.size() == 0)
            {
                shinyBetDAO.commit();

                return singleResponseByKey(NO_BETS_NO_WINNERS_KEY);
            }

            Integer closestRange = calculateClosestRange(get.getTimeInMinutes(), bets);
            for (ShinyBet bet : bets)
            {
                bet.setShiny(get);
                shinyBetDAO.save(bet);

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

            ListenerResponse response = ListenerResponse.respondRelayToDiscord();
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
                Integer winnings = totalWinnings.get(user);
                user.incrementBalance(winnings);

                response.addMessage(messages.format(USER_UPDATE_KEY, user.getUsername(), winnings, MARK, user.getBalance(), MARK));

                userDAO.save(user);
            }

            userDAO.commit();

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

    protected ShinyGet createShinyGetFromParams(String params)
    {
        //[type] [pokemon] [time] [checks]
        String[] p = params.split(" ");

        try
        {
            Pokemon pokemon = pokemonService.getPokemonFromText(p[1]);
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

            Integer shinyNumber = shinyGetDAO.getCurrentShinyNumber();

            ShinyGet get = new ShinyGet();
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
