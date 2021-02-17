package com.schlock.bot.services.apps.pokemon.impl;

import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.entities.apps.pokemon.ShinyGet;
import com.schlock.bot.entities.apps.pokemon.ShinyGetType;
import com.schlock.bot.entities.apps.pokemon.ShinyGetUtils;
import com.schlock.bot.services.apps.pokemon.PokemonService;
import com.schlock.bot.services.database.DatabaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShinyInfoServiceImplTest extends DatabaseTest
{
    private final static String USERNAME1 = "username1";

    private PokemonService pokemonService;

    private ShinyInfoServiceImpl impl;

    private ShinyGet get1;


    @Test
    public void testMostRecent()
    {
        String response = impl.process(USERNAME1, "!recent");

        Pokemon pokemon = pokemonService.getPokemonFromText(get1.getPokemonId());
        String expected = ShinyGetUtils.format(get1, pokemon);

        assertEquals(expected, response);
    }


    @BeforeEach
    public void setup() throws Exception
    {
        setupDatabase();
        createTestObjects();

        pokemonService = new PokemonServiceImpl(getDeploymentContext());

        impl = new ShinyInfoServiceImpl(pokemonService, getDatabase(), getDeploymentContext());
    }

    @AfterEach
    public void tearDown()
    {
        removeTestObjects();
    }

    private void createTestObjects()
    {
        get1 = new ShinyGet();
        get1.setType(ShinyGetType.CATCH);
        get1.setShinyNumber(1);
        get1.setPokemonId("bulbasaur");
        get1.setTimeInMinutes(100);
        get1.setNumOfRareChecks(1000);

        getDatabase().save(get1);
    }

    private void removeTestObjects()
    {
        getDatabase().delete(get1);
    }
}