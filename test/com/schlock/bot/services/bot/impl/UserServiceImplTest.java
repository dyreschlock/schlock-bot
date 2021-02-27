package com.schlock.bot.services.bot.impl;

import com.schlock.bot.entities.Persisted;
import com.schlock.bot.entities.apps.User;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.apps.UserDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserServiceImplTest extends DatabaseTest
{
    private final static String USERNAME1 = "username1";
    private final static Integer USER1_BALANCE = 5000;

    private final static String USERNAME2 = "username2";

    private UserServiceImpl impl;

    private User testUser1;

    @Test
    public void checkBalance()
    {
        String response = impl.processSingleResult(USERNAME1, "!balance");
        String expected = USERNAME1 + " your balance is " + USER1_BALANCE.toString() + getMark();

        assertEquals(expected, response);

        String defaultBalance = getDeploymentConfiguration().getUserDefaultBalance().toString();

        response = impl.processSingleResult(USERNAME2, "!balance");
        expected = USERNAME2 + " your balance is " + defaultBalance + getMark();

        assertEquals(expected, response);
    }

    private String getMark()
    {
        return getDeploymentConfiguration().getCurrencyMark();
    }

    @BeforeEach
    public void setup() throws Exception
    {
        setupDatabase();
        createTestObjects();

        impl = new UserServiceImpl(getDatabase().get(UserDAO.class), getDeploymentConfiguration());
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
        testUser1.setBalance(USER1_BALANCE);

        getDatabase().save(testUser1);
    }

    private void removeTestObjects()
    {
        User generated = getDatabase().get(UserDAO.class).getByUsername(USERNAME2);

        List<Persisted> objects = Arrays.asList(testUser1, generated);

        getDatabase().delete(objects);
    }
}