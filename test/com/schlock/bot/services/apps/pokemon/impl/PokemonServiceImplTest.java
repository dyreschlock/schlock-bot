package com.schlock.bot.services.apps.pokemon.impl;

import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.impl.DeploymentContextImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PokemonServiceImplTest
{
    private PokemonServiceImpl impl;

    @Test
    public void testCommandIdentifiers()
    {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"!pokémon 1", "No. 1 Bulbasaur"});
        testData.add(new String[]{"!pokemon 1", "No. 1 Bulbasaur"});

        assertInputEqualsOutput(testData);
    }

    @Test
    public void testPokemonNumbers()
    {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"!pokemon 001", "No. 1 Bulbasaur"});
        testData.add(new String[]{"!pokemon 150", "No. 150 Mewtwo"});
        testData.add(new String[]{"!pokemon 0000000001", "No. 1 Bulbasaur"});
        testData.add(new String[]{"!pokemon 0", "No. 1 Bulbasaur"});
        testData.add(new String[]{"!pokemon 900", "No. 898 Calyrex"});

        assertInputEqualsOutput(testData);
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

        assertInputEqualsOutput(testData);
    }

    @Test
    public void testPokemonRangeSyntaxOnePokemon()
    {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"!pokemon bulbasaur-bulbasaur", "No. 1 Bulbasaur"});
        testData.add(new String[]{"!pokemon 1-1", "No. 1 Bulbasaur"});
        testData.add(new String[]{"!pokemon 1-bulbasaur", "No. 1 Bulbasaur"});
        testData.add(new String[]{"!pokemon bulbasaur-1", "No. 1 Bulbasaur"});

        assertInputEqualsOutput(testData);
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
        testData.add(new String[]{"!pokemon -0", "No. 1 Bulbasaur"});

        testData.add(new String[]{"!pokemon 900-", "No. 898 Calyrex"});
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
                String text = impl.process("", data[0]);
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


    @Test
    public void testType()
    {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"!pokemon type:null type", "No. 772 Type: Null - Normal"});
        testData.add(new String[]{"!pokemon 1 type", "No. 1 Bulbasaur - Grass/Poison"});
        testData.add(new String[]{"!pokemon 4 type", "No. 4 Charmander - Fire"});
        testData.add(new String[]{"!pokemon 20 type", "No. 20 Raticate - Normal"});

        assertInputEqualsOutput(testData);
    }

    @Test
    public void testStats()
    {
        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"!pokemon 1 basestats", "No. 1 Bulbasaur - Stats: 318"});
        testData.add(new String[]{"!pokemon 2 basestats", "No. 2 Ivysaur - Stats: 405"});
        testData.add(new String[]{"!pokemon 3 basestats", "No. 3 Venusaur - Stats: 525"});

        assertInputEqualsOutput(testData);
    }

    @Test
    public void testTypeStats()
    {
        final String RESULT = "No. 1 Bulbasaur - Grass/Poison - Stats: 318";

        List<String[]> testData = new ArrayList<>();
        testData.add(new String[]{"!pokemon 1 type basestats", RESULT});
        testData.add(new String[]{"!pokemon 1 basestats type", RESULT});
        testData.add(new String[]{"!pokemon 1       type       basestats", RESULT});

        assertInputEqualsOutput(testData);
    }

    @Test
    public void testGenerationSuccess()
    {
        List<Object[]> testData = new ArrayList<>();
        testData.add(new Object[]{"!pokemon gen1", 1, 151});
        testData.add(new Object[]{"!pokemon gen2", 152, 250});
        testData.add(new Object[]{"!pokemon gen3", 251, 386});
        testData.add(new Object[]{"!pokemon gen4", 387, 493});
        testData.add(new Object[]{"!pokemon gen5", 494, 649});
        testData.add(new Object[]{"!pokemon gen6", 650, 721});
        testData.add(new Object[]{"!pokemon gen7", 722, 809});
        testData.add(new Object[]{"!pokemon gen8", 810, 898});

        for (Object[] d : testData)
        {
            String response = impl.process("", (String) d[0]);
            String number = response.split(" ")[1];

            Integer pokemonId = Integer.parseInt(number);

            Integer start = (Integer) d[1];
            Integer end = (Integer) d[2];

            assertTrue(start <= pokemonId && pokemonId <= end);
        }
    }

    @Test
    public void testGenerationRangeSuccess()
    {
        List<Object[]> testData = new ArrayList<>();
        testData.add(new Object[]{"!pokemon gen1-", 1, 898});
        testData.add(new Object[]{"!pokemon -gen2", 1, 250});
        testData.add(new Object[]{"!pokemon gen2-gen3", 152, 386});
        testData.add(new Object[]{"!pokemon -gen1", 1, 151});
        testData.add(new Object[]{"!pokemon gen8-", 810, 898});
        testData.add(new Object[]{"!pokemon gen1-gen8", 1, 898});

        for (Object[] d : testData)
        {
            String response = impl.process("", (String) d[0]);
            String number = response.split(" ")[1];

            Integer pokemonId = Integer.parseInt(number);

            Integer start = (Integer) d[1];
            Integer end = (Integer) d[2];

            assertTrue(start <= pokemonId && pokemonId <= end);
        }
    }

    @Test
    public void testGenerationFail()
    {
        List<String> testData = new ArrayList<>();
        testData.add("!pokemon g1");
        testData.add("!pokemon generation1");
        testData.add("!pokemon gen9");
        testData.add("!pokemon genX");

        for (String d : testData)
        {
            String response = impl.process("", d);
            assertEquals(response, PokemonServiceImpl.POKEMON_RETURN_NULL);
        }
    }


    private void assertInputEqualsOutput(List<String[]> data)
    {
        for (String[] d : data)
        {
            String test = impl.process("", d[0]);
            assertEquals(test, d[1]);
        }
    }

    @BeforeEach
    public void setup() throws Exception
    {
        DeploymentContext context = new DeploymentContextImpl(DeploymentContext.TEST)
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