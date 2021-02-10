package com.schlock.bot;

import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.PokemonService;

public abstract class AbstractBot implements Bot
{
    private final PokemonService pokemonService;
    private final DeploymentContext context;


    public AbstractBot(PokemonService pokemonService, DeploymentContext context)
    {
        this.pokemonService = pokemonService;
        this.context = context;
    }

    protected PokemonService getPokemonService()
    {
        return pokemonService;
    }

    protected DeploymentContext getContext()
    {
        return context;
    }
}
