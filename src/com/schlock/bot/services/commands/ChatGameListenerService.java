package com.schlock.bot.services.commands;

public interface ChatGameListenerService extends ListenerService
{
    String getGameId();

    boolean isStarted();

    boolean isOn();

    boolean turnOn();

    boolean turnOff();
}
