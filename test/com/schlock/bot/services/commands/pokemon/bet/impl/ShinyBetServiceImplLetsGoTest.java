package com.schlock.bot.services.commands.pokemon.bet.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.entities.pokemon.ShinyBetLetsGo;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.ListenerService;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.base.UserDAO;
import com.schlock.bot.services.database.pokemon.ShinyBetLetsGoDAO;
import com.schlock.bot.services.entities.base.UserManagement;
import com.schlock.bot.services.entities.base.impl.UserManagementImpl;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.PokemonUtils;
import com.schlock.bot.services.entities.pokemon.ShinyBetFormatter;
import com.schlock.bot.services.entities.pokemon.impl.PokemonManagementImpl;
import com.schlock.bot.services.entities.pokemon.impl.PokemonUtilsImpl;
import com.schlock.bot.services.entities.pokemon.impl.ShinyBetFormatterImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ShinyBetServiceImplLetsGoTest extends DatabaseTest
{
    private static final String OPEN_BETS_LETSGO = "!openbets letsgo";

    private static final String USERNAME1 = "username1";
    private static final Long BALANCE = Long.valueOf(10000);

    private static final String BET1_POKEMON = "beedrill";
    private static final Integer BET1_MINUTES = 100;
    private static final Integer BET1_NEW_MINUTES = 50;
    private static final Integer BET1_AMOUNT = 100;
    private static final Integer BET1_NEW_AMOUNT = 200;
    private static final Integer BET1_NEW2_AMOUNT = BALANCE.intValue() + BET1_AMOUNT;
    private static final Integer BET1_NEW3_AMOUNT = 200000;

    private static final String BET2_POKEMON = "magikarp";
    private static final Integer BET2_MINUTES = 100;
    private static final Integer BET2_AMOUNT = 100;

    private static final String BET3_POKEMON = "zapdos";
    private static final Integer BET3_MINUTES = 100;
    private static final Integer BET3_AMOUNT = 200000;

    private PokemonManagement pokemonManagement;
    private UserManagement userManagement;

    private ShinyBetServiceImpl impl;

    private User user;


    @Test
    public void testNoBet()
    {
        impl.openBetting(OPEN_BETS_LETSGO);

        String newBet = "!bet";

        ListenerResponse resp = impl.process(USERNAME1, newBet);
        String response = resp.getFirstMessage();
        String expected = messages().format(ShinyBetServiceImpl.BET_LETSGO_WRONG_FORMAT_KEY);

        assertFalse(resp.isRelayAll());
        assertEquals(expected, response);
    }

    @Test
    public void testNewBetLetsGo()
    {
        impl.openBetting(OPEN_BETS_LETSGO);

        final String MARK = config().getCurrencyMark();
        Pokemon pokemon2 = pokemonManagement.getPokemonFromText(BET2_POKEMON);


        String newBet = "!bet " + BET2_POKEMON + " " + BET2_MINUTES + " " + BET2_AMOUNT;

        ListenerResponse resp = impl.process(USERNAME1, newBet);
        String response = resp.getFirstMessage();
        String expected = messages().format(ShinyBetServiceImpl.BET_LETSGO_SUCCESS_KEY, USERNAME1, pokemon2.getName(), BET2_MINUTES.toString(), BET2_AMOUNT.toString(), MARK);

        assertTrue(resp.isRelayAll());
        assertEquals(expected, response);

        user = database().get(UserDAO.class).getByUsername(user.getUsername());
        Long newBalance = BALANCE - BET2_AMOUNT;

        assertEquals(newBalance, user.getBalance());


        newBet = "!bet " + BET3_POKEMON + " " + BET3_MINUTES + " " + BET3_AMOUNT;

        resp = impl.process(USERNAME1, newBet);
        response = resp.getFirstMessage();
        expected = messages().format(ShinyBetServiceImpl.INSUFFICIENT_FUNDS_KEY, MARK, user.getBalance().toString(), MARK);

        assertFalse(resp.isRelayAll());
        assertEquals(expected, response);


        newBet = "!bet " + BET3_POKEMON + " " + BET3_MINUTES + " 0";

        resp = impl.process(USERNAME1, newBet);
        response = resp.getFirstMessage();
        expected = messages().format(ShinyBetServiceImpl.BET_AMOUNT_NEGATIVE, MARK);

        assertFalse(resp.isRelayAll());
        assertEquals(expected, response);


        newBet = "!bet " + BET3_POKEMON + " " + "-1" + " " + BET2_AMOUNT;

        resp = impl.process(USERNAME1, newBet);
        response = resp.getFirstMessage();
        expected = messages().format(ShinyBetServiceImpl.BET_TIME_NOT_ENOUGH);

        assertFalse(resp.isRelayAll());
        assertEquals(expected, response);
    }

    @Test
    public void testUpdateBet()
    {
        impl.openBetting(OPEN_BETS_LETSGO);

        final String MARK = config().getCurrencyMark();
        Pokemon pokemon1 = pokemonManagement.getPokemonFromText(BET1_POKEMON);

        List bets = database().get(ShinyBetLetsGoDAO.class).getByUsername(user.getUsername());

        assertEquals(1, bets.size());

        String newBet = "!bet " + BET1_POKEMON + " " + BET1_NEW_MINUTES + " " + BET1_NEW_AMOUNT;

        ListenerResponse resp = impl.process(USERNAME1, newBet);
        String response = resp.getFirstMessage();
        String expected = messages().format(ShinyBetServiceImpl.BET_LETSGO_UPDATE_SUCCESS_KEY, USERNAME1, pokemon1.getName(), BET1_NEW_MINUTES, BET1_NEW_AMOUNT, MARK);

        assertTrue(resp.isRelayAll());
        assertEquals(expected, response);

        bets = database().get(ShinyBetLetsGoDAO.class).getByUsername(user.getUsername());
        assertEquals(1, bets.size());


        newBet = "!bet " + BET1_POKEMON + " " + BET1_NEW_MINUTES + " " + BET1_NEW2_AMOUNT;

        resp = impl.process(USERNAME1, newBet);
        response = resp.getFirstMessage();
        expected = messages().format(ShinyBetServiceImpl.BET_LETSGO_UPDATE_SUCCESS_KEY, USERNAME1, pokemon1.getName(), BET1_NEW_MINUTES, BET1_NEW2_AMOUNT, MARK);

        assertTrue(resp.isRelayAll());
        assertEquals(expected, response);

        bets = database().get(ShinyBetLetsGoDAO.class).getByUsername(user.getUsername());
        assertEquals(1, bets.size());


        newBet = "!bet " + BET1_POKEMON + " " + BET1_NEW_MINUTES + " " + BET1_NEW3_AMOUNT;

        resp = impl.process(USERNAME1, newBet);
        response = resp.getFirstMessage();
        expected = messages().format(ShinyBetServiceImpl.UPDATE_INSUFFICIENT_FUNDS_KEY, MARK, "0", MARK, BET1_NEW2_AMOUNT.toString(), MARK);

        assertFalse(resp.isRelayAll());
        assertEquals(expected, response);

        bets = database().get(ShinyBetLetsGoDAO.class).getByUsername(user.getUsername());
        assertEquals(1, bets.size());
    }

    @Test
    public void testCurrentBets()
    {
        impl.openBetting(OPEN_BETS_LETSGO);

        final String MARK = config().getCurrencyMark();
        Pokemon pokemon1 = pokemonManagement.getPokemonFromText(BET1_POKEMON);
        Pokemon pokemon2 = pokemonManagement.getPokemonFromText(BET2_POKEMON);


        ListenerResponse resp = impl.process(USERNAME1, "!currentbets");
        List<String> responses = resp.getMessages();

        assertFalse(resp.isRelayAll());
        assertEquals(1, responses.size());


        String response = responses.get(0);
        String expected1 = messages().format(ShinyBetFormatterImpl.CURRENT_BET_LETSGO_KEY,
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

        String expected2 = messages().format(ShinyBetFormatterImpl.CURRENT_BET_LETSGO_KEY,
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
        impl.openBetting(OPEN_BETS_LETSGO);

        List bets = database().get(ShinyBetLetsGoDAO.class).getByUsername(user.getUsername());

        assertEquals(1, bets.size());


        ListenerResponse resp = impl.process(USERNAME1, "!cancelallbets");
        String response = resp.getFirstMessage();
        String expected = messages().format(ShinyBetServiceImpl.ALL_BETS_CANCELED_KEY, USERNAME1);

        assertTrue(resp.isRelayAll());
        assertEquals(expected, response);


        bets = database().get(ShinyBetLetsGoDAO.class).getByUsername(user.getUsername());

        assertEquals(0, bets.size());


        user = database().get(UserDAO.class).getByUsername(USERNAME1);
        Long newBalance = BALANCE + BET1_AMOUNT;

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
        impl.openBetting(OPEN_BETS_LETSGO);

        final String CANCEL_BET1 = "!cancelbet " + BET1_POKEMON;
        final String CANCEL_BET2 = "!cancelbet " + BET2_POKEMON;

        Pokemon pokemon1 = pokemonManagement.getPokemonFromText(BET1_POKEMON);

        ListenerResponse resp = impl.process(USERNAME1, CANCEL_BET1);
        String response = resp.getFirstMessage();
        String expected = messages().format(ShinyBetServiceImpl.BET_LETSGO_CANCELED_KEY, pokemon1.getName(), USERNAME1);

        assertTrue(resp.isRelayAll());
        assertEquals(expected, response);


        user = database().get(UserDAO.class).getByUsername(USERNAME1);
        Long newBalance = BALANCE + BET1_AMOUNT;

        assertEquals(newBalance, user.getBalance());


        Pokemon pokemon2 = pokemonManagement.getPokemonFromText(BET2_POKEMON);

        resp = impl.process(USERNAME1, CANCEL_BET2);
        response = resp.getFirstMessage();
        expected = messages().format(ShinyBetServiceImpl.BET_CANCEL_LETSGO_NO_BET_KEY, USERNAME1, pokemon2.getName());

        assertFalse(resp.isRelayAll());
        assertEquals(expected, response);
    }

    @Test
    public void testOpenAndCloseBetting()
    {
        impl.openBetting(OPEN_BETS_LETSGO);

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

        resp = impl.process(USERNAME1, OPEN_BETS_LETSGO);
        response = resp.getFirstMessage();
        expected = messages().get(ListenerService.NOT_ADMIN_KEY);

        assertFalse(resp.isRelayAll());
        assertEquals(expected, response);

        resp = impl.process(ADMIN, OPEN_BETS_LETSGO);
        response = resp.getFirstMessage();
        expected = messages().get(ShinyBetServiceImpl.BETS_NOW_OPEN_LETSGO_KEY);

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

        userManagement = new UserManagementImpl(database(), config());

        ShinyBetFormatter betFormatter = new ShinyBetFormatterImpl(pokemonManagement, messages(), config());

        impl = new ShinyBetServiceImpl(pokemonManagement, userManagement, betFormatter, database(), messages(), config());

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

        ShinyBetLetsGo bet = new ShinyBetLetsGo();
        bet.setUser(user);
        bet.setPokemonId(BET1_POKEMON);
        bet.setTimeMinutes(BET1_MINUTES);
        bet.setBetAmount(BET1_AMOUNT);

        database().save(user, bet);
    }

    @AfterEach
    public void tearDown() throws Exception
    {
        removeTestObjects();
    }

    private void removeTestObjects()
    {
        List<ShinyBetLetsGo> bets = database().get(ShinyBetLetsGoDAO.class).getByUsername(user.getUsername());

        for(ShinyBetLetsGo bet : bets)
        {
            database().delete(bet);
        }
        database().delete(user);
    }
}