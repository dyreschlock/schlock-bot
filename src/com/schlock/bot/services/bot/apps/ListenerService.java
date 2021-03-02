package com.schlock.bot.services.bot.apps;

import java.util.List;

public interface ListenerService
{
    String NULL_RESPONSE_KEY = "null-response";
    String NOT_ADMIN_KEY = "not-admin-response";

    boolean isAcceptRequest(String username, String message);

    boolean isTerminateAfterRequest();

    List<String> process(String username, String command);
}
