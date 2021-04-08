package com.schlock.bot.services.commands.base.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.AbstractListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.base.UserBalanceService;
import com.schlock.bot.services.database.adhoc.DatabaseManager;
import com.schlock.bot.services.database.base.UserDAO;
import org.apache.tapestry5.ioc.Messages;

public class UserBalanceServiceImpl extends AbstractListenerService implements UserBalanceService
{
    protected static final String BALANCE_KEY = "user-balance";
    protected static final String BALANCE_WRONG_FORMAT_KEY = "user-balance-wrong-format";
    protected static final String BALANCE_USER_UNKNOWN_KEY = "user-balance-user-unknown";

    protected static final String BALANCE_COMMAND = "!balance ";

    private final DatabaseManager database;
    private final DeploymentConfiguration config;

    public UserBalanceServiceImpl(DatabaseManager database,
                                  Messages messages,
                                  DeploymentConfiguration config)
    {
        super(messages);

        this.database = database;
        this.config = config;
    }

    public boolean isAcceptRequest(String username, String message)
    {
        return message != null &&
                message.toLowerCase().startsWith(BALANCE_COMMAND);
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    protected ListenerResponse processRequest(String username, String command)
    {
        String params = command.toLowerCase().substring(BALANCE_COMMAND.length()).trim();
        if (params.isEmpty() || params.contains(" "))
        {
            String message = messages.get(BALANCE_WRONG_FORMAT_KEY);
            return ListenerResponse.relaySingle().addMessage(message);
        }

        User user = database.get(UserDAO.class).getByUsername(params);
        if (user == null)
        {
            String message = messages.format(BALANCE_USER_UNKNOWN_KEY, params);
            return ListenerResponse.relaySingle().addMessage(message);
        }

        final String MARK = config.getCurrencyMark();
        long points = user.getBalance();

        String message = messages.format(BALANCE_KEY, username, points, MARK);
        message += user.getPrestigeLevel();

        return ListenerResponse.relaySingle().addMessage(message);
    }
}
