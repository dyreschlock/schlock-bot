package com.schlock.bot.services.bot.apps.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.bot.apps.AbstractListenerService;
import com.schlock.bot.services.bot.apps.ListenerResponse;
import com.schlock.bot.services.bot.apps.UserService;
import com.schlock.bot.services.database.apps.UserDAO;
import org.apache.tapestry5.ioc.Messages;

import java.util.Date;

public class UserServiceImpl extends AbstractListenerService implements UserService
{
    private final String BALANCE_COMMAND = "!balance";
    private final String GIVEPOINTS_COMMAND = "!givepoints %s ";
    private final String CASHOUT_COMMAND = "!cashout ";

    private static final String USER_BALANCE_KEY = "user-balance";

    private static final String GIVE_POINTS_ERROR_KEY = "user-give-points-error";
    private static final String GIVE_POINTS_KEY = "user-give-points";

    private static final String CASHOUT_WRONG_MESSAGE_KEY = "user-cashout-error";
    private static final String CASHOUT_TO_ELEMENTS_MESSAGE = "!givepoints %s %s";


    private final UserDAO userDAO;

    private final DeploymentConfiguration config;

    public UserServiceImpl(UserDAO userDAO,
                           Messages messages,
                           DeploymentConfiguration config)
    {
        super(messages);

        this.userDAO = userDAO;
        this.config = config;
    }

    private String getGivePointsCommand()
    {
        return String.format(GIVEPOINTS_COMMAND, config.getTwitchBotName());
    }

    public boolean isAcceptRequest(String username, String in)
    {
        return in != null &&
                (in.toLowerCase().startsWith(BALANCE_COMMAND) ||
                        in.toLowerCase().startsWith(getGivePointsCommand()) ||
                        in.toLowerCase().startsWith(CASHOUT_COMMAND));
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    public ListenerResponse process(String username, String in)
    {
        String command = in.toLowerCase();
        if (command.startsWith(BALANCE_COMMAND))
        {
            return checkBalance(username);
        }
        if (command.startsWith(getGivePointsCommand()))
        {
            return addPoints(username, in);
        }
        if (command.startsWith(CASHOUT_COMMAND))
        {
            return exchangePoints(username, in);
        }

        return null;
    }

    private ListenerResponse checkBalance(String username)
    {
        User user = getUser(username);

        String balance = user.getBalance().toString();

        return singleResponseFormat(USER_BALANCE_KEY, username, balance, config.getCurrencyMark());
    }

    public ListenerResponse addPoints(String username, String in)
    {
        Integer points;
        try
        {
            points = removePointsFromCommand(getGivePointsCommand(), in);
        }
        catch (NumberFormatException e)
        {
            return singleResponseFormat(GIVE_POINTS_ERROR_KEY, config.getTwitchBotName());
        }

        User user = getUser(username);
        user.incrementBalance(points);

        userDAO.save(user);
        userDAO.commit();

        return singleResponseFormat(GIVE_POINTS_KEY, points.toString(), config.getCurrencyMark(), username, user.getBalance().toString());
    }

    private Integer removePointsFromCommand(String command, String input) throws NumberFormatException
    {
        String points = input.substring(command.length());
        points = points.trim();

        return Integer.parseInt(points);
    }

    public ListenerResponse exchangePoints(String username, String in)
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

        User user = getUser(username);
        user.decrementBalance(points);

        userDAO.save(user);
        userDAO.commit();

        return singleResponseFormat(CASHOUT_TO_ELEMENTS_MESSAGE, username, points.toString());
    }

    public User getUser(String username)
    {
        User user = userDAO.getByUsername(username);
        if (user == null)
        {
            user = createNewUser(username);
        }
        return user;
    }

    private User createNewUser(String username)
    {
        User user = new User(username);
        user.setBalance(config.getUserDefaultBalance());
        user.setFollowDate(new Date());

        userDAO.save(user);

        return user;
    }
}
