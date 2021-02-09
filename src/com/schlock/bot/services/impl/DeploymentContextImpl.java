package com.schlock.bot.services.impl;

import com.schlock.bot.services.DeploymentContext;

import java.util.Properties;

public class DeploymentContextImpl implements DeploymentContext
{
    private static final String DISCORD_TOKEN = "discord.token";

    private static final String DATA_DIRECTORY = "data/";

    private final Properties properties;

    public DeploymentContextImpl(Properties properties)
    {
        this.properties = properties;
    }


    public String getDataDirectory()
    {
        return DATA_DIRECTORY;
    }

    public String getDiscordToken()
    {
        return properties.getProperty(DISCORD_TOKEN);
    }
}
