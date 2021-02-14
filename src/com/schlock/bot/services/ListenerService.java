package com.schlock.bot.services;

public interface ListenerService
{
    boolean isAcceptRequest(String message);

    boolean isTerminateAfterRequest();

    String process(String username, String command);
}
