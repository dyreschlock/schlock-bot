package com.schlock.bot.services.database.apps.impl;

import com.schlock.bot.entities.apps.pokemon.ShinyGet;
import com.schlock.bot.entities.apps.pokemon.ShinyGetType;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.apps.ShinyGetDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ShinyGetDAOImplTest extends DatabaseTest
{
    private ShinyGet get1;
    private ShinyGet get2;
    private ShinyGet get3;

    private static final Integer TIME1 = 80;
    private static final Integer TIME2 = 60;
    private static final Integer TIME3 = 30;

    private static final Integer CHECK1 = 400;
    private static final Integer CHECK2 = 200;

    @Test
    public void testGetMostRecent()
    {
        ShinyGet get = getDatabase().get(ShinyGetDAO.class).getMostRecent();

        assertEquals(get3, get);
    }

    @Test
    public void testGetCurrentShinyNumber()
    {
        Integer current = getDatabase().get(ShinyGetDAO.class).getCurrentShinyNumber();

        assertEquals(4, current);
    }

    @Test
    public void testGetCurrentAverageShinyTime()
    {
        Double average = getDatabase().get(ShinyGetDAO.class).getCurrentAverageTimeToShiny();

        Integer total = TIME1 + TIME2 + TIME3;

        Double expected = total.doubleValue() /3;

        assertEquals(expected, average);
    }

    @Test
    public void testGetAverageNumberOfRareShinyChecks()
    {
        Double average = getDatabase().get(ShinyGetDAO.class).getCurrentAverageNumberOfRareChecks();

        Integer total = CHECK1 + CHECK2;
        Double expected = total.doubleValue() /2;

        assertEquals(expected, average);
    }

    @BeforeEach
    public void setup() throws Exception
    {
        setupDatabase();
        createTestObjects();
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
        get1.setTimeInMinutes(TIME1);
        get1.setNumOfRareChecks(CHECK1);

        get2 = new ShinyGet();
        get2.setType(ShinyGetType.FAIL);
        get2.setShinyNumber(2);
        get2.setPokemonId("bulbasaur");
        get2.setTimeInMinutes(TIME2);
        get2.setNumOfRareChecks(CHECK2);

        get3 = new ShinyGet();
        get3.setType(ShinyGetType.CATCH);
        get3.setShinyNumber(3);
        get3.setPokemonId("catapie");
        get3.setTimeInMinutes(TIME3);
        get3.setNumOfRareChecks(null);


        getDatabase().save(get1);
        getDatabase().save(get2);
        getDatabase().save(get3);

    }

    private void removeTestObjects()
    {
        getDatabase().delete(get1);
        getDatabase().delete(get2);
        getDatabase().delete(get3);

    }
}