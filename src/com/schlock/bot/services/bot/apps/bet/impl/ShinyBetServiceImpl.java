package com.schlock.bot.services.bot.apps.bet.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.bet.ShinyBet;
import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.bot.apps.AbstractListenerService;
import com.schlock.bot.services.bot.apps.ListenerResponse;
import com.schlock.bot.services.bot.apps.UserService;
import com.schlock.bot.services.bot.apps.bet.ShinyBetService;
import com.schlock.bot.services.bot.apps.pokemon.PokemonService;
import com.schlock.bot.services.database.apps.ShinyBetDAO;
import com.schlock.bot.services.database.apps.UserDAO;
import org.apache.tapestry5.ioc.Messages;

import java.util.List;

public class ShinyBetServiceImpl extends AbstractListenerService implements ShinyBetService
{
    protected static final String INSUFFICIENT_FUNDS_KEY = "bet-insufficient-funds";
    protected static final String UPDATE_INSUFFICIENT_FUNDS_KEY = "bet-update-insufficient-funds";
    protected static final String BET_WRONG_FORMAT_KEY = "bet-wrong-format";
    protected static final String BET_UPDATE_SUCCESS_KEY = "bet-update-success";
    protected static final String BET_SUCCESS_KEY = "bet-success";
    protected static final String CURRENT_BET_KEY = "current-bet";
    protected static final String NO_CURRENT_BETS_KEY = "no-current-bets";

    protected static final String BET_CANCELED_KEY = "bet-canceled";
    protected static final String ALL_BETS_CANCELED_KEY = "all-bets-canceled";

    protected static final String BET_CANCEL_WRONG_FORMAT_KEY = "bet-cancel-wrong-format";
    protected static final String BET_CANCEL_NO_BET_KEY = "bet-cancel-no-bet";

    protected static final String BETS_NOW_OPEN_KEY = "bets-now-open";
    protected static final String BETS_NOW_CLOSED_KEY = "bets-now-closed";
    protected static final String BETS_ARE_CLOSED_KEY = "bets-are-closed";

    private static final String BET_COMMAND = "!bet ";

    private static final String SHOW_CURRENT_BETS = "!currentbets";

    private static final String CANCEL_BET = "!cancelbet ";
    private static final String CANCEL_ALL_BETS = "!cancelallbets";

    private static final String OPEN_BETTING = "!openbets";
    private static final String CLOSE_BETTING = "!closebets";

    private final PokemonService pokemonService;
    private final UserService userService;

    private final ShinyBetDAO shinyBetDAO;
    private final UserDAO userDAO;

    private final Messages messages;
    private final DeploymentConfiguration config;


    private boolean bettingCurrentOpen = false;

    public ShinyBetServiceImpl(PokemonService pokemonService,
                               UserService userService,
                               ShinyBetDAO shinyBetDAO,
                               UserDAO userDAO,
                               Messages messages,
                               DeploymentConfiguration config)
    {
        super(messages);

        this.pokemonService = pokemonService;
        this.userService = userService;

        this.shinyBetDAO = shinyBetDAO;
        this.userDAO = userDAO;

        this.messages = messages;
        this.config = config;
    }

