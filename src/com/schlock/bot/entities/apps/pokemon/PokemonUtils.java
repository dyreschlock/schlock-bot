package com.schlock.bot.entities.apps.pokemon;

import java.util.Arrays;
import java.util.List;

public class PokemonUtils
{
    private static final String POKEMON_RETURN_FORMAT = "No. %s %s";

    public static String formatOutput(Pokemon pokemon)
    {
        return formatOutput(pokemon, false, false);
    }

    public static String formatOutput(Pokemon pokemon, boolean type, boolean basestats)
    {
        String p = "";

        String number = Integer.toString(pokemon.getNumber());
        String name = pokemon.getName();

        p = String.format(POKEMON_RETURN_FORMAT, number, name);

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

    private static final List<Object[]> generations = Arrays.asList(
            new Object[]{GEN1, GEN1_START, GEN1_END},
            new Object[]{GEN2, GEN2_START, GEN2_END},
            new Object[]{GEN3, GEN3_START, GEN3_END},
            new Object[]{GEN4, GEN4_START, GEN4_END},
            new Object[]{GEN5, GEN5_START, GEN5_END},
            new Object[]{GEN6, GEN6_START, GEN6_END},
            new Object[]{GEN7, GEN7_START, GEN7_END},
            new Object[]{GEN8, GEN8_START, GEN8_END}
    );


    public static boolean isGenerationId(String gen)
    {
        for (Object[] generation : generations)
        {
            String g = (String) generation[0];
            if (g.equalsIgnoreCase(gen))
            {
                return true;
            }
        }
        return false;
    }

    public static String returnGenerationRange(String gen)
    {
        for (Object[] generation : generations)
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

    public static String returnGenerationId(Pokemon pokemon)
    {
        Integer id = pokemon.getNumber();
        for (Object[] generation : generations)
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

    public static String formatHint1(Pokemon pokemon)
    {
        String id = pokemon.getId();

        String firstLetter = id.substring(0, 1).toUpperCase();
        Integer length = id.length();
        String type1 = pokemon.getType1();

        String gen = PokemonUtils.returnGenerationId(pokemon);

        String message = "Pokemon starts with the letter %s and has %s characters, %s type, %s.";

        return String.format(message, firstLetter, length.toString(), type1, gen);
    }
}
