package com.schlock.bot.services;

import java.util.HashMap;

public interface DeploymentContext
{
    public String getDataDirectory();

    public String getBotName();

    public String getDiscordToken();

    public String getTwitchOAuthToken();

    public String getTwitchChannel();

    public HashMap<String, String> getListenerCommands();
}
