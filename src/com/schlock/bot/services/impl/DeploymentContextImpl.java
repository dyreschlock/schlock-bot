package com.schlock.bot.services.impl;

import com.schlock.bot.services.DeploymentContext;

import java.util.Properties;

public class DeploymentContextImpl implements DeploymentContext
{
    private static final String DATA_LOCATION = "data.location.local";

    private static final String DISCORD_TOKEN = "discord.token";


    private final Properties properties;

    public DeploymentContextImpl(Properties properties)
    {
        this.properties = properties;
    }


    public String getDataDirectory()
    {
        return properties.getProperty(DATA_LOCATION);
    }

    public String getDiscordToken()
    {
        return properties.getProperty(DISCORD_TOKEN);
    }
}
