package com.schlock.bot.services.apps.bet.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.bet.ShinyBet;
import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.services.DatabaseModule;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.apps.UserService;
import com.schlock.bot.services.apps.bet.ShinyBetService;
import com.schlock.bot.services.apps.pokemon.PokemonService;

import java.util.Arrays;
import java.util.List;

public class ShinyBetServiceImpl implements ShinyBetService
{
    protected static final String INSUFFICIENT_FUNDS = "You don't have enough %s to bet that. Current balance: %s%s";
    protected static final String WRONG_FORMAT = "Use format to bet: !bet [pokemon] [minutes] [bet amount]";
    protected static final String BET_SUCCESS = "Bet has been made for %s: %s in %s min at %s%s";

    private static final String INSTRUCTIONS_COMMAND = "!instructions";
    private static final String BET_COMMAND = "!bet ";

    private final PokemonService pokemonService;
    private final UserService userService;

    private final DatabaseModule database;

    private final DeploymentContext deploymentContext;

    public ShinyBetServiceImpl(PokemonService pokemonService,
                               UserService userService,
                               DatabaseModule database,
                               DeploymentContext deploymentContext)
    {
        this.pokemonService = pokemonService;
        this.userService = userService;

        this.database = database;

        this.deploymentContext = deploymentContext;
    }

    public boolean isAcceptRequest(String in)
    {
        return in != null &&
                (in.toLowerCase().startsWith(BET_COMMAND) ||
                        in.toLowerCase().startsWith(INSTRUCTIONS_COMMAND));
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    public List<String> process(String username, String in)
    {
        return Arrays.asList(processSingleResults(username, in));
    }

    public String processSingleResults(String username, String in)
    {
        String command = in.toLowerCase();
        if (command.startsWith(INSTRUCTIONS_COMMAND))
        {
            return returnInstructions(username, in);
        }
        if (command.startsWith(BET_COMMAND))
        {
            return placeBet(username, in);
        }
        return null;
    }

    private String returnInstructions(String username, String in)
    {
        return "Use format to bet: !bet [pokemon] [minutes] [bet amount]";
    }

    private String placeBet(String username, String in)
    {
        String params = in.substring(BET_COMMAND.length());

        Pokemon pokemon = getPokemonFromParams(params);
        Integer time = getTimeFromParams(params);
        Integer betAmount = getBetAmountFromParams(params);

        if (pokemon != null && time != null && betAmount != null)
        {
            String mark = deploymentContext.getCurrencyMark();

            User user = userService.getUser(username);
            if (user.getBalance() < betAmount)
            {
                String balance = user.getBalance().toString();

                return String.format(INSUFFICIENT_FUNDS, mark, balance, mark);
            }

            ShinyBet newBet = new ShinyBet();
            newBet.setUser(user);
            newBet.setPokemonId(pokemon.getId());
            newBet.setTimeMinutes(time);
            newBet.setBetAmount(betAmount);

            user.decrementBalance(betAmount);

            database.save(Arrays.asList(newBet, user));

            return String.format(BET_SUCCESS, username, pokemon.getName(), time.toString(), betAmount.toString(), mark);
        }
        return WRONG_FORMAT;
    }

    private Pokemon getPokemonFromParams(String params)
    {
        String[] p = params.trim().split(" ");

        String pokemonName = p[0];
        String next = p[1];
        if (!isNumber(next))
        {
            pokemonName += next;
        }
        Pokemon pokemon = pokemonService.getPokemonFromText(pokemonName);
        return pokemon;
    }

    private boolean isNumber(String number)
    {
        try
        {
            Integer.parseInt(number);
            return true;
        }
        catch(Exception e)
        {
        }
        return false;
    }


    private Integer getTimeFromParams(String params)
    {
        String timeMinutes = getSecondToLastCellWithValue(params.split(" "));
        try
        {
            return Integer.parseInt(timeMinutes);
        }
        catch (NumberFormatException e)
        {
        }
        return null;
    }

    private String getSecondToLastCellWithValue(String[] params)
    {
        boolean foundLast = false;
        for(int i = params.length -1; i > 0; i--)
        {
            String data = params[i].trim();
            if (!data.equalsIgnoreCase(""))
            {
                if (foundLast)
                {
                    return data;
                }
                foundLast = true;
            }
        }
        return null;
    }

    private Integer getBetAmountFromParams(String params)
    {
        String betAmount = getLastCellWithValue(params.split(" "));
        try
        {
            return Integer.parseInt(betAmount);
        }
        catch (NumberFormatException e)
        {
        }
        return null;
    }

    private String getLastCellWithValue(String[] params)
    {
        for(int i = params.length -1; i > 0; i--)
        {
            String data = params[i].trim();
            if (!data.equalsIgnoreCase(""))
            {
                return data;
            }
        }
        return null;
    }
}
