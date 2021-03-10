package com.schlock.bot.services.database.apps;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.services.database.BaseDAO;

import java.util.List;

public interface UserDAO extends BaseDAO<User>
{
    User getByUsername(String username);

    User getMostRecentUser();

    List<User> getOrderByPoints(int count);
}
