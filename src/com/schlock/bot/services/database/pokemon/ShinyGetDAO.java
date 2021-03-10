package com.schlock.bot.services.database.pokemon;

import com.schlock.bot.entities.pokemon.ShinyGet;
import com.schlock.bot.services.database.BaseDAO;

public interface ShinyGetDAO extends BaseDAO<ShinyGet>
{
    Double getCurrentAverageTimeToShiny();

    Double getCurrentAverageNumberOfRareChecks();

    Integer getCurrentShinyNumber();

    ShinyGet getMostRecent();
}
