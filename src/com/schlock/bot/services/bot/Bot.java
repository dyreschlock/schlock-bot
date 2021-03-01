package com.schlock.bot.services.bot;

public interface Bot
{
    public final String PING = "!ping";
    public final String PONG = "Pong!";

    public boolean isStarted();

    public void startup() throws Exception;
}
