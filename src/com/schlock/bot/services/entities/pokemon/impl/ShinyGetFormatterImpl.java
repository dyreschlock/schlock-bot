package com.schlock.bot.services.entities.pokemon.impl;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.entities.pokemon.ShinyGet;
import com.schlock.bot.entities.pokemon.ShinyHisuiGet;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.ShinyGetFormatter;
import org.apache.tapestry5.ioc.Messages;

public class ShinyGetFormatterImpl implements ShinyGetFormatter
{
    private static final String SHINY_CAUGHT_KEY = "shiny-caught";
    private static final String MOST_RECENT_KEY = "most-recent-shiny";

    private static final String HISUI_SHINY_WILD = "shiny-hisui-wild";
    private static final String HISUI_SHINY_OUTBREAK = "shiny-hisui-outbreak";
    private static final String HISUI_SHINY_MASSIVE = "shiny-hisui-massive";

    private PokemonManagement pokemonManagement;

    private Messages messages;

    public ShinyGetFormatterImpl(PokemonManagement pokemonManagement,
                                 Messages messages)
    {
        this.pokemonManagement = pokemonManagement;
        this.messages = messages;
    }

    public String formatNewlyCaught(ShinyGet get)
    {
        Pokemon pokemon = pokemonManagement.getPokemonFromText(get.getPokemonId());

        String pokemonName = pokemon.getName();
        String timeMinutes = get.getTimeInMinutes().toString();
        String shinyNumber = get.getShinyNumber().toString();

        String verb = messages.get(get.getType().name().toLowerCase());

        return messages.format(SHINY_CAUGHT_KEY, pokemonName, verb, timeMinutes, shinyNumber);
    }

    public String formatMostRecent(ShinyGet get)
    {
        Pokemon pokemon = pokemonManagement.getPokemonFromText(get.getPokemonId());

        String pokemonName = pokemon.getName();
        String timeMinutes = get.getTimeInMinutes().toString();
        String shinyNumber = get.getShinyNumber().toString();

        return messages.format(MOST_RECENT_KEY, pokemonName, timeMinutes, shinyNumber);
    }

    public String formatNewlyCaughtHisui(ShinyHisuiGet get)
    {
        Pokemon pokemon = pokemonManagement.getPokemonFromText(get.getPokemonId());

        String pokemonName = pokemon.getName();
        if (get.isOutbreak())
        {
            Integer resets = get.getResets();
            return messages.format(HISUI_SHINY_OUTBREAK, pokemonName, resets.toString());
        }
        if (get.isMassiveOutbreak())
        {
            return messages.format(HISUI_SHINY_MASSIVE, pokemonName);
        }
        return messages.format(HISUI_SHINY_WILD, pokemonName);
    }
}
