package com.schlock.bot.services.commands.pokemon.shiny.impl;

import com.schlock.bot.entities.pokemon.ShinyDexEntryLetsGo;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.PokemonUtils;
import com.schlock.bot.services.entities.pokemon.impl.PokemonManagementImpl;
import com.schlock.bot.services.entities.pokemon.impl.PokemonUtilsImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class ShinyDexServiceImplTest extends DatabaseTest
{
    private static final String USERNAME1 = "username1";

    private ShinyDexServiceImpl impl;


    private ShinyDexEntryLetsGo entry1;
    private ShinyDexEntryLetsGo entry2;

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

        impl = new ShinyDexServiceImpl(pokemonManagement, database(), messages());

        createTestObjects();
    }

    @Override
    protected void after() throws Exception
    {
        removeTestObjects();
    }

    private void createTestObjects()
    {
        entry1 = new ShinyDexEntryLetsGo();
        entry1.setNumberCode("001");
        entry1.setPokemonId("bulbasaur");

        entry2 = new ShinyDexEntryLetsGo();
        entry2.setNumberCode("002");
        entry2.setPokemonId("ivysaur");

        database().save(entry1, entry2);
    }

    private void removeTestObjects()
    {
        database().delete(entry1, entry2);
    }
}