package com.schlock.bot.services.database.pokemon;

import com.schlock.bot.entities.pokemon.ShinyBet;
import com.schlock.bot.services.database.BaseDAO;

import java.util.List;

public interface ShinyBetDAO extends BaseDAO<ShinyBet>
{
    List<ShinyBet> getByUsername(String username);

    ShinyBet getByUsernameAndPokemon(String username, String pokemon);

    List<ShinyBet> getAllCurrent();
}
