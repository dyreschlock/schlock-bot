package com.schlock.bot;

import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.PokemonService;
import com.schlock.bot.twitch.ChangeColorOnJoin;
import com.schlock.bot.twitch.Commands;
import org.pircbotx.Configuration;
import org.pircbotx.cap.EnableCapHandler;
import org.pircbotx.hooks.ListenerAdapter;

public class TwitchBot extends AbstractBot
{
    private Configuration configuration;

    private final ListenerAdapter commands;
    private final ListenerAdapter changeColor;

    public TwitchBot(PokemonService pokemonService, DeploymentContext context)
    {
        super(pokemonService, context);

        commands = new Commands(pokemonService);
        changeColor = new ChangeColorOnJoin(context);
    }

    public void startup()
    {
        connect();


    }

    public void connect()
    {
        String botName = getContext().getBotName();
        String oauth = getContext().getTwitchOAuthToken();
        String channel = getContext().getTwitchChannel();

        configuration = new Configuration.Builder()
                .setName(botName)
                .setLogin(botName)
                .addServer("irc.chat.twitch.tv", 6667)
                .setServerPassword("oauth:" + oauth)
                .setAutoReconnect(true)
                .setAutoReconnectAttempts(5)
                .addCapHandler(new EnableCapHandler("twitch.tv/membership"))
                .addCapHandler(new EnableCapHandler("twitch.tv/tags"))
                .addCapHandler(new EnableCapHandler("twitch.tv/commands"))
                .addAutoJoinChannel(channel)
                .addListener(commands)
                .addListener(changeColor)
                .buildConfiguration();

    }
}
