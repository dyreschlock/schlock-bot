package com.schlock.bot.entities.apps.pokemon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PokemonUtilsTest
{
    Pokemon test1;

    @Test
    public void testFormat()
    {
        String format = PokemonUtils.formatOutput(test1);

        assertEquals(format, "No. 1 Bulbasaur");
    }

    @Test
    public void testType()
    {
        String format = PokemonUtils.formatOutput(test1, true, false);

        assertEquals(format, "No. 1 Bulbasaur - Grass/Poison");
    }

    @Test
    public void testBasestats()
    {
        String format = PokemonUtils.formatOutput(test1, false, true);

        assertEquals(format, "No. 1 Bulbasaur - Stats: 100");
    }

    @Test
    public void testBothTypeAndStats()
    {
        String format = PokemonUtils.formatOutput(test1, true, true);

        assertEquals(format, "No. 1 Bulbasaur - Grass/Poison - Stats: 100");
    }

    @BeforeEach
    public void setup() throws Exception
    {
        test1 = new Pokemon();
        test1.setId("bulbasaur");
        test1.setName("Bulbasaur");
        test1.setNumber(1);
        test1.setType1("Grass");
        test1.setType2("Poison");
        test1.setBasestats(100);
    }
}