package com.schlock.bot.services.entities.base;

import com.schlock.bot.entities.base.User;

public interface UserManagement
{
    User getUser(String username);

    User createNewDefaultUser(String username);
}
