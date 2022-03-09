package com.schlock.bot.services.commands.pokemon.shiny.impl;

import com.schlock.bot.entities.pokemon.*;
import com.schlock.bot.services.commands.AbstractListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.pokemon.shiny.ShinyDexService;
import com.schlock.bot.services.database.adhoc.DatabaseManager;
import com.schlock.bot.services.database.pokemon.ShinyDexEntryDAO;
import com.schlock.bot.services.database.pokemon.ShinyDexEntryGoDAO;
import com.schlock.bot.services.database.pokemon.ShinyDexEntryHisuiDAO;
import com.schlock.bot.services.database.pokemon.ShinyHisuiGetDAO;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import org.apache.tapestry5.ioc.Messages;

import java.util.ArrayList;
import java.util.List;

public class ShinyDexServiceImpl extends AbstractListenerService implements ShinyDexService
{
    protected static final String CURRENT_SHINY_DEX_KEY = "current-shiny-dex";

    private static final String SHINY_DEX_COMMAND = "!shinydex";

    private final PokemonManagement pokemonManagement;

    private DatabaseManager database;

    public ShinyDexServiceImpl(PokemonManagement pokemonManagement,
                               DatabaseManager database,
                               Messages messages)
    {
        super(messages);

        this.pokemonManagement = pokemonManagement;

        this.database = database;
    }

    public List<Pokemon> getShinyDexEntries()
    {
        List<ShinyDexEntry> entries = database.get(ShinyDexEntryDAO.class).getAll();

        List<Pokemon> pokemon = new ArrayList<>();
        for (ShinyDexEntry entry : entries)
        {
            Pokemon p = pokemonManagement.getPokemonFromText(entry.getPokemon());
            if (p != null)
            {
                pokemon.add(p);
            }
        }
        return pokemon;
    }

    public List<Pokemon> getShinyDexHisuiEntries()
    {
        List<ShinyDexEntryHisui> entries = database.get(ShinyDexEntryHisuiDAO.class).getAll();

        List<Pokemon> pokemon = new ArrayList<>();
        for (ShinyDexEntryHisui entry : entries)
        {
            Pokemon p = pokemonManagement.getPokemonFromText(entry.getPokemonId());
            if (p != null)
            {
                pokemon.add(p);
            }
        }
        return pokemon;
    }

    public List<Pokemon> getShinyDexHisuiGets()
    {
        List<ShinyHisuiGet> entries = database.get(ShinyHisuiGetDAO.class).getAll();

        List<Pokemon> pokemon = new ArrayList<>();
        for (ShinyHisuiGet entry : entries)
        {
            Pokemon p = pokemonManagement.getPokemonFromText(entry.getPokemonId());
            if (p != null)
            {
                pokemon.add(p);
            }
        }
        return pokemon;
    }

    public List<ShinyDexEntryGo> getPokemonGoEntries()
    {
        List<ShinyDexEntryGo> entries = database.get(ShinyDexEntryGoDAO.class).getAll();
        return entries;
    }


    public boolean isAcceptRequest(String username, String message)
    {
        return message != null &&
                (message.toLowerCase().startsWith(SHINY_DEX_COMMAND));
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    protected ListenerResponse processRequest(String username, String in)
    {
        String commandText = in.toLowerCase().trim();
        if (commandText.startsWith(SHINY_DEX_COMMAND))
        {
            List<ShinyDexEntry> entries = database.get(ShinyDexEntryDAO.class).getAll();

            Integer count = entries.size();

            return formatSingleResponse(CURRENT_SHINY_DEX_KEY, count.toString());
        }
        return nullResponse();
    }
}
