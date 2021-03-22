package com.schlock.bot.services.database.pokemon;

import com.schlock.bot.entities.pokemon.GuessingStreak;
import com.schlock.bot.services.database.BaseDAO;

public interface GuessingStreakDAO extends BaseDAO<GuessingStreak>
{
    GuessingStreak get();
}
