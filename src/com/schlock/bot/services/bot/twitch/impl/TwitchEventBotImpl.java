package com.schlock.bot.services.bot.twitch.impl;

import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.bot.AbstractBot;
import com.schlock.bot.services.bot.twitch.TwitchChatBot;
import com.schlock.bot.services.bot.twitch.TwitchEventBot;
import com.schlock.bot.services.commands.ListenerService;

import java.util.Set;

public class TwitchEventBotImpl extends AbstractBot implements TwitchEventBot
{
    private final TwitchChatBot twitchBot;

    public TwitchEventBotImpl(Set<ListenerService> listeners,
                              TwitchChatBot twitchBot,
                              DeploymentConfiguration config)
    {
        super(listeners, config);

        this.twitchBot = twitchBot;
    }


    protected void startService() throws Exception
    {

    }
}
