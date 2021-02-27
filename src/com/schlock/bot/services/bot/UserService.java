package com.schlock.bot.services.bot;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.services.bot.apps.ListenerService;

public interface UserService extends ListenerService
{
    public User getUser(String username);
}
