package com.schlock.bot.services;

import java.io.IOException;
import java.util.HashMap;

public interface DeploymentContext
{
    String LOCAL = "local";
    String HOSTED = "hosted";
    String TEST = "test";

    void loadProperties() throws IOException;

    Integer getUserDefaultBalance();

    String getCurrencyMark();

    String getDataDirectory();

    String getBotName();

    String getDiscordToken();

    String getTwitchBotName();

    String getTwitchOAuthToken();

    String getTwitchChannel();

    HashMap<String, String> getListenerCommands();

    String getHibernateProperty(String property);
}
