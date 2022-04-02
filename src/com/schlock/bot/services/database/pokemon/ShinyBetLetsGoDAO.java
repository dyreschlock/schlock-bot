package com.schlock.bot.services.database.pokemon;

import com.schlock.bot.entities.pokemon.ShinyBetLetsGo;
import com.schlock.bot.services.database.BaseDAO;

import java.util.List;

public interface ShinyBetLetsGoDAO extends BaseDAO<ShinyBetLetsGo>
{
    List<ShinyBetLetsGo> getByUsername(String username);

    ShinyBetLetsGo getByUsernameAndPokemon(String username, String pokemon);

    List<ShinyBetLetsGo> getAllCurrent();
}
