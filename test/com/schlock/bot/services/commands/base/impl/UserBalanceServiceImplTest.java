package com.schlock.bot.services.commands.base.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.services.database.DatabaseTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserBalanceServiceImplTest extends DatabaseTest
{
    private static final String USERNAME = "user_balance_test";
    private static final Long BALANCE = Long.valueOf(10000);

    private static final String BALANCE_COMMAND = UserBalanceServiceImpl.BALANCE_COMMAND;

    private static final String BALANCE_KEY = UserBalanceServiceImpl.BALANCE_KEY;
    private static final String BALANCE_WRONG_FORMAT_KEY = UserBalanceServiceImpl.BALANCE_WRONG_FORMAT_KEY;
    private static final String BALANCE_USER_UNKOWN_KEY = UserBalanceServiceImpl.BALANCE_USER_UNKNOWN_KEY;

    private User testUser;

    private UserBalanceServiceImpl impl;


    @Test
    public void checkBalance()
    {
        final String MARK = config().getCurrencyMark();

        String command = BALANCE_COMMAND;

        String response = impl.process(USERNAME, command).getFirstMessage();
        String expected = messages().format(BALANCE_WRONG_FORMAT_KEY);

        assertEquals(expected, response);


        String fakeUsername = "asdf asdf";

        command = BALANCE_COMMAND + " " + fakeUsername;

        response = impl.process(USERNAME, command).getFirstMessage();
        expected = messages().format(BALANCE_WRONG_FORMAT_KEY);

        assertEquals(expected, response);


        fakeUsername = "asdfasdf";

        command = BALANCE_COMMAND + " " + fakeUsername;

        response = impl.process(USERNAME, command).getFirstMessage();
        expected = messages().format(BALANCE_USER_UNKOWN_KEY, fakeUsername);

        assertEquals(expected, response);


        command = BALANCE_COMMAND + " " + USERNAME;

        response = impl.process(USERNAME, command).getFirstMessage();
        expected = messages().format(BALANCE_KEY, USERNAME, BALANCE, MARK);
        expected += testUser.getPrestigeLevel();

        assertEquals(expected, response);
    }


    @Override
    protected void before() throws Exception
    {
        impl = new UserBalanceServiceImpl(database(), messages(), config());

        createTestObjects();
    }

    public void createTestObjects()
    {
        testUser = new User();
        testUser.setUsername(USERNAME);
        testUser.setBalance(BALANCE);
        testUser.setHighScorePoints(BALANCE);
        testUser.setPrestige(2);
        testUser.setPointsDoubler(1);

        database().save(testUser);
    }

    @Override
    protected void after() throws Exception
    {
        removeTestObjects();
    }

    public void removeTestObjects()
    {
        database().delete(testUser);
    }
}