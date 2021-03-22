package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.GuessingStreak;
import com.schlock.bot.services.database.AbstractBaseDAO;
import com.schlock.bot.services.database.pokemon.GuessingStreakDAO;
import org.hibernate.Session;

import java.util.List;

public class GuessingStreakDAOImpl extends AbstractBaseDAO<GuessingStreak> implements GuessingStreakDAO
{
    public GuessingStreakDAOImpl(Session session)
    {
        super(GuessingStreak.class, session);
    }

    public GuessingStreak get()
    {
        List<GuessingStreak> streaks = getAll();

        if (!streaks.isEmpty())
        {
            return streaks.get(0);
        }
        return null;
    }
}
