package com.schlock.bot.services.impl;

import com.schlock.bot.services.DeploymentConfiguration;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

public class DeploymentConfigurationImpl implements DeploymentConfiguration
{
    private static final String LOCATION = "com.schlock.bot.deploy";


    private static final String README_FILE = "readme.md";

    private static final String CONFIG_PROPERTIES = "config.properties";
    private static final String DEPLOY_PROPERTIES = "deploy.properties";

    private static final String HTML_DATA_DIRECTORY = "html.data.directory";

    private static final String BOT_NAME = "bot.name";

    private static final String USER_DEFAULT_BALANCE = "user.default.balance";
    private static final String USER_PRESTIGE_BASE_VALUE = "user.prestige.base";
    private static final String CURRENCY_MARK = "currency.mark";

    private static final String QUIZ_CORRECT_POINTS = "quiz.correct.points";
    private static final String QUIZ_STREAK_DECAY_MIN = "quiz.streak.decay.min";
    private static final String QUIZ_STREAK_DECAY_MAX = "quiz.streak.decay.max";

    private static final String BETS_LETSGO_POKEMON_WIN_FACTOR = "bets.letsgo.win.factor.pokemon";
    private static final String BETS_LETSGO_TIME_WIN_FACTOR = "bets.letsgo.win.factor.time";
    private static final String BETS_LETSGO_BOTH_WIN_FACTOR = "bets.letsgo.win.factor.both";
    private static final String BETS_LETSGO_EXACT_TIME_WIN_FACTOR = "bets.letsgo.win.factor.exact_time";

    private static final String BETS_HISUI_POKEMON_WIN_FACTOR = "bets.hisui.win.factor.pokemon";
    private static final String BETS_HISUI_OUTBREAK_WIN_FACTOR = "bets.hisui.win.factor.outbreaks";
    private static final String BETS_HISUI_OUTBREAK_POT_WIN_FACTOR = "bets.hisui.win.factor.outbreaks.pot";

    private static final String ONWER_USERNAME = "owner.username";

    private static final String DISCORD_TOKEN = "discord.token";
    private static final String DISCORD_RELAY_CHANNEL = "discord.relay.channel";

    private static final String TWITCH_BOT_NAME = "twitch.bot.name";
    private static final String TWITCH_OAUTH_IRC_TOKEN = "twitch.oauth.irc-token";
    private static final String TWITCH_OAUTH_CLIENT_ID = "twitch.oauth.websocket-client-id";
    private static final String TWITCH_CHANNEL = "twitch.channel";

    private static final String LISTEN_COMMAND_PREFIX = "listen.command.";

    private static final String DATA_DIRECTORY = "data/";

    private String context;

    private Properties properties;

    public DeploymentConfigurationImpl()
    {
    }

    protected String getContext()
    {
        if (context == null)
        {
            String location = System.getProperty(LOCATION);
            if (location == null || location.isEmpty())
            {
                return LOCAL;
            }
            context = location;
        }
        return context;
    }

    private void setContext(String context)
    {
        this.context = context;
    }

    private Properties getProperties()
    {
        if(properties == null)
        {
            try
            {
                loadProperties();
            }
            catch(IOException e)
            {
                throw new RuntimeException(e);
            }
        }
        return properties;
    }

