package com.schlock.bot.services.bet.impl;

import com.schlock.bot.entities.bet.BettingUser;
import com.schlock.bot.services.DatabaseModule;
import com.schlock.bot.services.bet.BettingService;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.database.bet.BetDAO;
import com.schlock.bot.services.database.bet.BettingUserDAO;
import com.schlock.bot.services.pokemon.PokemonService;

public class BettingServiceImpl implements BettingService
{
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

    private String placeBet(String username, String in)
    {
        BettingUser user = database.get(BettingUserDAO.class).getByUsername(username);



        return "";
    }

    private String returnBalance(String username, String in)
    {

        return "";
    }

    private String roundComplete(String username, String string)
    {

        return "";
    }
}
