package com.schlock.bot.services.apps.bet.impl;

import com.schlock.bot.entities.Persisted;
import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.bet.ShinyBet;
import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.entities.apps.pokemon.ShinyGet;
import com.schlock.bot.entities.apps.pokemon.ShinyGetType;
import com.schlock.bot.services.DatabaseModule;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.apps.bet.ShinyPayoutService;
import com.schlock.bot.services.apps.pokemon.PokemonService;
import com.schlock.bot.services.database.apps.ShinyBetDAO;
import com.schlock.bot.services.database.apps.ShinyGetDAO;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class ShinyPayoutServiceImpl implements ShinyPayoutService
{
    private static final String NO_BETS_NO_WINNERS = "There were no bets, so there are no winners.";

    private static final String WINNERS_POKEMON = "Users that guessed the correct Pokemon: %s";
    private static final String WINNERS_POKEMON_NONE = "No one guessed the correct Pokemon.";
    private static final String WINNERS_TIME = "Users that guessed the closed time: %s";
    private static final String WINNERS_BOTH = "Users that correctly guessed both! %s";

    private static final String BIGGEST_WINNER = "The biggest winner was %s. Winning %s%s. New Balance: %s%s";
    private static final String USER_UPDATE = "User %s won %s%s. New Balance: %s%s";

    private static final String BAD_FORMAT_MESSAGE = "Bad format: !shinyget [type] [pokemon] [time] [checks]";

    private static final String SHINY_GET_COMMAND = "!shinyget ";

    private final PokemonService pokemonService;

    private final DatabaseModule database;
    private final DeploymentContext context;


    public ShinyPayoutServiceImpl(PokemonService pokemonService,
                                    DatabaseModule database,
                                    DeploymentContext context)
    {
        this.pokemonService = pokemonService;

        this.database = database;
        this.context = context;
    }

    public boolean isAcceptRequest(String username, String in)
    {
        String admin = context.getOwnerUsername();

        return username.equals(admin) &&
                in != null &&
                in.toLowerCase().startsWith(SHINY_GET_COMMAND);
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    public List<String> process(String username, String in)
    {
        final String MARK = context.getCurrencyMark();
        final String ADMIN = context.getOwnerUsername();

        String command = in.toLowerCase().trim();
        if (username.equals(ADMIN) && command.startsWith(SHINY_GET_COMMAND))
        {
            String params = in.substring(SHINY_GET_COMMAND.length()).trim();

            ShinyGet get = createShinyGetFromParams(params);;
            if(get == null)
            {
                return Arrays.asList(BAD_FORMAT_MESSAGE);
            }

            List<Persisted> tobeSaved = new ArrayList<>();
            List<Persisted> tobeDeleted = new ArrayList<>();

            tobeSaved.add(get);

            Map<User, Integer> totalWinnings = new HashMap<>();
            Set<String> usersWinningPokemon = new HashSet<>();
            Set<String> usersWinningTime = new HashSet<>();
            Set<String> usersWinningBoth = new HashSet<>();

            List<ShinyBet> bets = database.get(ShinyBetDAO.class).getAll();
            if (bets.size() == 0)
            {
                database.save(tobeSaved);

                return Arrays.asList(NO_BETS_NO_WINNERS);
            }

            Integer closestRange = calculateClosestRange(get.getTimeInMinutes(), bets);
            for (ShinyBet bet : bets)
            {
                boolean winningPokemon = isWinningPokemon(bet, get.getPokemonId());
                boolean winningTime = isWinningTime(bet, get.getTimeInMinutes(), closestRange);

                Double winnings = 0.0;
                if (winningPokemon)
                {
                    winnings += bet.getBetAmount() * context.getBetsPokemonWinFactor();

                    usersWinningPokemon.add(bet.getUser().getUsername());
                }
                if (winningTime)
                {
                    winnings += bet.getBetAmount() * context.getBetsTimeWinFactor();

                    usersWinningTime.add(bet.getUser().getUsername());
                }
                if (winningPokemon && winningTime)
                {
                    winnings = winnings * context.getBetsBothWinFactor();

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

            List<String> responses = new ArrayList<>();
            if (usersWinningPokemon.size() != 0)
            {
                responses.add(String.format(WINNERS_POKEMON, StringUtils.join(usersWinningPokemon, ", ")));
            }
            else
            {
                responses.add(WINNERS_POKEMON_NONE);
            }
            if (usersWinningTime.size() != 0)
            {
                responses.add(String.format(WINNERS_TIME, StringUtils.join(usersWinningPokemon, ", ")));
            }
            if (usersWinningBoth.size() != 0)
            {
                responses.add(String.format(WINNERS_BOTH, StringUtils.join(usersWinningBoth, ", ")));
            }


            for (User user : totalWinnings.keySet())
            {
                Integer winnings = totalWinnings.get(user);
                user.incrementBalance(winnings);

                responses.add(String.format(USER_UPDATE, user.getUsername(), winnings, MARK, user.getBalance(), MARK));

                tobeSaved.add(user);
            }


            tobeDeleted.addAll(bets);

            database.save(tobeSaved);
            database.delete(tobeDeleted);

            return responses;
        }
        return Collections.EMPTY_LIST;
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

            Integer shinyNumber = database.get(ShinyGetDAO.class).getCurrentShinyNumber();

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
