package com.schlock.bot.services.bot.twitch.chat;

import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.bot.Bot;
import com.schlock.bot.services.bot.discord.DiscordBot;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.ListenerService;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Set;

public class Commands extends ListenerAdapter
{
    private static final Logger log = LoggerFactory.getLogger(Commands.class);

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

        log.info("INCOMING -- [" + username + "] " + message);

        if (message.startsWith(Bot.PING))
        {
            event.getChannel().send().message(Bot.PONG);
            discordBot.relayMessage(Bot.PONG);

            log.info("OUT from Ping -- [" + username + "] " + message);
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
                    log.info("OUT from Listener -- [" + username + "] " + message);
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

                log.info("OUT from Short -- [" + username + "] " + message);
                return;
            }
        }

        log.info("OUT from Nothing -- [" + username + "] " + message);
    }
}
