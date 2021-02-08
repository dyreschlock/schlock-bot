package com.schlock.bot.services.impl;

import com.schlock.bot.entities.Pokemon;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.PokemonService;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PokemonServiceImpl implements PokemonService
{
    private static final String LIST_OF_POKEMON_FILE = "pokemon.html";

    private final DeploymentContext context;

    private Map<Integer, Pokemon> pokemonByNumber;
    private Map<String, Pokemon> pokemonByName;

    public PokemonServiceImpl(DeploymentContext context)
    {
        this.context = context;
    }

    public String process(String in)
    {
        initialize();


        return null;
    }



    private static final String DIV_CONTENT_ID = "content";

    private void initialize()
    {
        if (pokemonByName == null || pokemonByNumber == null)
        {
            pokemonByName = new HashMap<>();
            pokemonByNumber = new HashMap<>();

            loadPokemon();
        }
    }

    private void loadPokemon()
    {
        String fileLocation = context.getDataDirectory() + LIST_OF_POKEMON_FILE;


    }
}
