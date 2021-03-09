package com.schlock.bot.services.entities.pokemon;

import com.schlock.bot.entities.apps.pokemon.Pokemon;

public interface PokemonManagement
{
    Pokemon getPokemonFromText(String text);

    Pokemon getRandomPokemon();

    boolean isGenSearch(String params);

    Pokemon getRandomPokemonInGen(String gen);

    boolean isRangeSearch(String params);

    Pokemon getRandomPokemonInRange(String rangeText);
}
