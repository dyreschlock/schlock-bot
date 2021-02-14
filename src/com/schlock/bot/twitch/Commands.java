package com.schlock.bot.twitch;

import com.schlock.bot.Bot;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.ListenerService;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.HashMap;
import java.util.Set;

public class Commands extends ListenerAdapter
{
    private final Set<ListenerService> listeners;

    private final DeploymentContext context;

    public Commands(Set<ListenerService> listeners,
                    DeploymentContext context)
    {
        this.listeners = listeners;

        this.context = context;
    }

    public void onMessage(MessageEvent event) throws Exception
    {
        String message = event.getMessage().toLowerCase();
        String username = event.getUser().getNick();

        if (message.startsWith(Bot.PING))
        {
            event.getChannel().send().message(Bot.PONG);
            return;
        }

        for (ListenerService service : listeners)
        {
            if (service.isAcceptRequest(message))
            {
                String response = service.process(username, message);
                if (response != null)
                {
                    event.getChannel().send().message(response);
                }

                if(service.isTerminateAfterRequest())
                {
                    return;
                }
            }
        }

        HashMap<String, String> commands = context.getListenerCommands();
        for (String command : commands.keySet())
        {
            if (message.startsWith(command))
            {
                String response = commands.get(command);
                event.getChannel().send().message(response);
                return;
            }
        }
    }
}
