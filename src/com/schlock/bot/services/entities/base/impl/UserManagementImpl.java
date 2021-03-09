package com.schlock.bot.services.entities.base.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.database.apps.UserDAO;
import com.schlock.bot.services.entities.base.UserManagement;

import java.util.Date;

public class UserManagementImpl implements UserManagement
{
    private final UserDAO userDAO;

    private final DeploymentConfiguration config;

    public UserManagementImpl(UserDAO userDAO,
                              DeploymentConfiguration config)
    {
        this.userDAO = userDAO;

        this.config = config;
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
