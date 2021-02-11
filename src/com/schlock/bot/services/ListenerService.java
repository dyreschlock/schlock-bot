package com.schlock.bot.services;

public interface ListenerService
{
    public boolean isCommand(String message);

    public String process(String command);
}
