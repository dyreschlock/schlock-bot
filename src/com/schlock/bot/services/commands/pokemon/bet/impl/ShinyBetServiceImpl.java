package com.schlock.bot.services.commands.pokemon.bet.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.entities.pokemon.ShinyBetHisui;
import com.schlock.bot.entities.pokemon.ShinyBetLetsGo;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.AbstractListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.pokemon.bet.ShinyBetService;
import com.schlock.bot.services.database.adhoc.DatabaseManager;
import com.schlock.bot.services.database.pokemon.ShinyBetHisuiDAO;
import com.schlock.bot.services.database.pokemon.ShinyBetLetsGoDAO;
import com.schlock.bot.services.entities.base.UserManagement;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.ShinyBetFormatter;
import org.apache.tapestry5.ioc.Messages;

import java.util.Arrays;
import java.util.List;

public class ShinyBetServiceImpl extends AbstractListenerService implements ShinyBetService
{
    protected static final String INSUFFICIENT_FUNDS_KEY = "bet-insufficient-funds";
    protected static final String UPDATE_INSUFFICIENT_FUNDS_KEY = "bet-update-insufficient-funds";
    protected static final String BET_LETSGO_WRONG_FORMAT_KEY = "bet-letsgo-wrong-format";
    protected static final String BET_HISUI_WRONG_FORMAT_KEY = "bet-hisui-wrong-format";

    protected static final String BET_AMOUNT_NEGATIVE = "bet-amount-must-be-positive";
    protected static final String BET_AMOUNT_NOT_ENOUGH = "bet-amount-not-enough";
    protected static final String BET_TIME_NOT_ENOUGH = "bet-time-must-be-positive";
    protected static final String BET_OUTBREAKS_NOT_ENOUGH = "bet-outbreaks-must-be-positive";
    protected static final String BET_POKEMON_ERROR = "bet-pokemon-bad";

    protected static final String BET_LETSGO_UPDATE_SUCCESS_KEY = "bet-letsgo-update-success";
    protected static final String BET_LETSGO_SUCCESS_KEY = "bet-letsgo-success";
    protected static final String BET_HISUI_UPDATE_SUCCESS_KEY = "bet-hisui-update-success";
    protected static final String BET_HISUI_SUCCESS_KEY = "bet-hisui-success";
    protected static final String BET_HISUI_POKEMON_KEY = "bet-hisui-pokemon";
    protected static final String NO_CURRENT_BETS_KEY = "user-no-current-bets";

    protected static final String BET_LETSGO_CANCELED_KEY = "bet-letsgo-canceled";
    protected static final String BET_HISUI_CANCELED_KEY = "bet-hisui-canceled";
    protected static final String ALL_BETS_CANCELED_KEY = "all-bets-canceled";

    protected static final String BET_CANCEL_WRONG_FORMAT_KEY = "bet-cancel-wrong-format";
    protected static final String BET_CANCEL_LETSGO_NO_BET_KEY = "bet-cancel-letsgo-no-bet";
    protected static final String BET_CANCEL_HISUI_NO_BET_KEY = "bet-cancel-hisui-no-bet";

    protected static final String BETS_NOW_OPEN_LETSGO_KEY = "bets-now-open-letsgo";
    protected static final String BETS_NOW_OPEN_HISUI_KEY = "bets-now-open-hisui";
    protected static final String BETS_NOW_OPEN_ERROR_KEY = "bets-now-open-error";
    protected static final String BETS_NOW_CLOSED_KEY = "bets-now-closed";
    protected static final String BETS_ARE_CLOSED_KEY = "bets-are-closed";

    private static final String BET_COMMAND = "!bet ";

    private static final String SHOW_CURRENT_BETS = "!currentbets";

    private static final String CANCEL_BET = "!cancelbet";
    private static final String CANCEL_ALL_BETS = "!cancelallbets";

    private static final String OPEN_BETTING = "!openbets ";
    private static final String CLOSE_BETTING = "!closebets";

    private static final String LETS_GO = "letsgo";
    private static final String HISUI = "hisui";

    private static final Integer HISUI_BET_THRESHOLD = 100;

    private final PokemonManagement pokemonManagement;
    private final UserManagement userManagement;

    private final ShinyBetFormatter shinyBetFormatter;

    private final DatabaseManager database;

    private final DeploymentConfiguration config;


    private BettingType currentBettingType = null;

