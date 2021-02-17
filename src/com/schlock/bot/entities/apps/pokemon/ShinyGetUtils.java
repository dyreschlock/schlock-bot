package com.schlock.bot.entities.apps.pokemon;

public class ShinyGetUtils
{
    private static final String FORMAT_MESSAGE = "Most Recent Shiny: %s after %s minutes. (#%s) ";

    public static String format(ShinyGet get, Pokemon pokemon)
    {
        String pokemonName = pokemon.getName();
        String timeMinutes = get.getTimeInMinutes().toString();
        String shinyNumber = get.getShinyNumber().toString();

        return String.format(FORMAT_MESSAGE, pokemonName, timeMinutes, shinyNumber);
    }
}
