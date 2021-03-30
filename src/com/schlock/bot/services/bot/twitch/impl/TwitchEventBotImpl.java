package com.schlock.bot.services.bot.twitch.impl;

import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.bot.AbstractBot;
import com.schlock.bot.services.bot.twitch.TwitchChatBot;
import com.schlock.bot.services.bot.twitch.TwitchEventBot;
import com.schlock.bot.services.bot.twitch.event.TwitchPubSubWebsocketClient;
import com.schlock.bot.services.commands.ListenerService;
import com.schlock.bot.services.impl.DeploymentConfigurationImpl;

import java.util.Set;

public class TwitchEventBotImpl extends AbstractBot implements TwitchEventBot
{
    private final TwitchChatBot twitchBot;

    private TwitchPubSubWebsocketClient clientEndpoint;

    public TwitchEventBotImpl(Set<ListenerService> listeners,
                              TwitchChatBot twitchBot,
                              DeploymentConfiguration config)
    {
        super(listeners, config);

        this.twitchBot = twitchBot;
        this.clientEndpoint = new TwitchPubSubWebsocketClient(twitchBot, config);
    }

    protected void startService() throws Exception
    {
        clientEndpoint.startup();
    }

    public static void main(String[] args) throws Exception
    {
        //testing

        DeploymentConfiguration config = DeploymentConfigurationImpl.createDeploymentConfiguration(DeploymentConfiguration.LOCAL);

        TwitchEventBotImpl bot = new TwitchEventBotImpl(null, null, config);

        Thread thread = new Thread()
        {
            public void run()
            {
                try
                {
                    bot.startup();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
