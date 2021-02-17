package com.schlock.bot.services;

import java.util.List;

public interface ListenerService
{
    public static final String NULL_RESPONSE = "What?";

    boolean isAcceptRequest(String message);

    boolean isTerminateAfterRequest();

    List<String> process(String username, String command);
}
