package com.schlock.bot.services.bot.apps.bet.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.bet.ShinyBet;
import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.services.bot.apps.ListenerService;
import com.schlock.bot.services.bot.apps.UserService;
import com.schlock.bot.services.bot.apps.impl.UserServiceImpl;
import com.schlock.bot.services.bot.apps.pokemon.PokemonService;
import com.schlock.bot.services.bot.apps.pokemon.PokemonUtils;
import com.schlock.bot.services.bot.apps.pokemon.impl.PokemonServiceImpl;
import com.schlock.bot.services.bot.apps.pokemon.impl.PokemonUtilsImpl;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.apps.ShinyBetDAO;
import com.schlock.bot.services.database.apps.UserDAO;
import com.schlock.bot.services.database.apps.impl.ShinyBetDAOImpl;
import com.schlock.bot.services.database.apps.impl.UserDAOImpl;
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

    private PokemonService pokemonService;

    private ShinyBetDAO shinyBetDAO;
    private UserDAO userDAO;

    private ShinyBetServiceImpl impl;

    private User user;


    @Test
    public void testNewBet()
    {
        final String MARK = config().getCurrencyMark();
        Pokemon pokemon2 = pokemonService.getPokemonFromText(BET2_POKEMON);


        String newBet = "!bet " + BET2_POKEMON + " " + BET2_MINUTES + " " + BET2_AMOUNT;

        String response = impl.processSingleResults(USERNAME1, newBet);
        String expected = messages().format(ShinyBetServiceImpl.BET_SUCCESS_KEY, USERNAME1, pokemon2.getName(), BET2_MINUTES.toString(), BET2_AMOUNT.toString(), MARK);

        assertEquals(expected, response);

        user = userDAO.getByUsername(user.getUsername());
        Integer newBalance = BALANCE - BET2_AMOUNT;

        assertEquals(newBalance, user.getBalance());


        newBet = "!bet " + BET3_POKEMON + " " + BET3_MINUTES + " " + BET3_AMOUNT;

        response = impl.processSingleResults(USERNAME1, newBet);
        expected = messages().format(ShinyBetServiceImpl.INSUFFICIENT_FUNDS_KEY, MARK, user.getBalance().toString(), MARK);

        assertEquals(expected, response);
    }

    @Test
    public void testUpdateBet()
    {
        final String MARK = config().getCurrencyMark();
        Pokemon pokemon1 = pokemonService.getPokemonFromText(BET1_POKEMON);

        List bets = shinyBetDAO.getByUsername(user.getUsername());

        assertEquals(1, bets.size());

        String newBet = "!bet " + BET1_POKEMON + " " + BET1_NEW_MINUTES + " " + BET1_NEW_AMOUNT;

        String response = impl.processSingleResults(USERNAME1, newBet);
        String expected = messages().format(ShinyBetServiceImpl.BET_UPDATE_SUCCESS_KEY, USERNAME1, pokemon1.getName(), BET1_NEW_MINUTES, BET1_NEW_AMOUNT, MARK);

        assertEquals(expected, response);

        bets = shinyBetDAO.getByUsername(user.getUsername());
        assertEquals(1, bets.size());


        newBet = "!bet " + BET1_POKEMON + " " + BET1_NEW_MINUTES + " " + BET1_NEW2_AMOUNT;

        response = impl.processSingleResults(USERNAME1, newBet);
        expected = messages().format(ShinyBetServiceImpl.BET_UPDATE_SUCCESS_KEY, USERNAME1, pokemon1.getName(), BET1_NEW_MINUTES, BET1_NEW2_AMOUNT, MARK);

        assertEquals(expected, response);

        bets = shinyBetDAO.getByUsername(user.getUsername());
        assertEquals(1, bets.size());


        newBet = "!bet " + BET1_POKEMON + " " + BET1_NEW_MINUTES + " " + BET1_NEW3_AMOUNT;

        response = impl.processSingleResults(USERNAME1, newBet);
        expected = messages().format(ShinyBetServiceImpl.UPDATE_INSUFFICIENT_FUNDS_KEY, MARK, "0", MARK, BET1_NEW2_AMOUNT.toString(), MARK);

        assertEquals(expected, response);

        bets = shinyBetDAO.getByUsername(user.getUsername());
        assertEquals(1, bets.size());
    }

    @Test
    public void testCurrentBets()
    {
        final String MARK = config().getCurrencyMark();
        Pokemon pokemon1 = pokemonService.getPokemonFromText(BET1_POKEMON);
        Pokemon pokemon2 = pokemonService.getPokemonFromText(BET2_POKEMON);


        List<String> responses = impl.process(USERNAME1, "!currentbets");

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
        impl.processSingleResults(USERNAME1, newBet);


        responses = impl.process(USERNAME1, "!currentbets");

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


        String response = impl.processSingleResults(USERNAME1, "!cancelallbets");
        String expected = messages().format(ShinyBetServiceImpl.ALL_BETS_CANCELED_KEY, USERNAME1);

        assertEquals(expected, response);


        bets = shinyBetDAO.getByUsername(user.getUsername());

        assertEquals(0, bets.size());


        user = userDAO.getByUsername(USERNAME1);
        Integer newBalance = BALANCE + BET1_AMOUNT;

        assertEquals(newBalance, user.getBalance());


        response = impl.processSingleResults(USERNAME1, "!cancelallbets");
        expected = messages().format(ShinyBetServiceImpl.ALL_BETS_CANCELED_KEY, USERNAME1);

        assertEquals(expected, response);
    }

    @Test
    public void testCancelBet()
    {
        final String CANCEL_BET1 = "!cancelbet " + BET1_POKEMON;
        final String CANCEL_BET2 = "!cancelbet " + BET2_POKEMON;

        Pokemon pokemon1 = pokemonService.getPokemonFromText(BET1_POKEMON);

        String response = impl.processSingleResults(USERNAME1, CANCEL_BET1);
        String expected = messages().format(ShinyBetServiceImpl.BET_CANCELED_KEY, pokemon1.getName(), USERNAME1);

        assertEquals(expected, response);


        user = userDAO.getByUsername(USERNAME1);
        Integer newBalance = BALANCE + BET1_AMOUNT;

        assertEquals(newBalance, user.getBalance());


        Pokemon pokemon2 = pokemonService.getPokemonFromText(BET2_POKEMON);

        response = impl.processSingleResults(USERNAME1, CANCEL_BET2);
        expected = messages().format(ShinyBetServiceImpl.BET_CANCEL_NO_BET_KEY, USERNAME1, pokemon2.getName());

        assertEquals(expected, response);
    }

    @Test
    public void testOpenAndCloseBetting()
    {
        final String OPEN_BETS = "!openbets";
        final String CLOSE_BETS = "!closebets";

        final String ADMIN = config().getOwnerUsername();

        String bet = "!bet " + BET1_POKEMON + " " + BET1_MINUTES + " " + BET1_AMOUNT;

        String response = impl.processSingleResults(USERNAME1, bet);
        String not_expected = messages().get(ShinyBetServiceImpl.BETS_ARE_CLOSED_KEY);

        assertNotEquals(not_expected, response);

        response = impl.processSingleResults(USERNAME1, CLOSE_BETS);
        String expected = messages().get(ListenerService.NOT_ADMIN_KEY);

        assertEquals(expected, response);

        response = impl.processSingleResults(ADMIN, CLOSE_BETS);
        expected = messages().get(ShinyBetServiceImpl.BETS_NOW_CLOSED_KEY);

        assertEquals(expected, response);

        response = impl.processSingleResults(USERNAME1, bet);
        expected = messages().get(ShinyBetServiceImpl.BETS_ARE_CLOSED_KEY);

        assertEquals(expected, response);

        response = impl.processSingleResults(USERNAME1, OPEN_BETS);
        expected = messages().get(ListenerService.NOT_ADMIN_KEY);

        assertEquals(expected, response);

        response = impl.processSingleResults(ADMIN, OPEN_BETS);
        expected = messages().get(ShinyBetServiceImpl.BETS_NOW_OPEN_KEY);

        assertEquals(expected, response);

        response = impl.processSingleResults(USERNAME1, bet);
        not_expected = messages().get(ShinyBetServiceImpl.BETS_ARE_CLOSED_KEY);

        assertNotEquals(not_expected, response);
    }

    @Override
    protected void before() throws Exception
    {
        PokemonUtils pokemonUtils = new PokemonUtilsImpl(messages());

        pokemonService = new PokemonServiceImpl(pokemonUtils, messages(), config());

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

        UserService userService = new UserServiceImpl(userDAO, messages(), config());

        impl = new ShinyBetServiceImpl(pokemonService, userService, shinyBetDAO, userDAO, messages(), config());
        impl.openBetting();

        createTestObjects();
    }

    @Override
    protected void after() throws Exception
    {

    }

    private void createTestObjects()
    {
        user = new User();
        user.setUsername(USERNAME1);
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