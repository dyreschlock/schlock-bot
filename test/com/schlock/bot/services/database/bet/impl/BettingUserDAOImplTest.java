package com.schlock.bot.services.database.bet.impl;

import com.schlock.bot.entities.bet.BettingUser;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.bet.BettingUserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BettingUserDAOImplTest extends DatabaseTest
{
    private final static String USERNAME1 = "username1";

    @Test
    public void testGetByUsername()
    {
        BettingUser user1 = getDatabase().get(BettingUserDAO.class).getByUsername(USERNAME1);

    }

    @BeforeEach
    public void setup() throws Exception
    {
        databaseSetup();
        createTestObjects();
    }

    private void createTestObjects()
    {

    }
}