package com.schlock.bot.services.apps.bet.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.services.DatabaseModule;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.apps.UserService;
import com.schlock.bot.services.apps.bet.ShinyBetService;
import com.schlock.bot.services.apps.pokemon.PokemonService;
import com.schlock.bot.services.database.apps.UserDAO;

public class ShinyBetServiceImpl implements ShinyBetService
{
    private final String INSTRUCTIONS_COMMAND = "!instructions";
    private final String BET_COMMAND = "!bet ";
    private final String WIN_COMMAND = "!win ";

    private final PokemonService pokemonService;
    private final UserService userService;

    private final DatabaseModule database;

    private final DeploymentContext deploymentContext;

    public ShinyBetServiceImpl(PokemonService pokemonService,
                               UserService userService,
                               DatabaseModule database,
                               DeploymentContext deploymentContext)
    {
        this.pokemonService = pokemonService;
        this.userService = userService;

        this.database = database;

        this.deploymentContext = deploymentContext;
    }

    public boolean isAcceptRequest(String in)
    {
        return in != null &&
                (in.toLowerCase().startsWith(BET_COMMAND) ||
                        in.toLowerCase().startsWith(WIN_COMMAND));
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
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

    private String roundComplete(String username, String string)
    {

        return String.format("Someday you can win, %s!", username);
    }
}
