package com.schlock.bot.services.database.pokemon;

import com.schlock.bot.entities.pokemon.PokemonGoDexEntry;
import com.schlock.bot.services.database.BaseDAO;

import java.util.List;

public interface PokemonGoDexEntryDAO extends BaseDAO<PokemonGoDexEntry>
{
    List<PokemonGoDexEntry> getInPokemonOrder();
}
