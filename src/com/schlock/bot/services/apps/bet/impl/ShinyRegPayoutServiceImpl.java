package com.schlock.bot.services.apps.bet.impl;

import com.schlock.bot.services.DatabaseModule;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.apps.bet.ShinyRegPayoutService;

import java.util.*;

public class ShinyRegPayoutServiceImpl implements ShinyRegPayoutService
{
    private static final String SHINY_GET_COMMAND = "!shinyget ";

    private final DatabaseModule database;
    private final DeploymentContext context;


    public ShinyRegPayoutServiceImpl(DatabaseModule database,
                                     DeploymentContext context)
    {
        this.database = database;
        this.context = context;
    }

    public boolean isAcceptRequest(String username, String in)
    {
        String admin = context.getOwnerUsername();

        return username.equals(admin) &&
                in != null &&
                in.toLowerCase().startsWith(SHINY_GET_COMMAND);
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    public List<String> process(String username, String in)
    {
        String admin = context.getOwnerUsername();

        String command = in.toLowerCase().trim();
        if (command.startsWith(SHINY_GET_COMMAND))
        {

        }
        return Collections.EMPTY_LIST;
    }
}
