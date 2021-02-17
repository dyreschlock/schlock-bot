package com.schlock.bot.services.database.apps.impl;

import com.schlock.bot.entities.Persisted;
import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.bet.ShinyBet;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.apps.ShinyBetDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

class ShinyBetDAOImplTest extends DatabaseTest
{
    private static final String USERNAME1 = "username1";
    private static final String USERNAME2 = "username2";

    private User user1;
    private User user2;

    private ShinyBet bet1;
    private ShinyBet bet2;
    private ShinyBet bet3;

    @Test
    public void testGetBets()
    {
        List<ShinyBet> bets1 = getDatabase().get(ShinyBetDAO.class).getBetsByUser(user1);

        assertEquals(bets1.size(), 2);


        List<ShinyBet> bets2 = getDatabase().get(ShinyBetDAO.class).getBetsByUser(user2);

        assertEquals(bets2.size(), 1);
    }


    @BeforeEach
    public void setup() throws Exception
    {
        setupDatabase();
        createTestObjects();
    }

    public void createTestObjects()
    {
        user1 = new User();
        user1.setUsername(USERNAME1);

        user2 = new User();
        user2.setUsername(USERNAME2);

        bet1 = new ShinyBet();
        bet1.setUser(user1);

        bet2 = new ShinyBet();
        bet2.setUser(user1);

        bet3 = new ShinyBet();
        bet3.setUser(user2);

        List<Persisted> objects = Arrays.asList(bet1, bet2, bet3, user1, user2);
        getDatabase().save(objects);
    }

    @AfterEach
    public void tearDown()
    {
        removetTestObjects();
    }

    public void removetTestObjects()
    {
        List<Persisted> objects = Arrays.asList(bet1, bet2, bet3, user1, user2);
        getDatabase().delete(objects);
    }
}