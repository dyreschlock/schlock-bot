package com.schlock.bot.services.bot.apps;

import com.schlock.bot.entities.apps.User;

public interface UserService extends ListenerService
{
    public User getUser(String username);
}
