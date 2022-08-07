package com.schlock.bot.services.commands.pokemon.quiz;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.services.commands.ChatGameListenerService;

public interface RatePokemonService extends ChatGameListenerService
{
    Pokemon getCurrentPokemon();
}
