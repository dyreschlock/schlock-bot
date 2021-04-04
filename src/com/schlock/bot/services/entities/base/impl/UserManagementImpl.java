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
        user.setPrestige(0);

        user.setHighScorePoints(config.getUserDefaultBalance());
        user.setHighScoreStreak(0);

        return user;
    }

    public Long getUserPrestigeValue(User user)
    {
        Long baseVal = config.getUserPrestigeBaseValue();

        Double factor = Math.pow(2, user.getPrestige().doubleValue());

        Long value = baseVal * factor.longValue();
        return value;
    }

    public boolean prestigeUser(User user)
    {
        Long prestigeValue = getUserPrestigeValue(user);
        if (user.getBalance() < prestigeValue)
        {
            return false;
        }

        int prestige = user.getPrestige();
        prestige++;

        int multiplier = user.getPointsDoubler();
        multiplier++;

        user.setBalance(config.getUserDefaultBalance());
        user.setPrestige(prestige);
        user.setPointsDoubler(multiplier);

        database.save(user);

        return true;
    }
}
