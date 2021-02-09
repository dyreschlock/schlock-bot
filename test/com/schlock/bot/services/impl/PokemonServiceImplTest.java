package com.schlock.bot.services.impl;

import com.schlock.bot.services.DeploymentContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class PokemonServiceImplTest
{
    private PokemonServiceImpl impl;

    @Test
    public void testPokemonNumbers()
    {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"!pokemon 001", "No. 1 Bulbasaur"});
        testData.add(new String[]{"!pokemon 150", "No. 150 Mewtwo"});
        testData.add(new String[]{"!pokemon 0000000001", "No. 1 Bulbasaur"});
        testData.add(new String[]{"!pokemon 0", PokemonServiceImpl.POKEMON_RETURN_NULL});
        testData.add(new String[]{"!pokemon 900", PokemonServiceImpl.POKEMON_RETURN_NULL});

        for (String[] data : testData)
        {
            String test = impl.process(data[0]);
            assertEquals(test, data[1]);
        }
    }

    @Test
    public void testPokemonNames()
    {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"!pokemon bulbasaur", "No. 1 Bulbasaur"});
        testData.add(new String[]{"!pokemon mewtwo", "No. 150 Mewtwo"});
        testData.add(new String[]{"!pokemon xxx", PokemonServiceImpl.POKEMON_RETURN_NULL});
        testData.add(new String[]{"!pokemon nidoran f", "No. 29 Nidoran♀"});
        testData.add(new String[]{"!pokemon nidoranf", "No. 29 Nidoran♀"});
        testData.add(new String[]{"!pokemon nidoran m", "No. 32 Nidoran♂"});
        testData.add(new String[]{"!pokemon nidoranm", "No. 32 Nidoran♂"});

        for (String[] data : testData)
        {
            String test = impl.process(data[0]);
            assertEquals(test, data[1]);
        }
    }

    @Test
    public void testPokemonRangeSyntaxOnePokemon()
    {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"!pokemon bulbasaur-bulbasaur", "No. 1 Bulbasaur"});
        testData.add(new String[]{"!pokemon 1-1", "No. 1 Bulbasaur"});
        testData.add(new String[]{"!pokemon 1-bulbasaur", "No. 1 Bulbasaur"});
        testData.add(new String[]{"!pokemon bulbasaur-1", "No. 1 Bulbasaur"});

        for (String[] data : testData)
        {
            String text = impl.process(data[0]);
            assertEquals(text, data[1]);
        }
    }

    @Test
    public void testPokemonRangeTwoPokemon()
    {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"!pokemon 1-2", "No. 1 Bulbasaur", "No. 2 Ivysaur"});
        testData.add(new String[]{"!pokemon 2-1", "No. 1 Bulbasaur", "No. 2 Ivysaur"});

        testData.add(new String[]{"!pokemon -3", "No. 1 Bulbasaur", "No. 2 Ivysaur", "No. 3 Venusaur"});
        testData.add(new String[]{"!pokemon -2", "No. 1 Bulbasaur", "No. 2 Ivysaur"});
        testData.add(new String[]{"!pokemon -1", "No. 1 Bulbasaur"});
        testData.add(new String[]{"!pokemon -0", PokemonServiceImpl.POKEMON_RETURN_NULL});

        testData.add(new String[]{"!pokemon 900-", PokemonServiceImpl.POKEMON_RETURN_NULL});
        testData.add(new String[]{"!pokemon 898-", "No. 898 Calyrex"});
        testData.add(new String[]{"!pokemon 897-", "No. 898 Calyrex", "No. 897 Spectrier"});
        testData.add(new String[]{"!pokemon 896-", "No. 898 Calyrex", "No. 897 Spectrier", "No. 896 Glastrier"});

        for (String[] data : testData)
        {
            Set<String> results = new HashSet<>();

            int expected_results = data.length - 1;

            int test_ten_times_for_each = expected_results * 10;
            for (int i = 0; i < test_ten_times_for_each; i++)
            {
                String text = impl.process(data[0]);
                results.add(text);
            }

            assertEquals(expected_results, results.size(), "Too many results.");

            for (int i = 1; i < data.length; i++)
            {
                String result = data[i];
                assertTrue(results.contains(result), "Doesn't contain " + result);
            }
        }
    }


    @BeforeEach
    public void setup() throws Exception
    {
        DeploymentContext context = new DeploymentContextImpl(null)
        {
            @Override
            public String getDiscordToken()
            {
                return null;
            }
        };

        impl = new PokemonServiceImpl(context);
    }
}