package com.schlock.bot.services.database.bet.impl;

import com.schlock.bot.entities.bet.BettingUser;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.bet.BettingUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BettingUserDAOImplTest extends DatabaseTest
{
    private final static String USERNAME1 = "username1";

    @Test
    public void testGetByUsername()
    {
        BettingUser user1 = getDatabase().get(BettingUserDAO.class).getByUsername(USERNAME1);

        assertEquals(user1.getUsername(), USERNAME1);
    }

    @BeforeEach
    public void setup() throws Exception
    {
        databaseSetup();
        createTestObjects();
    }

    private void createTestObjects()
    {
//        BettingUser user1 = new BettingUser();
//        user1.setUsername(USERNAME1);
//        user1.setBalance(10000);
//
//        getDatabase().save(user1);
    }
}