package com.schlock.bot.services.impl;

import com.schlock.bot.services.DeploymentContext;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
            assertEquals(test, data[1], String.format("Incoming data [%s] // Outgoing data [%s]", data[0], test));
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
            assertEquals(test, data[1], String.format("Incoming data [%s] // Outgoing data [%s]", data[0], test));
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