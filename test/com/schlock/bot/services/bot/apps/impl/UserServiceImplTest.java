package com.schlock.bot.services.bot.apps.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.apps.UserDAO;
import com.schlock.bot.services.database.apps.impl.UserDAOImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserServiceImplTest extends DatabaseTest
{
    private final static String USERNAME1 = "username1";
    private final static Integer USER1_BALANCE = 5000;

    private final static String USERNAME2 = "username2";

    private UserDAO userDAO;

    private UserServiceImpl impl;

    private User testUser1;

    @Test
    public void checkBalance()
    {
        String response = impl.processSingleResult(USERNAME1, "!balance");
        String expected = USERNAME1 + " your balance is " + USER1_BALANCE.toString() + getMark();

        assertEquals(expected, response);

        String defaultBalance = config().getUserDefaultBalance().toString();

        response = impl.processSingleResult(USERNAME2, "!balance");
        expected = USERNAME2 + " your balance is " + defaultBalance + getMark();

        assertEquals(expected, response);
    }

    private String getMark()
    {
        return config().getCurrencyMark();
    }

    @Override
    protected void before() throws Exception
    {
        userDAO = new UserDAOImpl(session)
        {
            public void commit()
            {
            }
        };

        impl = new UserServiceImpl(userDAO, config());

        createTestObjects();
    }

    @Override
    protected void after() throws Exception
    {
        removeTestObjects();
    }

    private void createTestObjects()
    {
        testUser1 = new User();
        testUser1.setUsername(USERNAME1);
        testUser1.setBalance(USER1_BALANCE);

        userDAO.save(testUser1);
    }

    private void removeTestObjects()
    {
        User user2 = userDAO.getByUsername(USERNAME2);

        userDAO.delete(testUser1, user2);
    }
}