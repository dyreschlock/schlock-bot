package com.schlock.bot.services;

import java.io.IOException;
import java.util.HashMap;

public interface DeploymentConfiguration
{
    String LOCAL = "local";
    String HOSTED = "hosted";
    String TEST = "test";

    void loadProperties() throws IOException;

    Long getUserDefaultBalance();

    Integer getQuizCorrectPoints();

    Double getBetsPokemonWinFactor();

    Double getBetsTimeWinFactor();

    Double getBetsBothWinFactor();

    String getCurrencyMark();

    String getDataDirectory();

    String getOwnerUsername();

    String getBotName();

    String getDiscordToken();

    String getDiscordRelayChannel();

    String getTwitchBotName();

    String getTwitchOAuthIrcToken();

    String getTwitchOAuthWebsocketClientId();

    String getTwitchChannel();

    HashMap<String, String> getListenerCommands();

    String getHibernateProperty(String property);

    String getReadmeFileContents();
}
