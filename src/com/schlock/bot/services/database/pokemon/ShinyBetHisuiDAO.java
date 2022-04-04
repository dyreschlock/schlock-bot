package com.schlock.bot.services.database.pokemon;

import com.schlock.bot.entities.pokemon.ShinyBetHisui;
import com.schlock.bot.services.database.BaseDAO;

import java.util.List;

public interface ShinyBetHisuiDAO extends BaseDAO<ShinyBetHisui>
{
    ShinyBetHisui getByUsername(String username);

    List<ShinyBetHisui> getAllCurrent();
}
