package com.schlock.bot.services.database.pokemon;

import com.schlock.bot.entities.pokemon.ShinyDexEntryHisui;
import com.schlock.bot.services.database.BaseDAO;

import java.util.List;

public interface ShinyDexEntryHisuiDAO extends BaseDAO<ShinyDexEntryHisui>
{
    List<ShinyDexEntryHisui> getInPokemonOrder();
}
