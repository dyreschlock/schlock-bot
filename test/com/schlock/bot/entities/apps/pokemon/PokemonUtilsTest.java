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

    @Test
    public void testIdGenerationId()
    {
        assertTrue(PokemonUtils.isGenerationId("gen1"));
        assertTrue(PokemonUtils.isGenerationId("gen2"));
        assertTrue(PokemonUtils.isGenerationId("gen3"));
        assertTrue(PokemonUtils.isGenerationId("gen4"));
        assertTrue(PokemonUtils.isGenerationId("gen5"));
        assertTrue(PokemonUtils.isGenerationId("gen6"));
        assertTrue(PokemonUtils.isGenerationId("gen7"));
        assertTrue(PokemonUtils.isGenerationId("gen8"));

        assertTrue(PokemonUtils.isGenerationId("GEN1"));
        assertTrue(PokemonUtils.isGenerationId("Gen1"));

        assertFalse(PokemonUtils.isGenerationId("gen"));
        assertFalse(PokemonUtils.isGenerationId("gen9"));
        assertFalse(PokemonUtils.isGenerationId(""));
        assertFalse(PokemonUtils.isGenerationId("9"));
    }

    @Test
    public void testGenerationRange()
    {
        String range1 = PokemonUtils.returnGenerationRange("gen1");
        String range2 = PokemonUtils.returnGenerationRange("gen2");
        String range3 = PokemonUtils.returnGenerationRange("gen3");
        String range4 = PokemonUtils.returnGenerationRange("gen4");
        String range5 = PokemonUtils.returnGenerationRange("gen5");
        String range6 = PokemonUtils.returnGenerationRange("gen6");
        String range7 = PokemonUtils.returnGenerationRange("gen7");
        String range8 = PokemonUtils.returnGenerationRange("gen8");
        String rangeX = PokemonUtils.returnGenerationRange("");

        String rangeA = PokemonUtils.returnGenerationRange("GEN1");
        String rangeB = PokemonUtils.returnGenerationRange("Gen1");

        assertEquals(range1, "1-151");
        assertEquals(range2, "152-250");
        assertEquals(range3, "251-386");
        assertEquals(range4, "387-493");
        assertEquals(range5, "494-649");
        assertEquals(range6, "650-721");
        assertEquals(range7, "722-809");
        assertEquals(range8, "810-898");
        assertNull(rangeX);

        assertEquals(rangeA, "1-151");
        assertEquals(rangeB, "1-151");
    }

    @Test
    public void testGenerationId()
    {
        String gen1 = PokemonUtils.returnGenerationId(test1);

        assertEquals(gen1, "gen1");
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