package com.schlock.bot.services.apps.pokemon.impl;

import com.schlock.bot.entities.TimeUtils;
import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.entities.apps.pokemon.ShinyGet;
import com.schlock.bot.entities.apps.pokemon.ShinyGetType;
import com.schlock.bot.entities.apps.pokemon.ShinyGetUtils;
import com.schlock.bot.services.apps.pokemon.PokemonService;
import com.schlock.bot.services.database.DatabaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;

import static org.junit.jupiter.api.Assertions.*;

class ShinyInfoServiceImplTest extends DatabaseTest
{
    private final static String USERNAME1 = "username1";

    private final static Integer SHINY_MINUTES = 100;
    private final static Integer SHINY_CHECKS = 1000;

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

    @Test
    public void testAverage()
    {
        String response = impl.process(USERNAME1, "!shinyaverage");

        String time = TimeUtils.formatDoubleMinutesIntoTimeString(SHINY_MINUTES.doubleValue());
        String expected = String.format(ShinyInfoServiceImpl.AVERAGE_MESSAGE, time);

        assertEquals(expected, response);
    }

    @Test
    public void testAverageChecks()
    {
        String response = impl.process(USERNAME1, "!shinychecks");

        String checks = new DecimalFormat("#0.00").format(SHINY_CHECKS);
        String expected = String.format(ShinyInfoServiceImpl.AVERAGE_CHECKS_MESSAGE, checks);

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
        get1.setTimeInMinutes(SHINY_MINUTES);
        get1.setNumOfRareChecks(SHINY_CHECKS);

        getDatabase().save(get1);
    }

    private void removeTestObjects()
    {
        getDatabase().delete(get1);
    }
}