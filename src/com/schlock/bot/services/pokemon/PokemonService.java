package com.schlock.bot.services.pokemon;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.services.ListenerService;

public interface PokemonService extends ListenerService
{
    Pokemon getPokemonFromText(String text);
}
