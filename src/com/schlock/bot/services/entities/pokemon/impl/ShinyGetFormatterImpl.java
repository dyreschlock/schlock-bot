package com.schlock.bot.services.entities.pokemon.impl;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.entities.pokemon.ShinyGetLetsGo;
import com.schlock.bot.entities.pokemon.ShinyGetHisui;
import com.schlock.bot.entities.pokemon.ShinyGetType;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.ShinyGetFormatter;
import org.apache.tapestry5.ioc.Messages;

public class ShinyGetFormatterImpl implements ShinyGetFormatter
{
    private static final String SHINY_CAUGHT_KEY = "shiny-caught";
    private static final String SHINY_RESET_CAUGHT_KEY = "shiny-reset-caught";
    private static final String MOST_RECENT_KEY = "most-recent-shiny";

    private static final String HISUI_SHINY_WILD = "shiny-hisui-wild";
    private static final String HISUI_SHINY_OUTBREAK = "shiny-hisui-outbreak";
    private static final String HISUI_SHINY_MASSIVE = "shiny-hisui-massive";

    private static final String HISUI_SHINY_ALPHA = "shiny-hisui-alpha";
    private static final String HISUI_SHINY_ALPHA_COUNT = "shiny-hisui-alpha-count";

    private PokemonManagement pokemonManagement;

    private Messages messages;

    public ShinyGetFormatterImpl(PokemonManagement pokemonManagement,
                                 Messages messages)
    {
        this.pokemonManagement = pokemonManagement;
        this.messages = messages;
    }

    public String formatNewlyCaughtLetsGo(ShinyGetLetsGo get)
    {
        Pokemon pokemon = pokemonManagement.getPokemonFromText(get.getPokemonId());

        String pokemonName = pokemon.getName();
        if (get.isAlolan())
        {
            pokemonName = "Alolan " + pokemonName;
        }
        String shinyNumber = get.getShinyNumber().toString();

        if (ShinyGetType.RESET.equals(get.getType()))
        {
            String resets = get.getNumOfRareChecks().toString();

            return messages.format(SHINY_RESET_CAUGHT_KEY, pokemonName, resets, shinyNumber);
        }

        String timeMinutes = get.getTimeInMinutes().toString();
        String verb = messages.get(get.getType().name().toLowerCase());

        return messages.format(SHINY_CAUGHT_KEY, pokemonName, verb, timeMinutes, shinyNumber);
    }

    public String formatMostRecent(ShinyGetLetsGo get)
    {
        Pokemon pokemon = pokemonManagement.getPokemonFromText(get.getPokemonId());

        String pokemonName = pokemon.getName();
        String timeMinutes = get.getTimeInMinutes().toString();
        String shinyNumber = get.getShinyNumber().toString();

        return messages.format(MOST_RECENT_KEY, pokemonName, timeMinutes, shinyNumber);
    }

    public String formatNewlyCaughtHisui(ShinyGetHisui get)
    {
        Pokemon pokemon = pokemonManagement.getPokemonFromText(get.getPokemonId());

        String message = "";

        String pokemonName = pokemon.getName();
        if (get.getAlphaNumber() != null)
        {
            pokemonName = messages.format(HISUI_SHINY_ALPHA, pokemonName);
        }

        Integer checks = get.getOutbreakChecks();
        if (get.isOutbreak())
        {
            message = messages.format(HISUI_SHINY_OUTBREAK, pokemonName, checks.toString());
        }
        else if (get.isMassiveOutbreak())
        {
            message = messages.format(HISUI_SHINY_MASSIVE, pokemonName, checks.toString());
        }
        else
        {
            message = messages.format(HISUI_SHINY_WILD, pokemonName, checks.toString());
        }

        if (get.getAlphaNumber() != null)
        {
            String alphaCount = messages.format(HISUI_SHINY_ALPHA_COUNT, get.getAlphaNumber());
            return message + " " + alphaCount;
        }
        return message;
    }
}
