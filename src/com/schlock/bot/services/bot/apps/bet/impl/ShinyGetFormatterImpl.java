package com.schlock.bot.services.bot.apps.bet.impl;

import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.entities.apps.pokemon.ShinyGet;
import com.schlock.bot.services.bot.apps.bet.ShinyGetFormatter;
import com.schlock.bot.services.bot.apps.pokemon.PokemonService;
import org.apache.tapestry5.ioc.Messages;

public class ShinyGetFormatterImpl implements ShinyGetFormatter
{
    private static final String SHINY_CAUGHT_KEY = "shiny-caught";
    private static final String MOST_RECENT_KEY = "most-recent-shiny";

    private PokemonService pokemonService;

    private Messages messages;

    public ShinyGetFormatterImpl(PokemonService pokemonService,
                                 Messages messages)
    {
        this.pokemonService = pokemonService;
        this.messages = messages;
    }

    public String formatNewlyCaught(ShinyGet get)
    {
        Pokemon pokemon = pokemonService.getPokemonFromText(get.getPokemonId());

        String pokemonName = pokemon.getName();
        String timeMinutes = get.getTimeInMinutes().toString();
        String shinyNumber = get.getShinyNumber().toString();

        return messages.format(SHINY_CAUGHT_KEY, pokemonName, timeMinutes, shinyNumber);
    }

    public String formatMostRecent(ShinyGet get)
    {
        Pokemon pokemon = pokemonService.getPokemonFromText(get.getPokemonId());

        String pokemonName = pokemon.getName();
        String timeMinutes = get.getTimeInMinutes().toString();
        String shinyNumber = get.getShinyNumber().toString();

        return messages.format(MOST_RECENT_KEY, pokemonName, timeMinutes, shinyNumber);
    }
}
