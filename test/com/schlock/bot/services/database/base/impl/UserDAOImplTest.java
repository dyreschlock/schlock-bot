package com.schlock.bot.services.database.base.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.base.UserDAO;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UserDAOImplTest extends DatabaseTest
{
    private final static String USERNAME1 = "username1";
    private final static String USERNAME2 = "username2";

    private UserDAO userDAO;

    private User testUser1;
    private User testUser2;

    @Test
    public void testGetByUsername()
    {
        User user1 = userDAO.getByUsername(USERNAME1);

        assertEquals(user1.getUsername(), testUser1.getUsername());
    }

    @Test
    public void testMostRecent()
    {
        User user = userDAO.getMostRecentUser();

        assertNotEquals(testUser1.getUsername(), user.getUsername());
        assertEquals(testUser2.getUsername(), user.getUsername());
    }

    @Test
    public void testOrderByPoints()
    {
        List<User> users = userDAO.getOrderByPoints(10);

        assertEquals(2, users.size());
        assertEquals(testUser1.getUsername(), users.get(0).getUsername());
        assertEquals(testUser2.getUsername(), users.get(1).getUsername());
    }

    @Test
    public void testOrderByPointsIgnore()
    {
        List<String> ignore = Arrays.asList(USERNAME1);

        List<User> users = userDAO.getOrderByPoints(10, ignore);

        assertEquals(1, users.size());
        assertEquals(testUser2.getUsername(), users.get(0).getUsername());
    }


    protected void before() throws Exception
    {
        userDAO = database().get(UserDAO.class);

        createTestObjects();
    }

    protected void after() throws Exception
    {
        removeTestObjects();
    }

    private void createTestObjects()
    {
        testUser1 = new User();
        testUser1.setUsername(USERNAME1);
        testUser1.setBalance(Long.valueOf(10000));
        testUser1.setFollowDate(new Date(10000));

        testUser2 = new User();
        testUser2.setUsername(USERNAME2);
        testUser2.setBalance(Long.valueOf(1000));
        testUser2.setFollowDate(new Date(20000));

        database().save(testUser1, testUser2);
    }

    private void removeTestObjects()
    {
        database().delete(testUser1, testUser2);
    }
}