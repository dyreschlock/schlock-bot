package com.schlock.bot.services;

public interface ListenerService
{
    public static final String NULL_RESPONSE = "What?";

    boolean isAcceptRequest(String message);

    boolean isTerminateAfterRequest();

    String process(String username, String command);
}
