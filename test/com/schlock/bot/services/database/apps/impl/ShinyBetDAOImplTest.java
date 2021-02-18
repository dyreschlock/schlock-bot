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

    private static final String BET1_POKEMON = "bulbasaur";
    private static final String BET2_POKEMON = "ivysaur";
    private static final String BET3_POKEMON = "bulbasaur";

    private static final Integer BET1_AMOUNT = 100;
    private static final Integer BET2_AMOUNT = 200;
    private static final Integer BET3_AMOUNT = 300;

    private static final String OTHER_BET = "magikarp";

    private User user1;
    private User user2;

    private ShinyBet bet1;
    private ShinyBet bet2;
    private ShinyBet bet3;

    @Test
    public void testGetAllByUsername()
    {
        List<ShinyBet> bets1 = getDatabase().get(ShinyBetDAO.class).getByUsername(user1.getUsername());

        assertEquals(bets1.size(), 2);


        List<ShinyBet> bets2 = getDatabase().get(ShinyBetDAO.class).getByUsername(user2.getUsername());

        assertEquals(bets2.size(), 1);
    }

    @Test
    public void testGetByUsernamePokemon()
    {
        ShinyBet bet;

        bet = getDatabase().get(ShinyBetDAO.class).getByUsernameAndPokemon(user1.getUsername(), BET1_POKEMON);
        assertNotNull(bet);
        assertEquals(bet1, bet);

        bet = getDatabase().get(ShinyBetDAO.class).getByUsernameAndPokemon(user1.getUsername(), BET2_POKEMON);
        assertNotNull(bet);
        assertEquals(bet2, bet);

        bet = getDatabase().get(ShinyBetDAO.class).getByUsernameAndPokemon(user2.getUsername(), BET3_POKEMON);
        assertNotNull(bet);
        assertEquals(bet3, bet);

        bet = getDatabase().get(ShinyBetDAO.class).getByUsernameAndPokemon(user2.getUsername(), BET2_POKEMON);
        assertNull(bet);

        bet = getDatabase().get(ShinyBetDAO.class).getByUsernameAndPokemon(user1.getUsername(), OTHER_BET);
        assertNull(bet);

        bet = getDatabase().get(ShinyBetDAO.class).getByUsernameAndPokemon(user2.getUsername(), OTHER_BET);
        assertNull(bet);
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
        bet1.setPokemonId(BET1_POKEMON);
        bet1.setBetAmount(BET1_AMOUNT);

        bet2 = new ShinyBet();
        bet2.setUser(user1);
        bet2.setPokemonId(BET2_POKEMON);
        bet2.setBetAmount(BET2_AMOUNT);

        bet3 = new ShinyBet();
        bet3.setUser(user2);
        bet3.setPokemonId(BET3_POKEMON);
        bet3.setBetAmount(BET3_AMOUNT);

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