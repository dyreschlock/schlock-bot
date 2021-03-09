package com.schlock.bot.services.bot.apps.pokemon.impl;

import com.schlock.bot.entities.TimeUtils;
import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.entities.apps.pokemon.ShinyGet;
import com.schlock.bot.entities.apps.pokemon.ShinyGetType;
import com.schlock.bot.services.bot.apps.bet.ShinyGetFormatter;
import com.schlock.bot.services.bot.apps.bet.impl.ShinyGetFormatterImpl;
import com.schlock.bot.services.bot.apps.pokemon.PokemonService;
import com.schlock.bot.services.bot.apps.pokemon.PokemonUtils;
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
    private ShinyGetFormatter shinyFormatter;
    private ShinyGetDAO shinyGetDAO;

    private ShinyInfoServiceImpl impl;

    private ShinyGet get1;


    @Test
    public void testMostRecent()
    {
        String response = impl.process(USERNAME1, "!recent").getFirstMessage();

        String expected = shinyFormatter.formatMostRecent(get1);

        assertEquals(expected, response);
    }

    @Test
    public void testAverage()
    {
        String response = impl.process(USERNAME1, "!shinyaverage").getFirstMessage();

        String time = TimeUtils.formatDoubleMinutesIntoTimeString(SHINY_MINUTES.doubleValue());
        String expected = messages().format(ShinyInfoServiceImpl.AVERAGE_TIME_KEY, time);

        assertEquals(expected, response);
    }

    @Test
    public void testAverageChecks()
    {
        String response = impl.process(USERNAME1, "!shinychecks").getFirstMessage();

        String checks = new DecimalFormat("#0.00").format(SHINY_CHECKS);
        String expected = messages().format(ShinyInfoServiceImpl.AVERAGE_CHECKS_KEY, checks);

        assertEquals(expected, response);
    }


    @Override
    protected void before() throws Exception
    {
        shinyGetDAO = new ShinyGetDAOImpl(session)
        {
            public void commit()
            {
            }
        };

        PokemonUtils pokemonUtils = new PokemonUtilsImpl(messages());

        pokemonService = new PokemonServiceImpl(pokemonUtils, messages(), config());

        shinyFormatter = new ShinyGetFormatterImpl(pokemonService, messages());

        impl = new ShinyInfoServiceImpl(pokemonService, shinyFormatter, shinyGetDAO, messages(), config());

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