package com.schlock.bot.services.impl;

import com.schlock.bot.services.DeploymentContext;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class DeploymentContextImpl implements DeploymentContext
{
    private static final String CONFIG_PROPERTIES = "config.properties";
    private static final String DEPLOY_PROPERTIES = "deploy.properties";

    private static final String BOT_NAME = "bot.name";

    private static final String USER_DEFAULT_BALANCE = "user.default.balance";
    private static final String CURRENCY_MARK = "currency.mark";

    private static final String QUIZ_CORRECT_POINTS = "quiz.correct.points";

    private static final String BETS_POKEMON_WIN_FACTOR = "bets.win.factor.pokemon";
    private static final String BETS_TIME_WIN_FACTOR = "bets.win.factor.time";
    private static final String BETS_BOTH_WIN_FACTOR = "bets.win.factor.both";

    private static final String ONWER_USERNAME = "owner.username";

    private static final String DISCORD_TOKEN = "discord.token";

    private static final String TWTICH_BOT_NAME = "twitch.bot.name";
    private static final String TWITCH_OAUTH_TOKEN = "twitch.oauth.token";
    private static final String TWITCH_CHANNEL = "twitch.channel";

    private static final String LISTEN_COMMAND_PREFIX = "listen.command.";

    private static final String DATA_DIRECTORY = "data/";

    private final String context;

    private final Properties properties = new Properties();

    public DeploymentContextImpl(String context)
    {
        this.context = context;
    }

    public void loadProperties() throws IOException
    {
        loadProperties(CONFIG_PROPERTIES);
        loadProperties(DEPLOY_PROPERTIES);
    }

    private void loadProperties(String resource) throws IOException
    {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(resource);
        if (stream != null)
        {
            try
            {
                properties.load(stream);
            }
            finally
            {
                stream.close();
            }
        }
        else
        {
            throw new FileNotFoundException("Property file missing: " + resource);
        }
    }

    public Integer getUserDefaultBalance()
    {
        String bal = properties.getProperty(USER_DEFAULT_BALANCE);
        return Integer.parseInt(bal);
    }

    public String getCurrencyMark()
    {
        return properties.getProperty(CURRENCY_MARK);
    }

    public Integer getQuizCorrectPoints()
    {
        String points = properties.getProperty(QUIZ_CORRECT_POINTS);
        return Integer.parseInt(points);
    }

    public Double getBetsPokemonWinFactor()
    {
        return getFloat(BETS_POKEMON_WIN_FACTOR);
    }

    public Double getBetsTimeWinFactor()
    {
        return getFloat(BETS_TIME_WIN_FACTOR);
    }

    public Double getBetsBothWinFactor()
    {
        return getFloat(BETS_BOTH_WIN_FACTOR);
    }

    private Double getFloat(final String PROPERTY)
    {
        String dbl = properties.getProperty(PROPERTY);
        return Double.parseDouble(dbl);
    }

    public String getDataDirectory()
    {
        return DATA_DIRECTORY;
    }

    public String getOwnerUsername()
    {
        return properties.getProperty(ONWER_USERNAME);
    }

    public String getBotName()
    {
        return properties.getProperty(BOT_NAME);
    }

    public String getDiscordToken()
    {
        return properties.getProperty(DISCORD_TOKEN);
    }

    public String getTwitchBotName()
    {
        return properties.getProperty(TWTICH_BOT_NAME);
    }

    public String getTwitchOAuthToken()
    {
        return properties.getProperty(TWITCH_OAUTH_TOKEN);
    }

    public String getTwitchChannel()
    {
        String channel = properties.getProperty(TWITCH_CHANNEL);
        if (!channel.startsWith("#"))
        {
            channel = "#" + channel;
        }
        return channel;
    }

    public HashMap<String, String> getListenerCommands()
    {
        HashMap<String, String> commands = new HashMap<>();

        Set<String> keys = properties.stringPropertyNames();
        for (String key : keys)
        {
            if (key.startsWith(LISTEN_COMMAND_PREFIX))
            {
                String command = "!" + key.substring(LISTEN_COMMAND_PREFIX.length());
                String message = properties.getProperty(key);

                commands.put(command, message);
            }
        }
        return commands;
    }

    public String getHibernateProperty(String property)
    {
        String hp = property + "." + context;
        return properties.getProperty(hp);
    }
}
