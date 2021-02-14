package com.schlock.bot.services.apps.pokemon;

import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.services.ListenerService;

public interface PokemonService extends ListenerService
{
    Pokemon getRandomPokemon();

    Pokemon getPokemonFromText(String text);
}
