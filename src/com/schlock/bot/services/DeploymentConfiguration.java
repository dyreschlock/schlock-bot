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

    Long getUserPrestigeBaseValue();

    Integer getQuizCorrectPoints();

    Integer getQuizStreakDecayMinValue();

    Integer getQuizStreakDecayMaxValue();

    Double getBetsLetsGoPokemonWinFactor();

    Double getBetsLetsGoTimeWinFactor();

    Double getBetsLetsGoExactTimeWinFactor();

    Double getBetsLetsGoBothWinFactor();

    Double getBetsHisuiPokemonWinFactor();

    Double getBetsHisuiOutbreakWinFactor();

    Double getBetsHisuiOutbreakPotWinFactor();

    String getCurrencyMark();

    String getDataDirectory();

    String getHTMLDataDirectory();

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
