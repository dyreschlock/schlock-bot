package com.schlock.bot.services.bot;

import java.util.List;

public interface ListenerService
{
    public static final String NULL_RESPONSE = "What?";
    public static final String NOT_ADMIN_RESPONSE = "Sorry, you are not the admin.";

    boolean isAcceptRequest(String username, String message);

    boolean isTerminateAfterRequest();

    List<String> process(String username, String command);
}
