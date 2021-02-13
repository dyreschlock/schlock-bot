package com.schlock.bot.services.apps.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.services.DatabaseModule;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.apps.UserService;
import com.schlock.bot.services.database.apps.UserDAO;

public class UserServiceImpl implements UserService
{
    private final String BALANCE_COMMAND = "!balance";

    private final DatabaseModule database;

    private final DeploymentContext context;

    public UserServiceImpl(DatabaseModule database,
                           DeploymentContext context)
    {
        this.database = database;
        this.context = context;
    }

    public boolean isCommand(String in)
    {
        return in != null &&
                (in.toLowerCase().startsWith(BALANCE_COMMAND));
    }

    public String process(String username, String in)
    {
        String command = in.toLowerCase();
        if (command.startsWith(BALANCE_COMMAND))
        {
            return checkBalance(username);
        }

        return null;
    }

    private static final String BALANCE_RETURN_FORMAT = "%s your balance is %s%s";

    private String checkBalance(String username)
    {
        User user = database.get(UserDAO.class).getByUsername(username);
        if (user == null)
        {
            user = createNewUser(username);
        }

        String balance = user.getBalance().toString();
        return String.format(BALANCE_RETURN_FORMAT, username, balance, context.getCurrencyMark());
    }

    private User createNewUser(String username)
    {
        User user = new User(username);
        user.setBalance(context.getUserDefaultBalance());

        database.save(user);

        return user;
    }
}
