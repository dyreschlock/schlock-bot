package com.schlock.bot.services.database.pokemon;

import com.schlock.bot.entities.pokemon.ShinyGetHisui;
import com.schlock.bot.services.database.BaseDAO;

public interface ShinyGetHisuiDAO extends BaseDAO<ShinyGetHisui>
{
    Double getCurrentResetAverage();

    Integer getCurrentShinyNumber();

    Integer getCurrentAlphaNumber();
}
