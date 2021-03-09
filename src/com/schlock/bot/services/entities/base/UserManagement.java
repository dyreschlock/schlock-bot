package com.schlock.bot.services.entities.base;

import com.schlock.bot.entities.apps.User;

public interface UserManagement
{
    User getUser(String username);
}
