package com.schlock.bot.services;

import java.util.HashMap;

public interface DeploymentContext
{
    public String getDataDirectory();

    public String getDiscordToken();

    public HashMap<String, String> getListenerCommands();
}
