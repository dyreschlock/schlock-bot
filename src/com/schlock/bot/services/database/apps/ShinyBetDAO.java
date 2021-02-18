package com.schlock.bot.services.database.apps;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.bet.ShinyBet;
import com.schlock.bot.services.database.BaseDAO;

import java.util.List;

public interface ShinyBetDAO extends BaseDAO<ShinyBet>
{
    List<ShinyBet> getByUsername(String username);

    ShinyBet getByUsernameAndPokemon(String username, String pokemon);
}