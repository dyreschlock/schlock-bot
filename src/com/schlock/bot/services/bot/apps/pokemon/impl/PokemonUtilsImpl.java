package com.schlock.bot.services.bot.apps.pokemon.impl;

import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.services.bot.apps.pokemon.PokemonUtils;
import org.apache.tapestry5.ioc.Messages;

import java.util.Arrays;
import java.util.List;

public class PokemonUtilsImpl implements PokemonUtils
{
    private final Messages messages;

    public PokemonUtilsImpl(Messages messages)
    {
        this.messages = messages;
    }

    private static final String POKEMON_DETAILS_KEY = "pokemon-details";
    private static final String POKEMON_HINT_1_KEY = "pokemon-hint-1";

    public String formatOutput(Pokemon pokemon)
    {
        return formatOutput(pokemon, false, false);
    }

    public String formatOutput(Pokemon pokemon, boolean type, boolean basestats)
    {
        String p = "";

        String number = Integer.toString(pokemon.getNumber());
        String name = pokemon.getName();

        p = messages.format(POKEMON_DETAILS_KEY, number, name);

        if (type)
        {
            String t = " - " + pokemon.getType1();
            if (pokemon.getType2() != null)
            {
                t = t + "/" + pokemon.getType2();
            }

            p = p + t;
        }
        if (basestats)
        {
            String b = " - Stats: " + pokemon.getBasestats();

            p = p + b;
        }
        return p;
    }

    private static final String GEN1 = "gen1";
    private static final Integer GEN1_START = 1;
    private static final Integer GEN1_END = 151;

    private static final String GEN2 = "gen2";
    private static final Integer GEN2_START = 152;
    private static final Integer GEN2_END = 250;

    private static final String GEN3 = "gen3";
    private static final Integer GEN3_START = 251;
    private static final Integer GEN3_END = 386;

    private static final String GEN4 = "gen4";
    private static final Integer GEN4_START = 387;
    private static final Integer GEN4_END = 493;

    private static final String GEN5 = "gen5";
    private static final Integer GEN5_START = 494;
    private static final Integer GEN5_END = 649;

    private static final String GEN6 = "gen6";
    private static final Integer GEN6_START = 650;
    private static final Integer GEN6_END = 721;

    private static final String GEN7 = "gen7";
    private static final Integer GEN7_START = 722;
    private static final Integer GEN7_END = 809;

    private static final String GEN8 = "gen8";
    private static final Integer GEN8_START = 810;
    private static final Integer GEN8_END = 898;

    private static final List<Object[]> GENERATIONS = Arrays.asList(
            new Object[]{GEN1, GEN1_START, GEN1_END},
            new Object[]{GEN2, GEN2_START, GEN2_END},
            new Object[]{GEN3, GEN3_START, GEN3_END},
            new Object[]{GEN4, GEN4_START, GEN4_END},
            new Object[]{GEN5, GEN5_START, GEN5_END},
            new Object[]{GEN6, GEN6_START, GEN6_END},
            new Object[]{GEN7, GEN7_START, GEN7_END},
            new Object[]{GEN8, GEN8_START, GEN8_END}
    );


    public boolean isGenerationId(String input)
    {
        String gen = input.trim();
        for (Object[] generation : GENERATIONS)
        {
            String g = (String) generation[0];
            if (g.equalsIgnoreCase(gen))
            {
                return true;
            }
        }
        return false;
    }

    public String returnGenerationRange(String gen)
    {
        for (Object[] generation : GENERATIONS)
        {
            String g = (String) generation[0];
            if (g.equalsIgnoreCase(gen))
            {
                Integer start = (Integer) generation[1];
                Integer end = (Integer) generation[2];

                return start.toString() + "-" + end.toString();
            }
        }
        return null;
    }

    public String returnGenerationId(Pokemon pokemon)
    {
        Integer id = pokemon.getNumber();
        for (Object[] generation : GENERATIONS)
        {
            Integer start = (Integer) generation[1];
            Integer end = (Integer) generation[2];

            if (start <= id && id <= end)
            {
                return (String) generation[0];
            }
        }
        return null;
    }

    public Integer returnFirstPokemonNumberInGeneration(String input)
    {
        String gen = input.trim();
        for (Object[] generation : GENERATIONS)
        {
            String g = (String) generation[0];
            if (g.equalsIgnoreCase(gen))
            {
                return (Integer) generation[1];
            }
        }
        return null;
    }

    public Integer returnLastPokemonNumberInGeneration(String input)
    {
        String gen = input.trim();
        for (Object[] generation : GENERATIONS)
        {
            String g = (String) generation[0];
            if (g.equalsIgnoreCase(gen))
            {
                return (Integer) generation[2];
            }
        }
        return null;
    }

    public String formatHint1(Pokemon pokemon)
    {
        String id = pokemon.getId();

        String firstLetter = id.substring(0, 1).toUpperCase();
        Integer length = id.length();
        String type1 = pokemon.getType1();

        String gen = returnGenerationId(pokemon);

        return messages.format(POKEMON_HINT_1_KEY, firstLetter, length.toString(), type1, gen);
    }
}
