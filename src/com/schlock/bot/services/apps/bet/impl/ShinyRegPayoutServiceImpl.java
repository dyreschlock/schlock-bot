package com.schlock.bot.services.apps.bet.impl;

import com.schlock.bot.services.apps.bet.ShinyRegPayoutService;

import java.util.*;

public class ShinyRegPayoutServiceImpl implements ShinyRegPayoutService
{
    private static final String SHINY_GET_COMMAND = "!shinyget ";



    public boolean isAcceptRequest(String message)
    {
        return message != null &&
                message.toLowerCase().startsWith(SHINY_GET_COMMAND);
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    public List<String> process(String username, String in)
    {
        String command = in.toLowerCase().trim();
        if (command.startsWith(SHINY_GET_COMMAND))
        {

        }
        return Collections.EMPTY_LIST;
    }
}
