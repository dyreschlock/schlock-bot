package com.schlock.bot.services.apps.bet.impl;

import com.schlock.bot.entities.Persisted;
import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.bet.ShinyBet;
import com.schlock.bot.services.database.DatabaseTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShinyRegPayoutServiceImplTest extends DatabaseTest
{
    private static final Integer BALANCE = 10000;

    private static final String USERNAME1 = "username1";
    private static final String USERNAME2 = "username2";
    private static final String USERNAME3 = "username3";

    private static final String BET1_POKEMON = "beedrill";
    private static final Integer BET1_MINUTES = 100;
    private static final Integer BET1_AMOUNT = 100;

    private static final String BET2_POKEMON = "beedrill";
    private static final Integer BET2_MINUTES = 80;
    private static final Integer BET2_AMOUNT = 100;

    private static final String BET3_POKEMON = "kakuna";
    private static final Integer BET3_MINUTES = 110;
    private static final Integer BET3_AMOUNT = 100;

    private ShinyRegPayoutServiceImpl impl;

    private User user1;
    private User user2;
    private User user3;

    private ShinyBet bet1;
    private ShinyBet bet2;
    private ShinyBet bet3;


    @Test
    public void testUsecase1()
    {

    }


    @BeforeEach
    public void setup() throws Exception
    {
        setupDatabase();
        createTestObjects();
    }

    private void createTestObjects()
    {
        user1 = new User();
        user1.setUsername(USERNAME1);
        user1.setBalance(BALANCE);

        user2 = new User();
        user2.setUsername(USERNAME2);
        user2.setBalance(BALANCE);

        user3 = new User();
        user3.setUsername(USERNAME3);
        user3.setBalance(BALANCE);


        bet1 = new ShinyBet();
        bet1.setUser(user1);
        bet1.setPokemonId(BET1_POKEMON);
        bet1.setTimeMinutes(BET1_MINUTES);
        bet1.setBetAmount(BET1_AMOUNT);

        bet2 = new ShinyBet();
        bet2.setUser(user2);
        bet2.setPokemonId(BET2_POKEMON);
        bet2.setTimeMinutes(BET2_MINUTES);
        bet2.setBetAmount(BET2_AMOUNT);

        bet3 = new ShinyBet();
        bet3.setUser(user3);
        bet3.setPokemonId(BET3_POKEMON);
        bet3.setTimeMinutes(BET3_MINUTES);
        bet3.setBetAmount(BET3_AMOUNT);


        List<Persisted> objects = Arrays.asList(user1, user2, user3, bet1, bet2, bet3);
        getDatabase().save(objects);
    }


    @AfterEach
    public void teardown()
    {
        removeTestObjects();
    }

    private void removeTestObjects()
    {
        List<Persisted> objects = Arrays.asList(user1, user2, user3);
        getDatabase().delete(objects);
    }
}