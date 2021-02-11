package com.schlock.bot.services.impl;

import com.schlock.bot.services.BettingService;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.PokemonService;

public class BettingServiceImpl implements BettingService
{
    private final PokemonService pokemonService;

    private final DeploymentContext deploymentContext;

    public BettingServiceImpl(PokemonService pokemonService, DeploymentContext deploymentContext)
    {
        this.pokemonService = pokemonService;

        this.deploymentContext = deploymentContext;
    }

    public boolean isCommand(String in)
    {
        return false;
    }

    public String process(String in)
    {
        return null;
    }
}
