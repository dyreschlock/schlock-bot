package com.schlock.bot.services.bot.twitch;

import com.schlock.bot.services.DeploymentConfiguration;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;

public class ChangeColorOnJoin extends ListenerAdapter
{
    private static final String SET_COLOR = "/color BlueViolet";

    private final DeploymentConfiguration config;

    public ChangeColorOnJoin(DeploymentConfiguration config)
    {
        this.config = config;
    }

    public void onJoin(JoinEvent event) throws Exception
    {
        String botName = config.getTwitchBotName();

        if (event.getUser().getNick().equalsIgnoreCase(botName))
        {
            event.getChannel().send().message(SET_COLOR);
        }
    }
}
