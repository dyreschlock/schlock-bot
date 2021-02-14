package com.schlock.bot.services.apps;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.services.ListenerService;

public interface UserService extends ListenerService
{
    public User getUser(String username);
}
