package com.schlock.bot.services.bot.apps.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.bot.apps.UserService;
import com.schlock.bot.services.database.apps.UserDAO;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class UserServiceImpl implements UserService
{
    private final String BALANCE_COMMAND = "!balance";
    private final String GIVEPOINTS_COMMAND = "!givepoints %s ";
    private final String CASHOUT_COMMAND = "!cashout ";

    private final UserDAO userDAO;

    private final DeploymentConfiguration config;

    public UserServiceImpl(UserDAO userDAO,
                           DeploymentConfiguration config)
    {
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

    public List<String> process(String username, String in)
    {
        return Arrays.asList(processSingleResult(username, in));
    }

    public String processSingleResult(String username, String in)
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

    private static final String BALANCE_RETURN_FORMAT = "%s your balance is %s%s";

    private String checkBalance(String username)
    {
        User user = getUser(username);

        String balance = user.getBalance().toString();
        return String.format(BALANCE_RETURN_FORMAT, username, balance, config.getCurrencyMark());
    }

    private static final String GIVE_WRONG_MESSAGE = "Wrong format, please use '!givepoints %s 123";
    private static final String GIVE_POINTS_ADDED_MESSAGE = "%s %s added to %s. Current balance: %s";

    public String addPoints(String username, String in)
    {
        Integer points;
        try
        {
            points = removePointsFromCommand(getGivePointsCommand(), in);
        }
        catch (NumberFormatException e)
        {
            return String.format(GIVE_WRONG_MESSAGE, config.getTwitchBotName());
        }

        User user = getUser(username);
        user.incrementBalance(points);

        userDAO.save(user);

        userDAO.commit();

        String message = String.format(GIVE_POINTS_ADDED_MESSAGE, points.toString(), config.getCurrencyMark(), username, user.getBalance().toString());
        return message;
    }

    private Integer removePointsFromCommand(String command, String input) throws NumberFormatException
    {
        String points = input.substring(command.length());
        points = points.trim();

        return Integer.parseInt(points);
    }

    private static final String CASHOUT_WRONG_MESSAGE = "Wrong format, please use '!cashout 123'";
    private static final String CASHOUT_TO_ELEMENTS_MESSAGE = "!givepoints %s %s";

    public String exchangePoints(String username, String in)
    {
        Integer points;
        try
        {
            points = removePointsFromCommand(CASHOUT_COMMAND, in);
        }
        catch (NumberFormatException e)
        {
            return String.format(CASHOUT_WRONG_MESSAGE);
        }

        User user = getUser(username);
        user.decrementBalance(points);

        userDAO.save(user);

        userDAO.commit();

        return String.format(CASHOUT_TO_ELEMENTS_MESSAGE, username, points.toString());
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
