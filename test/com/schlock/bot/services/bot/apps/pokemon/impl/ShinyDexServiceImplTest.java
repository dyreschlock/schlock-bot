package com.schlock.bot.services.bot.apps.pokemon.impl;

import com.schlock.bot.entities.apps.pokemon.ShinyDexEntry;
import com.schlock.bot.services.bot.apps.ListenerResponse;
import com.schlock.bot.services.bot.apps.pokemon.PokemonService;
import com.schlock.bot.services.bot.apps.pokemon.PokemonUtils;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.apps.ShinyDexEntryDAO;
import com.schlock.bot.services.database.apps.impl.ShinyDexEntryDAOImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShinyDexServiceImplTest extends DatabaseTest
{
    private static final String USERNAME1 = "username1";

    private ShinyDexEntryDAO dexEntryDAO;

    private ShinyDexServiceImpl impl;


    private ShinyDexEntry entry1;
    private ShinyDexEntry entry2;

    @Test
    public void testShinyDex()
    {
        ListenerResponse resp = impl.process(USERNAME1, "!shinydex");
        String response = resp.getFirstMessage();
        String expected = messages().format(ShinyDexServiceImpl.CURRENT_SHINY_DEX_KEY, 2);

        assertFalse(resp.isRelayAll());
        assertEquals(expected, response);
    }


    @Override
    protected void before() throws Exception
    {
        dexEntryDAO = new ShinyDexEntryDAOImpl(session)
        {
            public void commit()
            {
            }
        };

        PokemonUtils pokemonUtils = new PokemonUtilsImpl(messages());
        PokemonService pokemonService = new PokemonServiceImpl(pokemonUtils, messages(), config());

        impl = new ShinyDexServiceImpl(pokemonService, dexEntryDAO, messages());

        createTestObjects();
    }

    @Override
    protected void after() throws Exception
    {
        removeTestObjects();
    }

    private void createTestObjects()
    {
        entry1 = new ShinyDexEntry();
        entry1.setPokemon("bulbasaur");

        entry2 = new ShinyDexEntry();
        entry2.setPokemon("ivysaur");

        dexEntryDAO.save(entry1, entry2);
    }

    private void removeTestObjects()
    {
        dexEntryDAO.delete(entry1, entry2);
    }
}