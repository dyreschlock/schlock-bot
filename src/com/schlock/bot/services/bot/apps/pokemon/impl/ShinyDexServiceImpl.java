package com.schlock.bot.services.bot.apps.pokemon.impl;

import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.entities.apps.pokemon.ShinyDexEntry;
import com.schlock.bot.services.bot.apps.AbstractListenerService;
import com.schlock.bot.services.bot.apps.ListenerResponse;
import com.schlock.bot.services.bot.apps.pokemon.PokemonService;
import com.schlock.bot.services.bot.apps.pokemon.ShinyDexService;
import com.schlock.bot.services.database.apps.ShinyDexEntryDAO;
import org.apache.tapestry5.ioc.Messages;

import java.util.ArrayList;
import java.util.List;

public class ShinyDexServiceImpl extends AbstractListenerService implements ShinyDexService
{
    protected static final String CURRENT_SHINY_DEX_KEY = "current-shiny-dex";

    private static final String SHINY_DEX_COMMAND = "!shinydex";

    private final PokemonService pokemonService;

    private final ShinyDexEntryDAO dexEntryDAO;

    public ShinyDexServiceImpl(PokemonService pokemonService,
                               ShinyDexEntryDAO dexEntryDAO,
                               Messages messages)
    {
        super(messages);

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

    public boolean isAcceptRequest(String username, String message)
    {
        return message != null &&
                (message.toLowerCase().startsWith(SHINY_DEX_COMMAND));
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    public ListenerResponse process(String username, String in)
    {
        String commandText = in.toLowerCase().trim();
        if (commandText.startsWith(SHINY_DEX_COMMAND))
        {
            List<ShinyDexEntry> entries = dexEntryDAO.getAll();

            Integer count = entries.size();

            return singleResponseFormat(CURRENT_SHINY_DEX_KEY, count.toString());
        }
        return nullResponse();
    }
}
