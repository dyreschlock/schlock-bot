package com.schlock.bot.services.bot.apps.pokemon.impl;

import com.schlock.bot.entities.TimeUtils;
import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.entities.apps.pokemon.ShinyGet;
import com.schlock.bot.entities.apps.pokemon.ShinyGetType;
import com.schlock.bot.entities.apps.pokemon.ShinyGetUtils;
import com.schlock.bot.services.bot.apps.pokemon.PokemonService;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.apps.ShinyGetDAO;
import com.schlock.bot.services.database.apps.impl.ShinyGetDAOImpl;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShinyInfoServiceImplTest extends DatabaseTest
{
    private final static String USERNAME1 = "username1";

    private final static Integer SHINY_MINUTES = 100;
    private final static Integer SHINY_CHECKS = 1000;

    private PokemonService pokemonService;
    private ShinyGetDAO shinyGetDAO;

    private ShinyInfoServiceImpl impl;

    private ShinyGet get1;


    @Test
    public void testMostRecent()
    {
        String response = impl.processSingleResponse(USERNAME1, "!recent");

        Pokemon pokemon = pokemonService.getPokemonFromText(get1.getPokemonId());
        String expected = ShinyGetUtils.format(get1, pokemon);

        assertEquals(expected, response);
    }

    @Test
    public void testAverage()
    {
        String response = impl.processSingleResponse(USERNAME1, "!shinyaverage");

        String time = TimeUtils.formatDoubleMinutesIntoTimeString(SHINY_MINUTES.doubleValue());
        String expected = String.format(ShinyInfoServiceImpl.AVERAGE_MESSAGE, time);

        assertEquals(expected, response);
    }

    @Test
    public void testAverageChecks()
    {
        String response = impl.processSingleResponse(USERNAME1, "!shinychecks");

        String checks = new DecimalFormat("#0.00").format(SHINY_CHECKS);
        String expected = String.format(ShinyInfoServiceImpl.AVERAGE_CHECKS_MESSAGE, checks);

        assertEquals(expected, response);
    }


    @Override
    protected void before() throws Exception
    {
        shinyGetDAO = new ShinyGetDAOImpl(session);

        pokemonService = new PokemonServiceImpl(config);

        impl = new ShinyInfoServiceImpl(pokemonService, shinyGetDAO, config);

        createTestObjects();
    }

    @Override
    protected void after() throws Exception
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

        shinyGetDAO.save(get1);
    }

    private void removeTestObjects()
    {
        shinyGetDAO.delete(get1);
    }
}