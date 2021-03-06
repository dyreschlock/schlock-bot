package com.schlock.bot.services.bot.discord.impl;

import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.bot.AbstractBot;
import com.schlock.bot.services.bot.apps.ListenerResponse;
import com.schlock.bot.services.bot.apps.ListenerService;
import com.schlock.bot.services.bot.discord.DiscordBot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;


public class DiscordBotImpl extends AbstractBot implements DiscordBot
{
    private JDA discordJDA;

    public DiscordBotImpl(Set<ListenerService> listeners,
                          DeploymentConfiguration config)
    {
        super(listeners, config);
    }

    protected void startService() throws Exception
    {
        final String TOKEN = getConfig().getDiscordToken();

        discordJDA = JDABuilder.createDefault(TOKEN).build();
        discordJDA.addEventListener(pingListener());
        discordJDA.addEventListener(createServiceListener());
    }

    private ListenerAdapter pingListener()
    {
        final String BOT_CHANNEL = getConfig().getDiscordRelayChannel();

        return new ListenerAdapter()
        {
            @Override
            public void onMessageReceived(@NotNull MessageReceivedEvent event)
            {
                if(!event.getChannel().getName().equalsIgnoreCase(BOT_CHANNEL)) return;
                if(event.getAuthor().isBot()) return;

                if (event.getMessage().getContentRaw().trim().equalsIgnoreCase(PING))
                {
                    getBotChannel().sendMessage(PONG).queue();
                }
            }
        };
    }

    private ListenerAdapter createServiceListener()
    {
        final String BOT_CHANNEL = getConfig().getDiscordRelayChannel();

        return new ListenerAdapter()
        {
            @Override
            public void onMessageReceived(@NotNull MessageReceivedEvent event)
            {
                if(!event.getChannel().getName().equalsIgnoreCase(BOT_CHANNEL)) return;
                if(event.getAuthor().isBot()) return;

                final String message = event.getMessage().getContentRaw();
                final String username = event.getAuthor().getName();

                for (ListenerService service : getListeners())
                {
                    if (service.isAcceptRequest(username, message))
                    {
                        ListenerResponse responses = service.process(username, message);
                        for(String response : responses.getMessages())
                        {
                            if(response != null)
                            {
                                getBotChannel().sendMessage(response).queue();
                            }
                        }

                        if (service.isTerminateAfterRequest())
                        {
                            return;
                        }
                    }
                }
            }
        };
    }

    private TextChannel getBotChannel()
    {
        final String BOT_CHANNEL = getConfig().getDiscordRelayChannel();
        return discordJDA.getTextChannelsByName(BOT_CHANNEL, true).get(0);
    }

    public void relayMessages(List<String> messages)
    {
        for (String m : messages)
        {
            relayMessage(m);
        }
    }

    public void relayMessage(String message)
    {
        getBotChannel().sendMessage(message).queue();
    }
}
