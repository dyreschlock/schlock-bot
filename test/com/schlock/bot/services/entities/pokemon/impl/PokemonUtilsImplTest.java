package com.schlock.bot.services.entities.pokemon.impl;

import com.schlock.bot.AppTestCase;
import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.services.entities.pokemon.PokemonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PokemonUtilsImplTest extends AppTestCase
{
    PokemonUtils pokemonUtils;

    Pokemon test1;

    @Test
    public void testFormat()
    {
        String format = pokemonUtils.formatOutput(test1);

        assertEquals(format, "No. 1 Bulbasaur");
    }

    @Test
    public void testType()
    {
        String format = pokemonUtils.formatOutput(test1, true, false);

        assertEquals(format, "No. 1 Bulbasaur - Grass/Poison");
    }

    @Test
    public void testBasestats()
    {
        String format = pokemonUtils.formatOutput(test1, false, true);

        assertEquals(format, "No. 1 Bulbasaur - Stats: 100");
    }

    @Test
    public void testBothTypeAndStats()
    {
        String format = pokemonUtils.formatOutput(test1, true, true);

        assertEquals(format, "No. 1 Bulbasaur - Grass/Poison - Stats: 100");
    }

    @Test
    public void testIdGenerationId()
    {
        assertTrue(pokemonUtils.isGenerationId("gen1"));
        assertTrue(pokemonUtils.isGenerationId("gen2"));
        assertTrue(pokemonUtils.isGenerationId("gen3"));
        assertTrue(pokemonUtils.isGenerationId("gen4"));
        assertTrue(pokemonUtils.isGenerationId("gen5"));
        assertTrue(pokemonUtils.isGenerationId("gen6"));
        assertTrue(pokemonUtils.isGenerationId("gen7"));
        assertTrue(pokemonUtils.isGenerationId("gen8"));
        assertTrue(pokemonUtils.isGenerationId("gen9"));

        assertTrue(pokemonUtils.isGenerationId("GEN1"));
        assertTrue(pokemonUtils.isGenerationId("Gen1"));

        assertFalse(pokemonUtils.isGenerationId("gen"));
        assertFalse(pokemonUtils.isGenerationId(""));
        assertFalse(pokemonUtils.isGenerationId("9"));
    }

    @Test
    public void testGenerationRange()
    {
        String range1 = pokemonUtils.returnGenerationRange("gen1");
        String range2 = pokemonUtils.returnGenerationRange("gen2");
        String range3 = pokemonUtils.returnGenerationRange("gen3");
        String range4 = pokemonUtils.returnGenerationRange("gen4");
        String range5 = pokemonUtils.returnGenerationRange("gen5");
        String range6 = pokemonUtils.returnGenerationRange("gen6");
        String range7 = pokemonUtils.returnGenerationRange("gen7");
        String range8 = pokemonUtils.returnGenerationRange("gen8");
        String range9 = pokemonUtils.returnGenerationRange("gen9");
        String rangeX = pokemonUtils.returnGenerationRange("");

        String rangeA = pokemonUtils.returnGenerationRange("GEN1");
        String rangeB = pokemonUtils.returnGenerationRange("Gen1");

        assertEquals(range1, "1-151");
        assertEquals(range2, "152-251");
        assertEquals(range3, "252-386");
        assertEquals(range4, "387-493");
        assertEquals(range5, "494-649");
        assertEquals(range6, "650-721");
        assertEquals(range7, "722-809");
        assertEquals(range8, "810-905");
        assertEquals(range9, "906-1025");
        assertNull(rangeX);

        assertEquals(rangeA, "1-151");
        assertEquals(rangeB, "1-151");
    }

    @Test
    public void testGenerationId()
    {
        String gen1 = pokemonUtils.returnGenerationId(test1);

        assertEquals(gen1, "gen1");
    }

    @BeforeEach
    public void setup() throws Exception
    {
        pokemonUtils = new PokemonUtilsImpl(messages());

        test1 = new Pokemon();
        test1.setId("bulbasaur");
        test1.setName("Bulbasaur");
        test1.setNumber(1);
        test1.setType1("Grass");
        test1.setType2("Poison");
        test1.setBasestats(100);
    }
}