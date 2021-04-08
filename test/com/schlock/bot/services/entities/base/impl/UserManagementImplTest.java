package com.schlock.bot.services.entities.base.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.entities.base.UserManagement;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserManagementImplTest extends DatabaseTest
{
    private static final String USERNAME = "usermanagement_test";

    private UserManagement userManagement;

    private User user1;

    @Test
    public void testPrestiging()
    {
        user1.setPrestige(0);
        user1.setBalance(Long.valueOf(1000000));

        assertTrue(userManagement.prestigeUser(user1));

        assertEquals(user1.getPrestige(), 1);
        assertEquals(user1.getBalance(), config().getUserDefaultBalance());
    }

    @Test
    public void testPrestigeValues()
    {
        user1.setPrestige(0);

        Long prestigeValue = userManagement.getUserPrestigeValue(user1);
        Long expectedValue = config().getUserPrestigeBaseValue();

        assertEquals(expectedValue, prestigeValue);

        user1.setPrestige(1);

        prestigeValue = userManagement.getUserPrestigeValue(user1);
        expectedValue = expectedValue *4;

        assertEquals(expectedValue, prestigeValue);

        user1.setPrestige(2);

        prestigeValue = userManagement.getUserPrestigeValue(user1);
        expectedValue = expectedValue *4;

        assertEquals(expectedValue, prestigeValue);

        user1.setPrestige(3);

        prestigeValue = userManagement.getUserPrestigeValue(user1);
        expectedValue = expectedValue *4;

        assertEquals(expectedValue, prestigeValue);
    }

    protected void before() throws Exception
    {
        userManagement = new UserManagementImpl(database(), config());

        user1 = userManagement.createNewDefaultUser(USERNAME);

        database().save(user1);
    }

    protected void after() throws Exception
    {
        database().delete(user1);
    }
}