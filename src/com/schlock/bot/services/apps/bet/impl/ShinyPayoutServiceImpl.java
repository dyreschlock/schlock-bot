package com.schlock.bot.services.apps.bet.impl;

import com.schlock.bot.entities.Persisted;
import com.schlock.bot.entities.apps.bet.ShinyBet;
import com.schlock.bot.services.DatabaseModule;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.apps.bet.ShinyPayoutService;
import com.schlock.bot.services.database.apps.ShinyBetDAO;

import java.util.*;

public class ShinyPayoutServiceImpl implements ShinyPayoutService
{
    private static final String SHINY_GET_COMMAND = "!shinyget ";

    private final DatabaseModule database;
    private final DeploymentContext context;


    public ShinyPayoutServiceImpl(DatabaseModule database,
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
        if (username.equals(admin) &&
                command.startsWith(SHINY_GET_COMMAND))
        {
            Map<String, Persisted> tobeUpdated = new HashMap<>();
            List<Persisted> tobeDeleted = new ArrayList<>();

            List<ShinyBet> bets = database.get(ShinyBetDAO.class).getAll();

            


            tobeDeleted.addAll(bets);

            database.save(tobeUpdated.values());
            database.delete(tobeDeleted);
        }
        return Collections.EMPTY_LIST;
    }
}
