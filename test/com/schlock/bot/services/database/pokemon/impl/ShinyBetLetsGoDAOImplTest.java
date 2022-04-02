package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.entities.pokemon.ShinyBetLetsGo;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.pokemon.ShinyBetLetsGoDAO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShinyBetLetsGoDAOImplTest extends DatabaseTest
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

    private ShinyBetLetsGoDAO shinyBetDAO;

    private User user1;
    private User user2;

    private ShinyBetLetsGo bet1;
    private ShinyBetLetsGo bet2;
    private ShinyBetLetsGo bet3;

    @Test
    public void testGetAllByUsername()
    {
        List<ShinyBetLetsGo> bets1 = shinyBetDAO.getByUsername(user1.getUsername());

        assertEquals(bets1.size(), 2);


        List<ShinyBetLetsGo> bets2 = shinyBetDAO.getByUsername(user2.getUsername());

        assertEquals(bets2.size(), 1);
    }

    @Test
    public void testGetByUsernamePokemon()
    {
        ShinyBetLetsGo bet;

        bet = shinyBetDAO.getByUsernameAndPokemon(user1.getUsername(), BET1_POKEMON);
        assertNotNull(bet);
        assertEquals(bet1, bet);

        bet = shinyBetDAO.getByUsernameAndPokemon(user1.getUsername(), BET2_POKEMON);
        assertNotNull(bet);
        assertEquals(bet2, bet);

        bet = shinyBetDAO.getByUsernameAndPokemon(user2.getUsername(), BET3_POKEMON);
        assertNotNull(bet);
        assertEquals(bet3, bet);

        bet = shinyBetDAO.getByUsernameAndPokemon(user2.getUsername(), BET2_POKEMON);
        assertNull(bet);

        bet = shinyBetDAO.getByUsernameAndPokemon(user1.getUsername(), OTHER_BET);
        assertNull(bet);

        bet = shinyBetDAO.getByUsernameAndPokemon(user2.getUsername(), OTHER_BET);
        assertNull(bet);
    }


    @Override
    protected void before() throws Exception
    {
        shinyBetDAO = database().get(ShinyBetLetsGoDAO.class);

        createTestObjects();
    }

    @Override
    protected void after() throws Exception
    {
        removetTestObjects();
    }

    public void createTestObjects()
    {
        user1 = new User();
        user1.setUsername(USERNAME1);

        user2 = new User();
        user2.setUsername(USERNAME2);

        bet1 = new ShinyBetLetsGo();
        bet1.setUser(user1);
        bet1.setPokemonId(BET1_POKEMON);
        bet1.setBetAmount(BET1_AMOUNT);

        bet2 = new ShinyBetLetsGo();
        bet2.setUser(user1);
        bet2.setPokemonId(BET2_POKEMON);
        bet2.setBetAmount(BET2_AMOUNT);

        bet3 = new ShinyBetLetsGo();
        bet3.setUser(user2);
        bet3.setPokemonId(BET3_POKEMON);
        bet3.setBetAmount(BET3_AMOUNT);

        database().save(bet1, bet2, bet3, user1, user2);
    }

    public void removetTestObjects()
    {
        database().delete(bet1, bet2, bet3, user1, user2);
    }
}