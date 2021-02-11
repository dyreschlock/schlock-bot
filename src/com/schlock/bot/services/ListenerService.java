package com.schlock.bot.services;

public interface ListenerService
{
    boolean isCommand(String message);

    String process(String username, String command);
}
