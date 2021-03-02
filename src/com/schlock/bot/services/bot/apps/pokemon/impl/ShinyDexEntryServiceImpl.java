package com.schlock.bot.services.bot.apps.pokemon.impl;

import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.entities.apps.pokemon.ShinyDexEntry;
import com.schlock.bot.services.bot.apps.pokemon.ShinyDexEntryService;
import com.schlock.bot.services.bot.apps.pokemon.PokemonService;
import com.schlock.bot.services.database.apps.ShinyDexEntryDAO;

import java.util.ArrayList;
import java.util.List;

public class ShinyDexEntryServiceImpl implements ShinyDexEntryService
{
    private final PokemonService pokemonService;

    private final ShinyDexEntryDAO dexEntryDAO;

    public ShinyDexEntryServiceImpl(PokemonService pokemonService,
                                    ShinyDexEntryDAO dexEntryDAO)
    {
        this.pokemonService = pokemonService;

        this.dexEntryDAO = dexEntryDAO;
    }

    @Override
    public List<Pokemon> getShinyDexEntries()
    {
        List<ShinyDexEntry> entries = dexEntryDAO.getAll();

        List<Pokemon> pokemon = new ArrayList<>();

        for (ShinyDexEntry entry : entries)
        {
            Pokemon p = pokemonService.getPokemonFromText(entry.getPokemon());
            if (p != null)
            {
                pokemon.add(p);
            }
        }
        return pokemon;
    }
}
