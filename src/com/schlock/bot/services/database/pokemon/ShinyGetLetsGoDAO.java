package com.schlock.bot.services.database.pokemon;

import com.schlock.bot.entities.pokemon.ShinyGetLetsGo;
import com.schlock.bot.services.database.BaseDAO;

public interface ShinyGetLetsGoDAO extends BaseDAO<ShinyGetLetsGo>
{
    Double getCurrentAverageTimeToShiny();

    Double getCurrentAverageNumberOfRareChecks();

    Integer getCurrentShinyNumber();

    ShinyGetLetsGo getMostRecent();
}
