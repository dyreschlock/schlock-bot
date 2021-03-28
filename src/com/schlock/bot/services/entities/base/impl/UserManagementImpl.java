package com.schlock.bot.services.entities.base.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.database.adhoc.DatabaseManager;
import com.schlock.bot.services.database.base.UserDAO;
import com.schlock.bot.services.entities.base.UserManagement;

import java.util.Date;

public class UserManagementImpl implements UserManagement
{
    private final DatabaseManager database;

    private final DeploymentConfiguration config;

    public UserManagementImpl(DatabaseManager database,
                              DeploymentConfiguration config)
    {
        this.database = database;

        this.config = config;
    }

    public User getUser(String username)
    {
        User user = database.get(UserDAO.class).getByUsername(username);
        if (user == null)
        {
            user = createNewDefaultUser(username);

            database.save(user);
        }
        return user;
    }

    public User createNewDefaultUser(String username)
    {
        User user = new User(username);
        user.setBalance(config.getUserDefaultBalance());
        user.setFollowDate(new Date());
        user.setPointsDoubler(1);

        return user;
    }
}
