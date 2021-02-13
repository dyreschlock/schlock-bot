package com.schlock.bot.services.database.apps.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.apps.UserDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserDAOImplTest extends DatabaseTest
{
    private final static String USERNAME1 = "username1";

    private User testUser1;

    @Test
    public void testGetByUsername()
    {
        User user1 = getDatabase().get(UserDAO.class).getByUsername(USERNAME1);

        assertEquals(user1.getUsername(), testUser1.getUsername());
    }

    @BeforeEach
    public void setup() throws Exception
    {
        databaseSetup();
        createTestObjects();
    }

    @AfterEach
    public void teardown()
    {
        removeTestObjects();
    }

    private void createTestObjects()
    {
        testUser1 = new User();
        testUser1.setUsername(USERNAME1);
        testUser1.setBalance(10000);

        getDatabase().save(testUser1);
    }

    private void removeTestObjects()
    {
        getDatabase().delete(testUser1);

    }
}