package com.schlock.bot.services.database.pokemon;

import com.schlock.bot.entities.pokemon.ShinyDexEntryGo;
import com.schlock.bot.services.database.BaseDAO;

import java.util.List;

public interface ShinyDexEntryGoDAO extends BaseDAO<ShinyDexEntryGo>
{
    List<ShinyDexEntryGo> getInPokemonOrder();
}