    public boolean isAcceptRequest(String username, String in)
    {
        if(in != null)
        {
            if (in.toLowerCase().startsWith(OPEN_BETTING) ||
                    in.toLowerCase().startsWith(CLOSE_BETTING))
            {
                String admin = config.getOwnerUsername();
                return username.equals(admin);
            }

            return in != null &&
                    (in.toLowerCase().startsWith(BET_COMMAND) ||
                            in.toLowerCase().startsWith(SHOW_CURRENT_BETS) ||
                            in.toLowerCase().startsWith(CANCEL_BET) ||
                            in.toLowerCase().startsWith(CANCEL_ALL_BETS));
        }
        return false;
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    public ListenerResponse process(String username, String in)
    {
        String command = in.toLowerCase();
        if (command.startsWith(SHOW_CURRENT_BETS))
        {
            return currentBets(username);
        }

        String owner = config.getOwnerUsername();
        if (command.startsWith(OPEN_BETTING))
        {
            if (!username.equals(owner))
            {
                return singleResponseByKey(NOT_ADMIN_KEY);
            }
            openBetting();

            return singleResponseByKey(BETS_NOW_OPEN_KEY);
        }
        if (command.startsWith(CLOSE_BETTING))
        {
            if (!username.equals(owner))
            {
                return singleResponseByKey(NOT_ADMIN_KEY);
            }
            closeBetting();
            return singleResponseByKey(BETS_NOW_CLOSED_KEY);
        }

        if (!bettingCurrentOpen)
        {
            return singleResponseByKey(BETS_ARE_CLOSED_KEY);
        }

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

        return nullResponse();
    }

    private ListenerResponse currentBets(String username)
    {
        List<ShinyBet> bets = shinyBetDAO.getByUsername(username);

        ListenerResponse responses = ListenerResponse.relaySingle();
        for(ShinyBet bet : bets)
        {
            Pokemon pokemon = pokemonService.getPokemonFromText(bet.getPokemonId());

            String response = messages.format(CURRENT_BET_KEY,
                                                username,
                                                pokemon.getName(),
                                                bet.getTimeMinutes().toString(),
                                                bet.getBetAmount().toString(),
                                                config.getCurrencyMark());

            responses.addMessage(response);
        }

        if (bets.size() == 0)
        {
            String response = messages.format(NO_CURRENT_BETS_KEY, username);
            responses.addMessage(response);
        }

        return responses;
    }

    private ListenerResponse placeBet(String username, String in)
    {
        String params = in.substring(BET_COMMAND.length());

        Pokemon pokemon = getPokemonFromParams(params);
        Integer time = getTimeFromParams(params);
        Integer betAmount = getBetAmountFromParams(params);

        if (pokemon != null && time != null && betAmount != null)
        {
            String mark = config.getCurrencyMark();
            User user = userService.getUser(username);

            ShinyBet currentBet = shinyBetDAO.getByUsernameAndPokemon(username, pokemon.getId());
            if(currentBet != null)
            {
                Integer changedBalance = user.getBalance() + currentBet.getBetAmount();
                if (changedBalance < betAmount)
                {
                    String balance = user.getBalance().toString();
                    String currentBetAmount = currentBet.getBetAmount().toString();

                    return singleResponseFormat(UPDATE_INSUFFICIENT_FUNDS_KEY, mark, balance, mark, currentBetAmount, mark);
                }

                user.incrementBalance(currentBet.getBetAmount());
                user.decrementBalance(betAmount);

                currentBet.setBetAmount(betAmount);
                currentBet.setTimeMinutes(time);

                shinyBetDAO.save(currentBet);
                userDAO.save(user);

                userDAO.commit();

                return singleResponseFormat(BET_UPDATE_SUCCESS_KEY, username, pokemon.getName(), time.toString(), betAmount.toString(), mark);
            }
            else
            {
                if (user.getBalance() < betAmount)
                {
                    String balance = user.getBalance().toString();

                    return singleResponseFormat(INSUFFICIENT_FUNDS_KEY, mark, balance, mark);
                }

                ShinyBet newBet = new ShinyBet();
                newBet.setUser(user);
                newBet.setPokemonId(pokemon.getId());
                newBet.setTimeMinutes(time);
                newBet.setBetAmount(betAmount);

                user.decrementBalance(betAmount);

                shinyBetDAO.save(newBet);
                userDAO.save(user);

                userDAO.commit();

                return singleResponseFormat(BET_SUCCESS_KEY, username, pokemon.getName(), time.toString(), betAmount.toString(), mark);
            }
        }
        return singleResponseByKey(BET_WRONG_FORMAT_KEY);
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

    private ListenerResponse cancelBet(String username, String in)
    {
        String params = in.substring(CANCEL_BET.length());

        Pokemon pokemon = pokemonService.getPokemonFromText(params);
        if (pokemon != null)
        {
            ShinyBet bet = shinyBetDAO.getByUsernameAndPokemon(username, pokemon.getId());
            if (bet != null)
            {
                Integer betAmount = bet.getBetAmount();

                User user = userService.getUser(username);
                user.incrementBalance(betAmount);

                shinyBetDAO.delete(bet);
                userDAO.save(user);

                userDAO.commit();

                return singleResponseFormat(BET_CANCELED_KEY, pokemon.getName(), username);
            }
            return singleResponseFormat(BET_CANCEL_NO_BET_KEY, username, pokemon.getName());
        }
        return singleResponseByKey(BET_CANCEL_WRONG_FORMAT_KEY);
    }

    private ListenerResponse cancelAllBets(String username)
    {
        List<ShinyBet> bets = shinyBetDAO.getByUsername(username);

        User user = userService.getUser(username);

        for (ShinyBet bet : bets)
        {
            Integer betAmount = bet.getBetAmount();
            user.incrementBalance(betAmount);

            shinyBetDAO.delete(bet);
        }
        userDAO.save(user);

        userDAO.commit();

        return singleResponseFormat(ALL_BETS_CANCELED_KEY, username);
    }

    protected void openBetting()
    {
        bettingCurrentOpen = true;
    }

    protected void closeBetting()
    {
        bettingCurrentOpen = false;
    }
}
