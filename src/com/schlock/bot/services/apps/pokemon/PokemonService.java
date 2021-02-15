package com.schlock.bot.services.apps.pokemon;

import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.services.ListenerService;

public interface PokemonService extends ListenerService
{
    Pokemon getPokemonFromText(String text);

    Pokemon getRandomPokemon();

    boolean isGenSearch(String params);

    Pokemon getRandomPokemonInGen(String gen);

    boolean isRangeSearch(String params);

    Pokemon getRandomPokemonInRange(String rangeText);
}
