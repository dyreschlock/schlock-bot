package com.schlock.bot;

import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.PokemonService;

public class TwitchBot extends AbstractBot
{
    public TwitchBot(PokemonService pokemonService, DeploymentContext context)
    {
        super(pokemonService, context);
    }

    public void startup()
    {


        listenForPing();
        listenForCommands();
        listenForPokemon();
    }

    public void listenForPing()
    {

    }

    public void listenForCommands()
    {

    }

    public void listenForPokemon()
    {

    }
}
