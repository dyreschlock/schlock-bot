package com.schlock.bot.services.commands.pokemon.shiny.impl;

import com.schlock.bot.entities.pokemon.ShinyDexEntry;
import com.schlock.bot.services.entities.pokemon.impl.PokemonManagementImpl;
import com.schlock.bot.services.entities.pokemon.impl.PokemonUtilsImpl;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.PokemonUtils;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.pokemon.ShinyDexEntryDAO;
import com.schlock.bot.services.database.pokemon.impl.ShinyDexEntryDAOImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShinyDexServiceImplTest extends DatabaseTest
{
    private static final String USERNAME1 = "username1";

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
        PokemonUtils pokemonUtils = new PokemonUtilsImpl(messages());
        PokemonManagement pokemonManagement = new PokemonManagementImpl(pokemonUtils, config());

        impl = new ShinyDexServiceImpl(pokemonManagement, database, messages());

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

        database.save(entry1, entry2);
    }

    private void removeTestObjects()
    {
        database.delete(entry1, entry2);
    }
}