package com.schlock.bot.services.impl;

import com.schlock.bot.services.DeploymentContext;

import java.util.*;

public class DeploymentContextImpl implements DeploymentContext
{
    private static final String DISCORD_TOKEN = "discord.token";

    private static final String LISTEN_COMMAND_PREFIX = "listen.command.";

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
}
