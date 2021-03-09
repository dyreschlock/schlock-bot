package com.schlock.bot.services.commands;

public interface ListenerService
{
    String NULL_RESPONSE_KEY = "null-response";
    String NOT_ADMIN_KEY = "not-admin-response";

    boolean isAcceptRequest(String username, String message);

    boolean isTerminateAfterRequest();

    ListenerResponse process(String username, String command);
}
