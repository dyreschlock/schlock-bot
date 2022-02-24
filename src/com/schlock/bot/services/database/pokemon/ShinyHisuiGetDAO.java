package com.schlock.bot.services.database.pokemon;

import com.schlock.bot.entities.pokemon.ShinyHisuiGet;
import com.schlock.bot.services.database.BaseDAO;

public interface ShinyHisuiGetDAO extends BaseDAO<ShinyHisuiGet>
{
    Double getCurrentResetAverage();

    Integer getCurrentShinyNumber();
}