    public ShinyBetServiceImpl(PokemonManagement pokemonManagement,
                               UserManagement userManagement,
                               ShinyBetFormatter shinyBetFormatter,
                               DatabaseManager database,
                               Messages messages,
                               DeploymentConfiguration config)
    {
        super(messages);

        this.pokemonManagement = pokemonManagement;
        this.userManagement = userManagement;

        this.shinyBetFormatter = shinyBetFormatter;

        this.database = database;

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

    protected ListenerResponse processRequest(String username, String in)
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
                return formatSingleResponse(NOT_ADMIN_KEY);
            }
            return openBetting(command);
        }
        if (command.startsWith(CLOSE_BETTING))
        {
            if (!username.equals(owner))
            {
                return formatSingleResponse(NOT_ADMIN_KEY);
            }
            closeBetting();
            return formatAllResponse(BETS_NOW_CLOSED_KEY);
        }

        if (!isBettingCurrentlyOpen())
        {
            return formatSingleResponse(BETS_ARE_CLOSED_KEY);
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
        List<ShinyBetLetsGo> letsGoBets = database.get(ShinyBetLetsGoDAO.class).getByUsername(username);
        ShinyBetHisui hisuiBet = database.get(ShinyBetHisuiDAO.class).getByUsername(username);

        if (letsGoBets.size() == 0 && hisuiBet == null)
        {
            return formatSingleResponse(NO_CURRENT_BETS_KEY, username);
        }

        ListenerResponse responses = ListenerResponse.relaySingle();

        responses = shinyBetFormatter.formatAllBetsLetsGo(responses, letsGoBets);
        if (hisuiBet != null)
        {
            responses = shinyBetFormatter.formatAllBetsHisui(responses, Arrays.asList(hisuiBet));
        }

        return responses;
    }

    private ListenerResponse placeBet(String username, String in)
    {
        String params = in.substring(BET_COMMAND.length());

        if (currentBettingType.isLetsGo())
        {
            return placeBetLetsGo(username, params);
        }
        if (currentBettingType.isHisui())
        {
            return placeBetHisui(username, params);
        }
        return nullResponse();
    }

    private ListenerResponse placeBetHisui(String username, String params)
    {
        Integer betAmount = getBetAmountFromHisuiParams(params);
        Integer numberOutbreaks = getNumOutbreakFromHisuiParams(params);

        if (betAmount != null && numberOutbreaks != null)
        {
            final String MARK = config.getCurrencyMark();

            if (numberOutbreaks <= 0)
            {
                return formatSingleResponse(BET_OUTBREAKS_NOT_ENOUGH);
            }
            if (betAmount <= HISUI_BET_THRESHOLD)
            {
                String threshold = HISUI_BET_THRESHOLD + MARK;
                return formatSingleResponse(BET_AMOUNT_NOT_ENOUGH, threshold);
            }

            Pokemon pokemon = null;
            if (hasPokemonParamInHisu(params))
            {
                pokemon = getPokemonFromHisuiParams(params);
                if (pokemon == null)
                {
                    return formatSingleResponse(BET_POKEMON_ERROR);
                }
            }

            User user = userManagement.getUser(username);

            ShinyBetHisui currentBet = database.get(ShinyBetHisuiDAO.class).getByUsername(user.getUsername());
            if(currentBet != null)
            {
                Long changedBalance = user.getBalance() + currentBet.getBetAmount();
                if (changedBalance < betAmount)
                {
                    String balance = user.getBalance().toString();
                    String currentBetAmount = currentBet.getBetAmount().toString();

                    return formatSingleResponse(UPDATE_INSUFFICIENT_FUNDS_KEY, MARK, balance, MARK, currentBetAmount, MARK);
                }

                user.incrementBalance(currentBet.getBetAmount().longValue());
                user.decrementBalance(betAmount.longValue());

                currentBet.setBetAmount(betAmount);
                currentBet.setNumberOfChecks(numberOutbreaks);
                currentBet.setPokemon(pokemon);

                database.save(currentBet, user);

                String response = messages.format(BET_HISUI_UPDATE_SUCCESS_KEY, username, betAmount, MARK, numberOutbreaks);
                if (pokemon != null)
                {
                    response += " " + messages.format(BET_HISUI_POKEMON_KEY, pokemon.getName());
                }

                return ListenerResponse.relayAll().addMessage(response);
            }
            else
            {
                if (user.getBalance() < betAmount)
                {
                    String balance = user.getBalance().toString();

                    return formatSingleResponse(INSUFFICIENT_FUNDS_KEY, MARK, balance, MARK);
                }

                ShinyBetHisui newBet = new ShinyBetHisui();
                newBet.setUser(user);
                newBet.setBetAmount(betAmount);
                newBet.setNumberOfChecks(numberOutbreaks);
                newBet.setPokemon(pokemon);

                user.decrementBalance(betAmount.longValue());

                database.save(newBet, user);

                String response = messages.format(BET_HISUI_SUCCESS_KEY, username, betAmount, MARK, numberOutbreaks);
                if (pokemon != null)
                {
                    response += " " + messages.format(BET_HISUI_POKEMON_KEY, pokemon.getName());
                }

                return ListenerResponse.relayAll().addMessage(response);
            }
        }
        return formatSingleResponse(BET_HISUI_WRONG_FORMAT_KEY);
    }

    private boolean hasPokemonParamInHisu(String params)
    {
        //[bet amount] [outbreaks] ([pokmon])
        String[] p = params.split(" ");
        return p.length >= 3;
    }

    private Pokemon getPokemonFromHisuiParams(String params)
    {
        //[bet amount] [outbreaks] ([pokemon])
        try
        {
            String[] p = params.split(" ");
            String poke = p[2];
            return pokemonManagement.getPokemonFromText(poke);
        }
        catch (Exception e)
        {
        }
        return null;
    }

    public Integer getBetAmountFromHisuiParams(String params)
    {
        //[bet amount] [outbreaks] ([pokemon])
        try
        {
            String[] p = params.split(" ");
            String amount = p[0];
            return Integer.parseInt(amount);
        }
        catch(Exception e)
        {
        }
        return null;
    }

    public Integer getNumOutbreakFromHisuiParams(String params)
    {
        //[bet amount] [outbreaks] ([pokemon])
        try
        {
            String[] p = params.split(" ");
            String outbreaks = p[1];
            return Integer.parseInt(outbreaks);
        }
        catch(Exception e)
        {
        }
        return null;
    }

    private ListenerResponse placeBetLetsGo(String username, String params)
    {
        Pokemon pokemon = getPokemonFromLetsGoParams(params);
        Integer time = getTimeFromLetsGoParams(params);
        Integer betAmount = getBetAmountFromLetsGoParams(params);

        if (pokemon != null && time != null && betAmount != null)
        {
            String mark = config.getCurrencyMark();

            if (time < 0)
            {
                return formatSingleResponse(BET_TIME_NOT_ENOUGH);
            }

            if (betAmount <= 0)
            {
                return formatSingleResponse(BET_AMOUNT_NEGATIVE, mark);
            }

            User user = userManagement.getUser(username);

            ShinyBetLetsGo currentBet = database.get(ShinyBetLetsGoDAO.class).getByUsernameAndPokemon(username, pokemon.getId());
            if(currentBet != null)
            {
                Long changedBalance = user.getBalance() + currentBet.getBetAmount();
                if (changedBalance < betAmount)
                {
                    String balance = user.getBalance().toString();
                    String currentBetAmount = currentBet.getBetAmount().toString();

                    return formatSingleResponse(UPDATE_INSUFFICIENT_FUNDS_KEY, mark, balance, mark, currentBetAmount, mark);
                }

                user.incrementBalance(currentBet.getBetAmount().longValue());
                user.decrementBalance(betAmount.longValue());

                currentBet.setBetAmount(betAmount);
                currentBet.setTimeMinutes(time);

                database.save(currentBet, user);

                return formatAllResponse(BET_LETSGO_UPDATE_SUCCESS_KEY, username, pokemon.getName(), time.toString(), betAmount.toString(), mark);
            }
            else
            {
                if (user.getBalance() < betAmount)
                {
                    String balance = user.getBalance().toString();

                    return formatSingleResponse(INSUFFICIENT_FUNDS_KEY, mark, balance, mark);
                }

                ShinyBetLetsGo newBet = new ShinyBetLetsGo();
                newBet.setUser(user);
                newBet.setPokemonId(pokemon.getId());
                newBet.setTimeMinutes(time);
                newBet.setBetAmount(betAmount);

                user.decrementBalance(betAmount.longValue());

                database.save(newBet, user);

                return formatAllResponse(BET_LETSGO_SUCCESS_KEY, username, pokemon.getName(), time.toString(), betAmount.toString(), mark);
            }
        }
        return formatSingleResponse(BET_LETSGO_WRONG_FORMAT_KEY);
    }

    private Pokemon getPokemonFromLetsGoParams(String params)
    {
        String[] p = params.trim().split(" ");

        String pokemonName = p[0];
        String next = p[1];
        if (!isNumber(next))
        {
            pokemonName += next;
        }
        Pokemon pokemon = pokemonManagement.getPokemonFromText(pokemonName);
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


    private Integer getTimeFromLetsGoParams(String params)
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

    private Integer getBetAmountFromLetsGoParams(String params)
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

        if (currentBettingType != null && currentBettingType.isHisui())
        {
            ShinyBetHisui bet = database.get(ShinyBetHisuiDAO.class).getByUsername(username);
            if (bet != null)
            {
                Integer betAmount = bet.getBetAmount();

                User user = userManagement.getUser(username);
                user.incrementBalance(betAmount.longValue());

                database.delete(bet);
                database.save(user);

                return formatAllResponse(BET_HISUI_CANCELED_KEY, username);
            }
            return formatSingleResponse(BET_CANCEL_HISUI_NO_BET_KEY, username);
        }

        if (currentBettingType != null && currentBettingType.isLetsGo())
        {
            Pokemon pokemon = pokemonManagement.getPokemonFromText(params);
            if (pokemon != null)
            {
                ShinyBetLetsGo bet = database.get(ShinyBetLetsGoDAO.class).getByUsernameAndPokemon(username, pokemon.getId());
                if (bet != null)
                {
                    Integer betAmount = bet.getBetAmount();

                    User user = userManagement.getUser(username);
                    user.incrementBalance(betAmount.longValue());

                    database.delete(bet);
                    database.save(user);

                    return formatAllResponse(BET_LETSGO_CANCELED_KEY, pokemon.getName(), username);
                }
                return formatSingleResponse(BET_CANCEL_LETSGO_NO_BET_KEY, username, pokemon.getName());
            }
            return formatSingleResponse(BET_CANCEL_WRONG_FORMAT_KEY);
        }
        return formatSingleResponse(BETS_ARE_CLOSED_KEY);
    }

    private ListenerResponse cancelAllBets(String username)
    {
        if (currentBettingType != null && currentBettingType.isLetsGo())
        {
            List<ShinyBetLetsGo> bets = database.get(ShinyBetLetsGoDAO.class).getByUsername(username);

            if(bets.size() == 0)
            {
                return formatSingleResponse(NO_CURRENT_BETS_KEY, username);
            }

            User user = userManagement.getUser(username);

            for (ShinyBetLetsGo bet : bets)
            {
                Integer betAmount = bet.getBetAmount();
                user.incrementBalance(betAmount.longValue());

                database.delete(bet);
            }
            database.save(user);

            return formatAllResponse(ALL_BETS_CANCELED_KEY, username);
        }
        return formatSingleResponse(BETS_ARE_CLOSED_KEY);
    }

    protected ListenerResponse openBetting(String command)
    {
        String[] p = command.trim().split(" ");

        BettingType type = BettingType.getType(p[1]);

        if (type != null)
        {
            this.currentBettingType = type;

            return formatAllResponse(type.openMessageKey);
        }
        return formatSingleResponse(BETS_NOW_OPEN_ERROR_KEY);
    }

    protected void closeBetting()
    {
        currentBettingType = null;
    }

    private boolean isBettingCurrentlyOpen()
    {
        return currentBettingType != null;
    }

    private enum BettingType
    {
        LETS_GO(ShinyBetServiceImpl.LETS_GO, BETS_NOW_OPEN_LETSGO_KEY),
        HISUI(ShinyBetServiceImpl.HISUI, BETS_NOW_OPEN_HISUI_KEY);

        private final String key;
        private final String openMessageKey;

        private BettingType(String key, String openMessageKey)
        {
            this.key = key;
            this.openMessageKey = openMessageKey;
        }

        public boolean isLetsGo()
        {
            return LETS_GO == this;
        }

        public boolean isHisui()
        {
            return HISUI == this;
        }

        public static BettingType getType(String key)
        {
            for (BettingType type : values())
            {
                if (type.key.equals(key))
                {
                    return type;
                }
            }
            return null;
        }
    }
}
