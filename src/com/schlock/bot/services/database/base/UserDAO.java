package com.schlock.bot.services.database.base;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.services.database.BaseDAO;

import java.util.List;

public interface UserDAO extends BaseDAO<User>
{
    User getByUsername(String username);

    User getMostRecentUser();

    List<User> getOrderByPoints(int count);

    List<User> getOrderByPoints(int count, List<String> ignoreUsers);
}
