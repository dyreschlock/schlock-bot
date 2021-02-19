package com.schlock.bot.services.apps.bet.impl;

import com.schlock.bot.entities.Persisted;
import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.bet.ShinyBet;
import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.services.DatabaseModule;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.apps.UserService;
import com.schlock.bot.services.apps.bet.ShinyBetService;
import com.schlock.bot.services.apps.pokemon.PokemonService;
import com.schlock.bot.services.database.apps.ShinyBetDAO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShinyBetServiceImpl implements ShinyBetService
{
    protected static final String INSUFFICIENT_FUNDS = "You don't have enough %s to bet that. Current balance: %s%s";
    protected static final String INSUFFICIENT_FUNDS_UPDATE = "You don't have enough %s to update that bet. Current Balance %s%s, Current Bet: %s%s";
    protected static final String BET_WRONG_FORMAT = "Use format to bet: !bet [pokemon] [minutes] [bet amount]";
    protected static final String BET_UPDATE_SUCCESS = "Bet for %s has been updated. Now: %s in %s min at %s%s";
    protected static final String BET_SUCCESS = "Bet has been made for %s: %s in %s min at %s%s";
    protected static final String BET_FORMAT = "%s has a bet for %s in %s min at %s%s";

    protected static final String BET_CANCELED = "Bet for %s has been canceled for %s";
    protected static final String ALL_BETS_CANCELED = "All bets have been canceled for %s";

    protected static final String BET_CANCEL_WRONG_FORMAT = "User format to cancel bet: !cancelbet [pokemon]";
    protected static final String BET_CANCEL_NO_BET = "Sorry %s, you don't have a bet for %s";

    private static final String BET_COMMAND = "!bet ";

    private static final String SHOW_CURRENT_BETS = "!currentbets";

    private static final String CANCEL_BET = "!cancelbet ";
    private static final String CANCEL_ALL_BETS = "!cancelallbets";

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
                        in.toLowerCase().startsWith(SHOW_CURRENT_BETS) ||
                        in.toLowerCase().startsWith(CANCEL_BET) ||
                        in.toLowerCase().startsWith(CANCEL_ALL_BETS));
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    public List<String> process(String username, String in)
    {
        String command = in.toLowerCase();
        if (command.startsWith(SHOW_CURRENT_BETS))
        {
            return currentBets(username);
        }
        return Arrays.asList(processSingleResults(username, command));
    }

    public String processSingleResults(String username, String command)
    {
        if (command.startsWith(BET_COMMAND))
        {
            return placeBet(username, command);
        }
        if (command.startsWith(CANCEL_BET))
        {
            return cancelBet(username, command);
        }
        if (command.startsWith(CANCEL_ALL_BETS))
        {
            return cancelAllBets(username);
        }
        return null;
    }

    private List<String> currentBets(String username)
    {
        List<ShinyBet> bets = database.get(ShinyBetDAO.class).getByUsername(username);

        List<String> responses = new ArrayList<>();
        for(ShinyBet bet : bets)
        {
            Pokemon pokemon = pokemonService.getPokemonFromText(bet.getPokemonId());

            String response = String.format(BET_FORMAT,
                                                username,
                                                pokemon.getName(),
                                                bet.getTimeMinutes().toString(),
                                                bet.getBetAmount().toString(),
                                                deploymentContext.getCurrencyMark());

            responses.add(response);
        }
        return responses;
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

            ShinyBet currentBet = database.get(ShinyBetDAO.class).getByUsernameAndPokemon(username, pokemon.getId());
            if(currentBet != null)
            {
                Integer changedBalance = user.getBalance() + currentBet.getBetAmount();
                if (changedBalance < betAmount)
                {
                    String balance = user.getBalance().toString();
                    String currentBetAmount = currentBet.getBetAmount().toString();

                    return String.format(INSUFFICIENT_FUNDS_UPDATE, mark, balance, mark, currentBetAmount, mark);
                }

                user.incrementBalance(currentBet.getBetAmount());
                user.decrementBalance(betAmount);

                currentBet.setBetAmount(betAmount);
                currentBet.setTimeMinutes(time);

                database.save(Arrays.asList(currentBet, user));

                return String.format(BET_UPDATE_SUCCESS, username, pokemon.getName(), time.toString(), betAmount.toString(), mark);
            }
            else
            {
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
        }
        return BET_WRONG_FORMAT;
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

    private String cancelBet(String username, String in)
    {
        String params = in.substring(CANCEL_BET.length());

        Pokemon pokemon = pokemonService.getPokemonFromText(params);
        if (pokemon != null)
        {
            ShinyBet bet = database.get(ShinyBetDAO.class).getByUsernameAndPokemon(username, pokemon.getId());
            if (bet != null)
            {
                Integer betAmount = bet.getBetAmount();

                User user = userService.getUser(username);
                user.incrementBalance(betAmount);

                database.delete(bet);
                database.save(user);

                return String.format(BET_CANCELED, pokemon.getName(), username);
            }
            return String.format(BET_CANCEL_NO_BET, username, pokemon.getName());
        }
        return BET_CANCEL_WRONG_FORMAT;
    }

    private String cancelAllBets(String username)
    {
        List<ShinyBet> bets = database.get(ShinyBetDAO.class).getByUsername(username);

        User user = userService.getUser(username);

        List<Persisted> objects = new ArrayList<>();
        for (ShinyBet bet : bets)
        {
            Integer betAmount = bet.getBetAmount();
            user.incrementBalance(betAmount);

            objects.add(bet);
        }

        database.delete(objects);
        database.save(user);

        return String.format(ALL_BETS_CANCELED, username);
    }
}
