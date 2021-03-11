package com.schlock.bot.services.commands.pokemon.bet.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.entities.pokemon.ShinyBet;
import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.ListenerService;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.PokemonUtils;
import com.schlock.bot.services.entities.pokemon.ShinyBetFormatter;
import com.schlock.bot.services.entities.pokemon.impl.PokemonManagementImpl;
import com.schlock.bot.services.entities.pokemon.impl.PokemonUtilsImpl;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.pokemon.ShinyBetDAO;
import com.schlock.bot.services.database.base.UserDAO;
import com.schlock.bot.services.database.pokemon.impl.ShinyBetDAOImpl;
import com.schlock.bot.services.database.base.impl.UserDAOImpl;
import com.schlock.bot.services.entities.base.UserManagement;
import com.schlock.bot.services.entities.base.impl.UserManagementImpl;
import com.schlock.bot.services.entities.pokemon.impl.ShinyBetFormatterImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ShinyBetServiceImplTest extends DatabaseTest
{
    private static final String USERNAME1 = "username1";
    private static final Integer BALANCE = 10000;

    private static final String BET1_POKEMON = "beedrill";
    private static final Integer BET1_MINUTES = 100;
    private static final Integer BET1_NEW_MINUTES = 50;
    private static final Integer BET1_AMOUNT = 100;
    private static final Integer BET1_NEW_AMOUNT = 200;
    private static final Integer BET1_NEW2_AMOUNT = BALANCE + BET1_AMOUNT;
    private static final Integer BET1_NEW3_AMOUNT = 200000;

    private static final String BET2_POKEMON = "magikarp";
    private static final Integer BET2_MINUTES = 100;
    private static final Integer BET2_AMOUNT = 100;

    private static final String BET3_POKEMON = "zapdos";
    private static final Integer BET3_MINUTES = 100;
    private static final Integer BET3_AMOUNT = 200000;

    private PokemonManagement pokemonManagement;
    private UserManagement userManagement;

    private ShinyBetDAO shinyBetDAO;
    private UserDAO userDAO;

    private ShinyBetServiceImpl impl;

    private User user;


    @Test
    public void testNewBet()
    {
        final String MARK = config().getCurrencyMark();
        Pokemon pokemon2 = pokemonManagement.getPokemonFromText(BET2_POKEMON);


        String newBet = "!bet " + BET2_POKEMON + " " + BET2_MINUTES + " " + BET2_AMOUNT;

        ListenerResponse resp = impl.process(USERNAME1, newBet);
        String response = resp.getFirstMessage();
        String expected = messages().format(ShinyBetServiceImpl.BET_SUCCESS_KEY, USERNAME1, pokemon2.getName(), BET2_MINUTES.toString(), BET2_AMOUNT.toString(), MARK);

        assertTrue(resp.isRelayAll());
        assertEquals(expected, response);

        user = userDAO.getByUsername(user.getUsername());
        Integer newBalance = BALANCE - BET2_AMOUNT;

        assertEquals(newBalance, user.getBalance());


        newBet = "!bet " + BET3_POKEMON + " " + BET3_MINUTES + " " + BET3_AMOUNT;

        resp = impl.process(USERNAME1, newBet);
        response = resp.getFirstMessage();
        expected = messages().format(ShinyBetServiceImpl.INSUFFICIENT_FUNDS_KEY, MARK, user.getBalance().toString(), MARK);

        assertFalse(resp.isRelayAll());
        assertEquals(expected, response);
    }

    @Test
    public void testUpdateBet()
    {
        final String MARK = config().getCurrencyMark();
        Pokemon pokemon1 = pokemonManagement.getPokemonFromText(BET1_POKEMON);

        List bets = shinyBetDAO.getByUsername(user.getUsername());

        assertEquals(1, bets.size());

        String newBet = "!bet " + BET1_POKEMON + " " + BET1_NEW_MINUTES + " " + BET1_NEW_AMOUNT;

        ListenerResponse resp = impl.process(USERNAME1, newBet);
        String response = resp.getFirstMessage();
        String expected = messages().format(ShinyBetServiceImpl.BET_UPDATE_SUCCESS_KEY, USERNAME1, pokemon1.getName(), BET1_NEW_MINUTES, BET1_NEW_AMOUNT, MARK);

        assertTrue(resp.isRelayAll());
        assertEquals(expected, response);

        bets = shinyBetDAO.getByUsername(user.getUsername());
        assertEquals(1, bets.size());


        newBet = "!bet " + BET1_POKEMON + " " + BET1_NEW_MINUTES + " " + BET1_NEW2_AMOUNT;

        resp = impl.process(USERNAME1, newBet);
        response = resp.getFirstMessage();
        expected = messages().format(ShinyBetServiceImpl.BET_UPDATE_SUCCESS_KEY, USERNAME1, pokemon1.getName(), BET1_NEW_MINUTES, BET1_NEW2_AMOUNT, MARK);

        assertTrue(resp.isRelayAll());
        assertEquals(expected, response);

        bets = shinyBetDAO.getByUsername(user.getUsername());
        assertEquals(1, bets.size());


        newBet = "!bet " + BET1_POKEMON + " " + BET1_NEW_MINUTES + " " + BET1_NEW3_AMOUNT;

        resp = impl.process(USERNAME1, newBet);
        response = resp.getFirstMessage();
        expected = messages().format(ShinyBetServiceImpl.UPDATE_INSUFFICIENT_FUNDS_KEY, MARK, "0", MARK, BET1_NEW2_AMOUNT.toString(), MARK);

        assertFalse(resp.isRelayAll());
        assertEquals(expected, response);

        bets = shinyBetDAO.getByUsername(user.getUsername());
        assertEquals(1, bets.size());
    }

    @Test
    public void testCurrentBets()
    {
        final String MARK = config().getCurrencyMark();
        Pokemon pokemon1 = pokemonManagement.getPokemonFromText(BET1_POKEMON);
        Pokemon pokemon2 = pokemonManagement.getPokemonFromText(BET2_POKEMON);


        ListenerResponse resp = impl.process(USERNAME1, "!currentbets");
        List<String> responses = resp.getMessages();

        assertFalse(resp.isRelayAll());
        assertEquals(1, responses.size());


        String response = responses.get(0);
        String expected1 = messages().format(ShinyBetServiceImpl.CURRENT_BET_KEY,
                                                                USERNAME1,
                                                                pokemon1.getName(),
                                                                BET1_MINUTES,
                                                                BET1_AMOUNT,
                                                                MARK);

        assertEquals(expected1, response);


        String newBet = "!bet "+ BET2_POKEMON +" "+ BET2_MINUTES +" " + BET2_AMOUNT;
        impl.process(USERNAME1, newBet);


        resp = impl.process(USERNAME1, "!currentbets");
        responses = resp.getMessages();

        assertFalse(resp.isRelayAll());
        assertEquals(2, responses.size());

        String expected2 = messages().format(ShinyBetServiceImpl.CURRENT_BET_KEY,
                                                                USERNAME1,
                                                                pokemon2.getName(),
                                                                BET2_MINUTES,
                                                                BET2_AMOUNT,
                                                                MARK);

        assertTrue(responses.contains(expected1));
        assertTrue(responses.contains(expected2));
    }

    @Test
    public void testCancelAllBets()
    {
        List bets = shinyBetDAO.getByUsername(user.getUsername());

        assertEquals(1, bets.size());


        ListenerResponse resp = impl.process(USERNAME1, "!cancelallbets");
        String response = resp.getFirstMessage();
        String expected = messages().format(ShinyBetServiceImpl.ALL_BETS_CANCELED_KEY, USERNAME1);

        assertTrue(resp.isRelayAll());
        assertEquals(expected, response);


        bets = shinyBetDAO.getByUsername(user.getUsername());

        assertEquals(0, bets.size());


        user = userDAO.getByUsername(USERNAME1);
        Integer newBalance = BALANCE + BET1_AMOUNT;

        assertEquals(newBalance, user.getBalance());


        resp = impl.process(USERNAME1, "!cancelallbets");
        response = resp.getFirstMessage();
        expected = messages().format(ShinyBetServiceImpl.NO_CURRENT_BETS_KEY, USERNAME1);

        assertFalse(resp.isRelayAll());
        assertEquals(expected, response);
    }

    @Test
    public void testCancelBet()
    {
        final String CANCEL_BET1 = "!cancelbet " + BET1_POKEMON;
        final String CANCEL_BET2 = "!cancelbet " + BET2_POKEMON;

        Pokemon pokemon1 = pokemonManagement.getPokemonFromText(BET1_POKEMON);

        ListenerResponse resp = impl.process(USERNAME1, CANCEL_BET1);
        String response = resp.getFirstMessage();
        String expected = messages().format(ShinyBetServiceImpl.BET_CANCELED_KEY, pokemon1.getName(), USERNAME1);

        assertTrue(resp.isRelayAll());
        assertEquals(expected, response);


        user = userDAO.getByUsername(USERNAME1);
        Integer newBalance = BALANCE + BET1_AMOUNT;

        assertEquals(newBalance, user.getBalance());


        Pokemon pokemon2 = pokemonManagement.getPokemonFromText(BET2_POKEMON);

        resp = impl.process(USERNAME1, CANCEL_BET2);
        response = resp.getFirstMessage();
        expected = messages().format(ShinyBetServiceImpl.BET_CANCEL_NO_BET_KEY, USERNAME1, pokemon2.getName());

        assertFalse(resp.isRelayAll());
        assertEquals(expected, response);
    }

    @Test
    public void testOpenAndCloseBetting()
    {
        final String OPEN_BETS = "!openbets";
        final String CLOSE_BETS = "!closebets";

        final String ADMIN = config().getOwnerUsername();

        String bet = "!bet " + BET1_POKEMON + " " + BET1_MINUTES + " " + BET1_AMOUNT;

        ListenerResponse resp = impl.process(USERNAME1, bet);
        String response = resp.getFirstMessage();
        String not_expected = messages().get(ShinyBetServiceImpl.BETS_ARE_CLOSED_KEY);

        assertTrue(resp.isRelayAll());
        assertNotEquals(not_expected, response);

        resp = impl.process(USERNAME1, CLOSE_BETS);
        response = resp.getFirstMessage();
        String expected = messages().get(ListenerService.NOT_ADMIN_KEY);

        assertFalse(resp.isRelayAll());
        assertEquals(expected, response);

        resp = impl.process(ADMIN, CLOSE_BETS);
        response = resp.getFirstMessage();
        expected = messages().get(ShinyBetServiceImpl.BETS_NOW_CLOSED_KEY);

        assertTrue(resp.isRelayAll());
        assertEquals(expected, response);

        resp = impl.process(USERNAME1, bet);
        response = resp.getFirstMessage();
        expected = messages().get(ShinyBetServiceImpl.BETS_ARE_CLOSED_KEY);

        assertFalse(resp.isRelayAll());
        assertEquals(expected, response);

        resp = impl.process(USERNAME1, OPEN_BETS);
        response = resp.getFirstMessage();
        expected = messages().get(ListenerService.NOT_ADMIN_KEY);

        assertFalse(resp.isRelayAll());
        assertEquals(expected, response);

        resp = impl.process(ADMIN, OPEN_BETS);
        response = resp.getFirstMessage();
        expected = messages().get(ShinyBetServiceImpl.BETS_NOW_OPEN_KEY);

        assertTrue(resp.isRelayAll());
        assertEquals(expected, response);

        resp = impl.process(USERNAME1, bet);
        response = resp.getFirstMessage();
        not_expected = messages().get(ShinyBetServiceImpl.BETS_ARE_CLOSED_KEY);

        assertTrue(resp.isRelayAll());
        assertNotEquals(not_expected, response);
    }

    @Override
    protected void before() throws Exception
    {
        PokemonUtils pokemonUtils = new PokemonUtilsImpl(messages());

        pokemonManagement = new PokemonManagementImpl(pokemonUtils, config());

        shinyBetDAO = new ShinyBetDAOImpl(session)
        {
            public void commit()
            {
            }
        };
        userDAO = new UserDAOImpl(session)
        {
            public void commit()
            {
            }
        };

        userManagement = new UserManagementImpl(userDAO, config());

        ShinyBetFormatter betFormatter = new ShinyBetFormatterImpl(pokemonManagement, messages(), config());

        impl = new ShinyBetServiceImpl(pokemonManagement, userManagement, betFormatter, shinyBetDAO, userDAO, messages(), config());
        impl.openBetting();

        createTestObjects();
    }

    @Override
    protected void after() throws Exception
    {

    }

    private void createTestObjects()
    {
        user = userManagement.createNewDefaultUser(USERNAME1);
        user.setBalance(BALANCE);

        ShinyBet bet = new ShinyBet();
        bet.setUser(user);
        bet.setPokemonId(BET1_POKEMON);
        bet.setTimeMinutes(BET1_MINUTES);
        bet.setBetAmount(BET1_AMOUNT);

        userDAO.save(user, bet);
    }

    @AfterEach
    public void tearDown() throws Exception
    {
        removeTestObjects();
    }

    private void removeTestObjects()
    {
        List<ShinyBet> bets = shinyBetDAO.getByUsername(user.getUsername());

        for(ShinyBet bet : bets)
        {
            shinyBetDAO.delete(bet);
        }
        userDAO.delete(user);
    }
}