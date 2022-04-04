package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.entities.pokemon.ShinyBetHisui;
import com.schlock.bot.entities.pokemon.ShinyGetHisui;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.pokemon.ShinyBetHisuiDAO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShinyBetHisuiDAOImplTest extends DatabaseTest
{
    private static final String USERNAME1 = "username1";
    private static final Integer BET1_OUTBREAKS = 20;
    private static final Integer BET1_AMOUNT = 300;
    private static final String BET1_POKEMON = "turtwig";

    private static final String USERNAME2 = "username2";
    private static final Integer BET2_OUTBREAKS = 30;
    private static final Integer BET2_AMOUNT = 500;

    private static final Integer BET3_OUTBREAKS = 25;
    private static final Integer BET3_AMOUNT = 1000;
    private static final String BET3_POKEMON = "oshawott";



    private ShinyBetHisuiDAO shinyBetDAO;

    private User user1;
    private User user2;

    private ShinyBetHisui bet1;
    private ShinyBetHisui bet2;

    private ShinyBetHisui bet3;

    private ShinyGetHisui get3;

    @Test
    public void testGetByUsername()
    {
        ShinyBetHisui bets1 = shinyBetDAO.getByUsername(USERNAME1);

        assertEquals(BET1_AMOUNT, bets1.getBetAmount());
        assertEquals(BET1_POKEMON, bets1.getPokemonId());
        assertEquals(BET1_OUTBREAKS, bets1.getNumberOfChecks());

        ShinyBetHisui bets2 = shinyBetDAO.getByUsername(USERNAME2);

        assertEquals(BET2_AMOUNT, bets2.getBetAmount());
        assertEquals(BET2_OUTBREAKS, bets2.getNumberOfChecks());
    }

    @Test
    public void testGetAllCurrent()
    {
        List<ShinyBetHisui> bets = shinyBetDAO.getAllCurrent();

        assertEquals(2, bets.size());
    }

    @Override
    protected void before() throws Exception
    {
        shinyBetDAO = database().get(ShinyBetHisuiDAO.class);

        createTestObjects();
    }

    @Override
    protected void after() throws Exception
    {
        removeTestObjects();
    }

    public void createTestObjects()
    {
        user1 = new User();
        user1.setUsername(USERNAME1);

        user2 = new User();
        user2.setUsername(USERNAME2);

        bet1 = new ShinyBetHisui();
        bet1.setUser(user1);
        bet1.setBetAmount(BET1_AMOUNT);
        bet1.setNumberOfChecks(BET1_OUTBREAKS);
        bet1.setPokemonId(BET1_POKEMON);

        bet2 = new ShinyBetHisui();
        bet2.setUser(user2);
        bet2.setBetAmount(BET2_AMOUNT);
        bet2.setNumberOfChecks(BET2_OUTBREAKS);

        get3 = new ShinyGetHisui();
        get3.setOutbreakChecks(BET3_OUTBREAKS);
        get3.setPokemonId(BET3_POKEMON);
        get3.setHisuiNumber(4);

        bet3 = new ShinyBetHisui();
        bet3.setUser(user2);
        bet3.setBetAmount(BET3_AMOUNT);
        bet3.setNumberOfChecks(BET3_OUTBREAKS);
        bet3.setPokemonId(BET3_POKEMON);
        bet3.setShiny(get3);

        database().save(bet1, bet2, bet3, get3, user1, user2);
    }

    public void removeTestObjects()
    {
        database().delete(bet1, bet2, bet3, get3, user1, user2);
    }
}