    public void loadProperties() throws IOException
    {
        properties = new Properties();

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

    public Long getUserDefaultBalance()
    {
        String bal = getProperties().getProperty(USER_DEFAULT_BALANCE);
        return Long.parseLong(bal);
    }

    public Long getUserPrestigeBaseValue()
    {
        String val = getProperties().getProperty(USER_PRESTIGE_BASE_VALUE);
        return Long.parseLong(val);
    }

    public String getCurrencyMark()
    {
        return getProperties().getProperty(CURRENCY_MARK);
    }

    public Integer getQuizCorrectPoints()
    {
        String points = getProperties().getProperty(QUIZ_CORRECT_POINTS);
        return Integer.parseInt(points);
    }

    public Integer getQuizStreakDecayMinValue()
    {
        String value = getProperties().getProperty(QUIZ_STREAK_DECAY_MIN);
        return Integer.parseInt(value);
    }

    public Integer getQuizStreakDecayMaxValue()
    {
        String value = getProperties().getProperty(QUIZ_STREAK_DECAY_MAX);
        return Integer.parseInt(value);
    }

    public Double getBetsLetsGoPokemonWinFactor()
    {
        return getFloat(BETS_LETSGO_POKEMON_WIN_FACTOR);
    }

    public Double getBetsLetsGoTimeWinFactor()
    {
        return getFloat(BETS_LETSGO_TIME_WIN_FACTOR);
    }

    public Double getBetsLetsGoExactTimeWinFactor()
    {
        return getFloat(BETS_LETSGO_EXACT_TIME_WIN_FACTOR);
    }

    public Double getBetsLetsGoBothWinFactor()
    {
        return getFloat(BETS_LETSGO_BOTH_WIN_FACTOR);
    }

    private Double getFloat(final String PROPERTY)
    {
        String dbl = getProperties().getProperty(PROPERTY);
        return Double.parseDouble(dbl);
    }

    public Double getBetsHisuiPokemonWinFactor()
    {
        return getFloat(BETS_HISUI_POKEMON_WIN_FACTOR);
    }

    public Double getBetsHisuiOutbreakWinFactor()
    {
        return getFloat(BETS_HISUI_OUTBREAK_WIN_FACTOR);
    }

    public Double getBetsHisuiOutbreakPotWinFactor()
    {
        return getFloat(BETS_HISUI_OUTBREAK_POT_WIN_FACTOR);
    }

    public String getDataDirectory()
    {
        return DATA_DIRECTORY;
    }

    public String getOwnerUsername()
    {
        return getProperties().getProperty(ONWER_USERNAME);
    }

    public String getBotName()
    {
        return getProperties().getProperty(BOT_NAME);
    }

    public String getHTMLDataDirectory()
    {
        return getProperties().getProperty(HTML_DATA_DIRECTORY);
    }

    public String getDiscordToken()
    {
        return getProperties().getProperty(DISCORD_TOKEN);
    }

    public String getDiscordRelayChannel()
    {
        return getProperties().getProperty(DISCORD_RELAY_CHANNEL);
    }

    public String getTwitchBotName()
    {
        return getProperties().getProperty(TWITCH_BOT_NAME);
    }

    public String getTwitchOAuthIrcToken()
    {
        return getProperties().getProperty(TWITCH_OAUTH_IRC_TOKEN);
    }

    public String getTwitchOAuthWebsocketClientId()
    {
        return getProperties().getProperty(TWITCH_OAUTH_CLIENT_ID);
    }

    public String getTwitchChannel()
    {
        String channel = getProperties().getProperty(TWITCH_CHANNEL);
        if (!channel.startsWith("#"))
        {
            channel = "#" + channel;
        }
        return channel;
    }

    public HashMap<String, String> getListenerCommands()
    {
        HashMap<String, String> commands = new HashMap<>();

        Set<String> keys = getProperties().stringPropertyNames();
        for (String key : keys)
        {
            if (key.startsWith(LISTEN_COMMAND_PREFIX))
            {
                String command = "!" + key.substring(LISTEN_COMMAND_PREFIX.length());
                String message = getProperties().getProperty(key);

                commands.put(command, message);
            }
        }
        return commands;
    }

    public String getHibernateProperty(String property)
    {
        String hp = property + "." + getContext();
        return getProperties().getProperty(hp);
    }


    public static DeploymentConfiguration createDeploymentConfiguration(String context)
    {
        DeploymentConfigurationImpl config = new DeploymentConfigurationImpl();
        config.setContext(context);

        return config;
    }

    public String getReadmeFileContents()
    {
        Path readme = Path.of(README_FILE);
        try
        {
            return Files.readString(readme);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
