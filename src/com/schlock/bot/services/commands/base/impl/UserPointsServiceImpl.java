package com.schlock.bot.services.commands.base.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.AbstractListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.base.UserPointsService;
import com.schlock.bot.services.database.adhoc.DatabaseManager;
import com.schlock.bot.services.database.pokemon.ShinyBetDAO;
import com.schlock.bot.services.entities.base.UserManagement;
import org.apache.tapestry5.ioc.Messages;

import java.util.List;

public class UserPointsServiceImpl extends AbstractListenerService implements UserPointsService
{
    protected static final String BALANCE_COMMAND_1 = "!balance";
    protected static final String BALANCE_COMMAND_2 = "!points";
    protected static final String BALANCE_COMMAND_3 = "!score";
    protected static final String GIVEPOINTS_COMMAND = "!givepoints %s ";
    protected static final String CASHOUT_COMMAND = "!cashout ";
    protected static final String PRESTIGE_COMMAND = "!prestige";

    private static final String YES_PARAM = "yes";

    protected static final String USER_BALANCE_KEY = "user-balance";

    protected static final String USER_ADDED_MULTIPLIER_KEY = "user-added-multiplier";

    private static final String GIVE_POINTS_ERROR_KEY = "user-give-points-error";
    private static final String GIVE_POINTS_KEY = "user-give-points";

    protected static final String PRESTIGE_CONFIRM_KEY = "prestige-confirm";
    protected static final String PRESTIGE_SUCCESS_KEY = "prestige-success";
    protected static final String PRESTIGE_NOT_ENOUGH_POINTS_KEY = "prestige-not-enough-points";
    protected static final String PRESTIGE_CANT_WITH_BETS_KEY = "prestige-cant-with-bets";

    private static final String CASHOUT_WRONG_MESSAGE_KEY = "user-cashout-error";
    private static final String CASHOUT_TO_ELEMENTS_MESSAGE = "!givepoints %s %s";


    private final UserManagement userManagement;
    private final DatabaseManager database;

    private final DeploymentConfiguration config;

    public UserPointsServiceImpl(UserManagement userManagement,
                                 DatabaseManager database,
                                 Messages messages,
                                 DeploymentConfiguration config)
    {
        super(messages);

        this.userManagement = userManagement;
        this.database = database;
        this.config = config;
    }

    private String getGivePointsCommand()
    {
        return String.format(GIVEPOINTS_COMMAND, config.getTwitchBotName());
    }

    public boolean isAcceptRequest(String username, String in)
    {
        return in != null &&
                (in.toLowerCase().startsWith(BALANCE_COMMAND_1) ||
                        in.toLowerCase().startsWith(BALANCE_COMMAND_2) ||
                        in.toLowerCase().startsWith(BALANCE_COMMAND_3) ||
                        in.toLowerCase().startsWith(getGivePointsCommand()) ||
                        in.toLowerCase().startsWith(CASHOUT_COMMAND) ||
                        in.toLowerCase().startsWith(PRESTIGE_COMMAND));
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    protected ListenerResponse processRequest(String username, String in)
    {
        String command = in.toLowerCase();
        if (command.startsWith(BALANCE_COMMAND_1) ||
            command.startsWith(BALANCE_COMMAND_2) ||
            command.startsWith(BALANCE_COMMAND_3))
        {
            return checkBalance(username);
        }
        if (command.startsWith(PRESTIGE_COMMAND))
        {
            return prestigeUser(username, in);
        }
//        if (command.startsWith(getGivePointsCommand()))
//        {
//            return addPoints(username, in);
//        }
//        if (command.startsWith(CASHOUT_COMMAND))
//        {
//            return exchangePoints(username, in);
//        }

        return null;
    }

    private ListenerResponse checkBalance(String username)
    {
        User user = userManagement.getUser(username);

        String balance = user.getBalance().toString();

        return formatSingleResponse(USER_BALANCE_KEY, username, balance, config.getCurrencyMark());
    }

    protected ListenerResponse addPoints(String username, String in)
    {
        Integer points;
        try
        {
            points = removePointsFromCommand(getGivePointsCommand(), in);
        }
        catch (NumberFormatException e)
        {
            return formatSingleResponse(GIVE_POINTS_ERROR_KEY, config.getTwitchBotName());
        }

        User user = userManagement.getUser(username);
        user.incrementBalance(points);

        database.save(user);

        return formatSingleResponse(GIVE_POINTS_KEY, points.toString(), config.getCurrencyMark(), username, user.getBalance().toString());
    }

    private Integer removePointsFromCommand(String command, String input) throws NumberFormatException
    {
        String points = input.substring(command.length());
        points = points.trim();

        return Integer.parseInt(points);
    }

    protected ListenerResponse exchangePoints(String username, String in)
    {
        Integer points;
        try
        {
            points = removePointsFromCommand(CASHOUT_COMMAND, in);
        }
        catch (NumberFormatException e)
        {
            String response = messages.get(CASHOUT_WRONG_MESSAGE_KEY);
            return ListenerResponse.relaySingle().addMessage(response);
        }

        User user = userManagement.getUser(username);
        user.decrementBalance(points);

        database.save(user);

        return formatSingleResponse(CASHOUT_TO_ELEMENTS_MESSAGE, username, points.toString());
    }

    protected ListenerResponse prestigeUser(String username, String in)
    {
        User user = userManagement.getUser(username);

        Long prestigeValue = userManagement.getUserPrestigeValue(user);
        if (user.getBalance() < prestigeValue)
        {
            final String MARK = config.getCurrencyMark();

            String message = messages.format(PRESTIGE_NOT_ENOUGH_POINTS_KEY, prestigeValue, MARK, user.getBalance(), MARK);
            return ListenerResponse.relaySingle().addMessage(message);
        }

        if (isUserHasBets(user))
        {
            String message = messages.get(PRESTIGE_CANT_WITH_BETS_KEY);
            return ListenerResponse.relaySingle().addMessage(message);
        }

        String params = in.substring(PRESTIGE_COMMAND.length());
        if (!params.contains(YES_PARAM))
        {
            String message = messages.get(PRESTIGE_CONFIRM_KEY);
            return ListenerResponse.relaySingle().addMessage(message);
        }

        boolean success = userManagement.prestigeUser(user);
        if (success)
        {
            String message = messages.format(PRESTIGE_SUCCESS_KEY, user.getUsername(), user.getPrestigeLevel());
            String newMultiplier = messages.format(USER_ADDED_MULTIPLIER_KEY, user.getUsername(), user.getPointsDoubler());

            return ListenerResponse.relayAll().addMessage(message).addMessage(newMultiplier);
        }

        return nullResponse();
    }

    private boolean isUserHasBets(User user)
    {
        List list = database.get(ShinyBetDAO.class).getByUsername(user.getUsername());
        return !list.isEmpty();
    }
}
