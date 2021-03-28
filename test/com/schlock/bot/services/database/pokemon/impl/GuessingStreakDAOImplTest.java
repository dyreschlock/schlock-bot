package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.entities.pokemon.GuessingStreak;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.pokemon.GuessingStreakDAO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GuessingStreakDAOImplTest extends DatabaseTest
{
    private static final String USERNAME = "username_guess";

    private GuessingStreakDAO streakDao;

    private GuessingStreak streak;
    private User testUser;

    @Test
    public void testGetMethod()
    {
        GuessingStreak s = streakDao.get();

        assertEquals(streak, s);
    }

    protected void before() throws Exception
    {
        streakDao = database.get(GuessingStreakDAO.class);

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
        streak.setStreakNumber(1);

        database.save(testUser);

        streak.setUserId(testUser.getId());

        database.save(streak);
    }

    private void removeTestObjects()
    {
        database.delete(testUser, streak);
    }
}