package com.schlock.bot;

import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.ListenerService;
import com.schlock.bot.twitch.ChangeColorOnJoin;
import com.schlock.bot.twitch.Commands;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.cap.EnableCapHandler;
import org.pircbotx.hooks.ListenerAdapter;

import java.util.Set;

public class TwitchBot extends AbstractBot
{
    private Configuration configuration;

    private PircBotX bot;

    private final ListenerAdapter commands;
    private final ListenerAdapter changeColor;

    public TwitchBot(Set<ListenerService> listeners,
                     DeploymentContext context)
    {
        super(listeners, context);

        commands = new Commands(listeners, context);
        changeColor = new ChangeColorOnJoin(context);
    }

    public void startup() throws Exception
    {
        String botName = getContext().getTwitchBotName();
        String oauth = getContext().getTwitchOAuthToken();
        String channel = getContext().getTwitchChannel();

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
