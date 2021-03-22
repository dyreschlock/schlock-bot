package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.entities.pokemon.GuessingStreak;
import com.schlock.bot.services.database.DatabaseTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GuessingStreakDAOImplTest extends DatabaseTest
{
    private static final String USERNAME = "username_guess";

    private GuessingStreakDAOImpl daoImpl;

    private GuessingStreak streak;
    private User testUser;

    @Test
    public void testGetMethod()
    {
        GuessingStreak s = daoImpl.get();

        assertEquals(streak, s);
    }

    protected void before() throws Exception
    {
        daoImpl = new GuessingStreakDAOImpl(session);

        createTestObjects();
    }

    protected void after() throws Exception
    {
        removeTestObjects();
    }

    private void createTestObjects()
    {
        testUser = new User(USERNAME);

        streak = new GuessingStreak();
        streak.setUser(testUser);
        streak.setStreakNumber(1);

        daoImpl.save(testUser, streak);
    }

    private void removeTestObjects()
    {
        daoImpl.delete(testUser, streak);
    }
}