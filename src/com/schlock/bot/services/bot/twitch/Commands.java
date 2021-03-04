package com.schlock.bot.services.bot.twitch;

import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.bot.Bot;
import com.schlock.bot.services.bot.apps.ListenerResponse;
import com.schlock.bot.services.bot.apps.ListenerService;
import com.schlock.bot.services.bot.discord.DiscordBot;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.HashMap;
import java.util.Set;

public class Commands extends ListenerAdapter
{
    private final Set<ListenerService> listeners;

    private final DiscordBot discordBot;
    private final DeploymentConfiguration config;

    public Commands(Set<ListenerService> listeners,
                    DiscordBot discordBot,
                    DeploymentConfiguration config)
    {
        this.listeners = listeners;

        this.discordBot = discordBot;
        this.config = config;
    }

    public void onMessage(MessageEvent event) throws Exception
    {
        String message = event.getMessage().toLowerCase();
        String username = event.getUser().getNick();

        if (message.startsWith(Bot.PING))
        {
            event.getChannel().send().message(Bot.PONG);
            discordBot.relayMessage(Bot.PONG);

            return;
        }

        for (ListenerService service : listeners)
        {
            if (service.isAcceptRequest(username, message))
            {
                ListenerResponse responses = service.process(username, message);
                for (String response : responses.getMessages())
                {
                    if (response != null && !response.isEmpty())
                    {
                        event.getChannel().send().message(response);

                        if (responses.isRelayAll())
                        {
                            discordBot.relayMessage(response);
                        }
                    }
                }

                if(service.isTerminateAfterRequest())
                {
                    return;
                }
            }
        }

        HashMap<String, String> commands = config.getListenerCommands();
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
