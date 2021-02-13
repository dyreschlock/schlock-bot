package com.schlock.bot.services.apps.bet.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.services.DatabaseModule;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.apps.bet.BettingService;
import com.schlock.bot.services.apps.pokemon.PokemonService;
import com.schlock.bot.services.database.apps.UserDAO;

public class BettingServiceImpl implements BettingService
{
    private final String INSTRUCTIONS_COMMAND = "!instructions";
    private final String BET_COMMAND = "!bet ";
    private final String BALANCE_COMMAND = "!balance ";
    private final String WIN_COMMAND = "!win ";

    private final PokemonService pokemonService;

    private final DatabaseModule database;

    private final DeploymentContext deploymentContext;

    public BettingServiceImpl(PokemonService pokemonService,
                              DatabaseModule database,
                              DeploymentContext deploymentContext)
    {
        this.pokemonService = pokemonService;

        this.database = database;

        this.deploymentContext = deploymentContext;
    }

    public boolean isCommand(String in)
    {
        return in != null &&
                (in.toLowerCase().startsWith(BET_COMMAND) ||
                        in.toLowerCase().startsWith(BALANCE_COMMAND) ||
                        in.toLowerCase().startsWith(WIN_COMMAND));
    }

    public String process(String username, String in)
    {
        String command = in.toLowerCase();
        if (command.startsWith(INSTRUCTIONS_COMMAND))
        {
            return returnInstructions(username, in);
        }
        if (command.startsWith(BET_COMMAND))
        {
            return placeBet(username, in);
        }
        if (command.startsWith(BALANCE_COMMAND))
        {
            return returnBalance(username, in);
        }
        if (command.startsWith(WIN_COMMAND))
        {
            return roundComplete(username, in);
        }
        return null;
    }

    private String returnInstructions(String username, String in)
    {
        return "";
    }

    private String placeBet(String username, String in)
    {
        User user = database.get(UserDAO.class).getByUsername(username);



        return String.format("Someday you can bet, %s!", username);
    }

    private String returnBalance(String username, String in)
    {

        return String.format("Someday you can balance, %s!", username);
    }

    private String roundComplete(String username, String string)
    {

        return String.format("Someday you can win, %s!", username);
    }
}
