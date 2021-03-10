package com.schlock.bot.services.entities.pokemon;

import com.schlock.bot.entities.pokemon.Pokemon;

public interface PokemonUtils
{
    public String formatOutput(Pokemon pokemon);

    public String formatOutput(Pokemon pokemon, boolean type, boolean basestats);

    public boolean isGenerationId(String input);

    public String returnGenerationRange(String gen);

    public String returnGenerationId(Pokemon pokemon);

    public Integer returnFirstPokemonNumberInGeneration(String input);

    public Integer returnLastPokemonNumberInGeneration(String input);

    public String formatHint1(Pokemon pokemon);
}
