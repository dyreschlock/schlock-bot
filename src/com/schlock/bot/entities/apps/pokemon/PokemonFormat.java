package com.schlock.bot.entities.apps.pokemon;

public class PokemonFormat
{
    protected static final String POKEMON_RETURN_FORMAT = "No. %s %s";

    public static String format(Pokemon pokemon)
    {
        return format(pokemon, false, false);
    }

    public static String format(Pokemon pokemon, boolean type, boolean basestats)
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
}
