package com.schlock.bot.services.bot.twitch.impl;

import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.bot.AbstractBot;
import com.schlock.bot.services.bot.apps.ListenerService;
import com.schlock.bot.services.bot.twitch.ChangeColorOnJoin;
import com.schlock.bot.services.bot.twitch.Commands;
import com.schlock.bot.services.bot.twitch.TwitchBot;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.EnableCapHandler;
import org.pircbotx.hooks.ListenerAdapter;

import java.util.Set;

public class TwitchBotImpl extends AbstractBot implements TwitchBot
{
    private Configuration configuration;

    private PircBotX bot;

    private final ListenerAdapter commands;
    private final ListenerAdapter changeColor;

    public TwitchBotImpl(Set<ListenerService> listeners,
                         DeploymentConfiguration config)
    {
        super(listeners, config);

        commands = new Commands(listeners, config);
        changeColor = new ChangeColorOnJoin(config);
    }

    protected void startService() throws Exception
    {
        String botName = getConfig().getTwitchBotName();
        String oauth = getConfig().getTwitchOAuthToken();
        String channel = getConfig().getTwitchChannel();

        configuration = new Configuration.Builder()
                .setName(botName)
                .setLogin(botName)
                .addServer("irc.chat.twitch.tv", 6667)
                .setServerPassword(oauth)
                .setAutoReconnect(true)
                .setAutoReconnectAttempts(5)
                .addCapHandler(new EnableCapHandler("twitch.tv/membership"))
                .addCapHandler(new EnableCapHandler("twitch.tv/tags"))
                .addCapHandler(new EnableCapHandler("twitch.tv/commands"))
                .addAutoJoinChannel(channel)
                .addListener(commands)
                .addListener(changeColor)
                .buildConfiguration();

        bot = new PircBotX(configuration);
        bot.startBot();
    }
}
