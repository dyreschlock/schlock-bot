package com.schlock.bot.services.bot.twitch;

import com.schlock.bot.services.DeploymentContext;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.JoinEvent;

public class ChangeColorOnJoin extends ListenerAdapter
{
    private static final String SET_COLOR = "/color BlueViolet";

    private final DeploymentContext context;

    public ChangeColorOnJoin(DeploymentContext context)
    {
        this.context = context;
    }

    public void onJoin(JoinEvent event) throws Exception
    {
        String botName = context.getTwitchBotName();

        if (event.getUser().getNick().equalsIgnoreCase(botName))
        {
            event.getChannel().send().message(SET_COLOR);
        }
    }
}
