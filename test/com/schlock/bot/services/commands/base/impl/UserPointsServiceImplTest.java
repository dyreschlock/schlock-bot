package com.schlock.bot.services.commands.base.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.entities.pokemon.ShinyBet;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.base.UserDAO;
import com.schlock.bot.services.entities.base.UserManagement;
import com.schlock.bot.services.entities.base.impl.UserManagementImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserPointsServiceImplTest extends DatabaseTest
{
    private static final String BALANCE_COMMAND = UserPointsServiceImpl.BALANCE_COMMAND;
    private static final String PRESTIGE_COMMAND = UserPointsServiceImpl.PRESTIGE_COMMAND;

    private static final String USER_BALANCE_KEY = UserPointsServiceImpl.USER_BALANCE_KEY;

    private static final String PRESTIGE_CONFIRM_KEY = UserPointsServiceImpl.PRESTIGE_CONFIRM_KEY;
    private static final String PRESTIGE_SUCCESS_KEY = UserPointsServiceImpl.PRESTIGE_SUCCESS_KEY;
    private static final String PRESTIGE_NOT_ENOUGH_POINTS_KEY = UserPointsServiceImpl.PRESTIGE_NOT_ENOUGH_POINTS_KEY;
    private static final String PRESTIGE_CANT_WITH_BETS = UserPointsServiceImpl.PRESTIGE_CANT_WITH_BETS_KEY;


    private final static String USERNAME1 = "username1_points";
    private final static Long USER1_BALANCE = Long.valueOf(5000);

    private final static String USERNAME2 = "useranme2_points";

    private UserPointsServiceImpl impl;

    private User testUser1;

    @Test
    public void checkBalance()
    {
        String response = impl.process(USERNAME1, BALANCE_COMMAND).getFirstMessage();
        String expected = messages().format(USER_BALANCE_KEY, USERNAME1, USER1_BALANCE.toString(), getMark());

        assertEquals(expected, response);

        String defaultBalance = config().getUserDefaultBalance().toString();

        response = impl.process(USERNAME2, UserPointsServiceImpl.BALANCE_COMMAND).getFirstMessage();
        expected = messages().format(USER_BALANCE_KEY, USERNAME2, defaultBalance, getMark());

        assertEquals(expected, response);
    }

    @Test
    public void testPrestiging()
    {
        Long prestigeBaseValue = config().getUserPrestigeBaseValue();
        Long defaultBalance = config().getUserDefaultBalance();

        String response = impl.process(USERNAME1, PRESTIGE_COMMAND).getFirstMessage();
        String expected = messages().format(PRESTIGE_NOT_ENOUGH_POINTS_KEY, prestigeBaseValue, getMark(), testUser1.getBalance(), getMark());

        assertEquals(expected, response);

        testUser1.incrementBalance(prestigeBaseValue.intValue());

        ShinyBet bet = new ShinyBet();
        bet.setBetAmount(1000);
        bet.setUser(testUser1);
        bet.setPokemonId("bulbasaur");
        bet.setTimeMinutes(60);

        database().save(testUser1, bet);

        response = impl.process(USERNAME1, PRESTIGE_COMMAND).getFirstMessage();
        expected = messages().get(PRESTIGE_CANT_WITH_BETS);

        assertEquals(expected, response);

        database().delete(bet);

        response = impl.process(USERNAME1, PRESTIGE_COMMAND).getFirstMessage();
        expected = messages().get(PRESTIGE_CONFIRM_KEY);

        assertEquals(expected, response);

        ListenerResponse responses = impl.process(USERNAME1, PRESTIGE_COMMAND + " yes");

        response = responses.getMessages().get(0);

        testUser1 = database().get(UserDAO.class).getByUsername(USERNAME1);
        expected = messages().format(PRESTIGE_SUCCESS_KEY, USERNAME1, testUser1.getPrestigeLevel());

        assertEquals(expected, response);

        response = responses.getMessages().get(1);
        expected = messages().format(UserPointsServiceImpl.USER_ADDED_MULTIPLIER_KEY, USERNAME1, testUser1.getPointsDoubler());

        assertEquals(1, testUser1.getPrestige());
        assertEquals(defaultBalance, testUser1.getBalance());
    }


    private String getMark()
    {
        return config().getCurrencyMark();
    }

    @Override
    protected void before() throws Exception
    {
        UserManagement userManagement = new UserManagementImpl(database(), config());

        impl = new UserPointsServiceImpl(userManagement, database(), messages(), config());

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
        testUser1.setHighScorePoints(USER1_BALANCE);
        testUser1.setPrestige(0);
        testUser1.setPointsDoubler(0);

        database().save(testUser1);
    }

    private void removeTestObjects()
    {
        User user2 = database().get(UserDAO.class).getByUsername(USERNAME2);
        if (user2 != null)
        {
            database().delete(testUser1, user2);
        }
        else
        {
            database().delete(testUser1);
        }
    }
}