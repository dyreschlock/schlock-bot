package com.schlock.bot.services.apps.bet.impl;

import com.schlock.bot.entities.Persisted;
import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.bet.ShinyBet;
import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.services.DatabaseModule;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.apps.UserService;
import com.schlock.bot.services.apps.impl.UserServiceImpl;
import com.schlock.bot.services.apps.pokemon.PokemonService;
import com.schlock.bot.services.apps.pokemon.impl.PokemonServiceImpl;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.apps.ShinyBetDAO;
import com.schlock.bot.services.database.apps.UserDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ShinyBetServiceImplTest extends DatabaseTest
{
    private static final String USERNAME1 = "username1";
    private static final Integer BALANCE = 10000;

    private static final String BET1_POKEMON = "beedrill";
    private static final Integer BET1_MINUTES = 100;
    private static final Integer BET1_AMOUNT = 100;

    private static final String BET2_POKEMON = "magikarp";
    private static final Integer BET2_MINUTES = 100;
    private static final Integer BET2_AMOUNT = 100;


    private PokemonService pokemonService;

    private ShinyBetServiceImpl impl;

    private User user;


    @Test
    public void testNewBet()
    {
        String newBet = "!bet "+ BET2_POKEMON +" "+ BET2_MINUTES +" " + BET2_AMOUNT;

        String response = impl.processSingleResults(USERNAME1, newBet);

        String mark = getDeploymentContext().getCurrencyMark();
        Pokemon pokemon = pokemonService.getPokemonFromText(BET2_POKEMON);

        String expected = String.format(ShinyBetServiceImpl.BET_SUCCESS, USERNAME1, pokemon.getName(), BET2_MINUTES.toString(), BET2_AMOUNT.toString(), mark);

        assertEquals(expected, response);

        user = getDatabase().get(UserDAO.class).getByUsername(user.getUsername());

        Integer newBalance = BALANCE - BET2_AMOUNT;

        assertEquals(newBalance, user.getBalance());
    }

    @Test
    public void testUpdateBet()
    {
        List bets = getDatabase().get(ShinyBetDAO.class).getByUsername(user.getUsername());

        assertEquals(1, bets.size());





        bets = getDatabase().get(ShinyBetDAO.class).getByUsername(user.getUsername());

        assertEquals(1, bets.size());
    }

    @Test
    public void testCurrentBets()
    {
        String mark = getDeploymentContext().getCurrencyMark();

        List<String> responses = impl.process(USERNAME1, "!currentbets");

        assertEquals(1, responses.size());

        Pokemon pokemon1 = pokemonService.getPokemonFromText(BET1_POKEMON);

        String response = responses.get(0);
        String expected1 = String.format(ShinyBetServiceImpl.BET_FORMAT,
                                                                USERNAME1,
                                                                pokemon1.getName(),
                                                                BET1_MINUTES,
                                                                BET1_AMOUNT,
                                                                mark);

        assertEquals(expected1, response);


        String newBet = "!bet "+ BET2_POKEMON +" "+ BET2_MINUTES +" " + BET2_AMOUNT;
        impl.processSingleResults(USERNAME1, newBet);

        Pokemon pokemon2 = pokemonService.getPokemonFromText(BET2_POKEMON);

        responses = impl.process(USERNAME1, "!currentbets");

        assertEquals(2, responses.size());

        String expected2 = String.format(ShinyBetServiceImpl.BET_FORMAT,
                                                                USERNAME1,
                                                                pokemon2.getName(),
                                                                BET2_MINUTES,
                                                                BET2_AMOUNT,
                                                                mark);

        assertTrue(responses.contains(expected1));
        assertTrue(responses.contains(expected2));
    }

    @Test
    public void testCancelAllBets()
    {
        List bets = getDatabase().get(ShinyBetDAO.class).getByUsername(user.getUsername());

        assertEquals(1, bets.size());


        String response = impl.processSingleResults(USERNAME1, "!cancelallbets");
        String expected = String.format(ShinyBetServiceImpl.ALL_BETS_CANCELED, USERNAME1);

        assertEquals(expected, response);


        bets = getDatabase().get(ShinyBetDAO.class).getByUsername(user.getUsername());

        assertEquals(0, bets.size());


        user = getDatabase().get(UserDAO.class).getByUsername(USERNAME1);
        Integer newBalance = BALANCE + BET1_AMOUNT;

        assertEquals(newBalance, user.getBalance());


        response = impl.processSingleResults(USERNAME1, "!cancelallbets");
        expected = String.format(ShinyBetServiceImpl.ALL_BETS_CANCELED, USERNAME1);

        assertEquals(expected, response);
    }

    @Test
    public void testCancelBet()
    {
        final String CANCEL_BET1 = "!cancelbet " + BET1_POKEMON;
        final String CANCEL_BET2 = "!cancelbet " + BET2_POKEMON;

        Pokemon pokemon1 = pokemonService.getPokemonFromText(BET1_POKEMON);

        String response = impl.processSingleResults(USERNAME1, CANCEL_BET1);
        String expected = String.format(ShinyBetServiceImpl.BET_CANCELED, pokemon1.getName(), USERNAME1);

        assertEquals(expected, response);


        user = getDatabase().get(UserDAO.class).getByUsername(USERNAME1);
        Integer newBalance = BALANCE + BET1_AMOUNT;

        assertEquals(newBalance, user.getBalance());


        Pokemon pokemon2 = pokemonService.getPokemonFromText(BET2_POKEMON);

        response = impl.processSingleResults(USERNAME1, CANCEL_BET2);
        expected = String.format(ShinyBetServiceImpl.BET_CANCEL_NO_BET, USERNAME1, pokemon2.getName());

        assertEquals(expected, response);
    }


    @BeforeEach
    public void setup() throws Exception
    {
        setupDatabase();
        createTestObjects();

        DeploymentContext context = getDeploymentContext();
        DatabaseModule database = getDatabase();

        pokemonService = new PokemonServiceImpl(context);
        UserService userService = new UserServiceImpl(database, context);

        impl = new ShinyBetServiceImpl(pokemonService, userService, database, context);
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

        List<Persisted> objects = Arrays.asList(user, bet);
        getDatabase().save(objects);
    }

    @AfterEach
    public void tearDown() throws Exception
    {
        removeTestObjects();
    }

    private void removeTestObjects()
    {
        List<ShinyBet> bets = getDatabase().get(ShinyBetDAO.class).getByUsername(user.getUsername());

        List<Persisted> objects = new ArrayList<>();
        objects.addAll(bets);
        objects.add(user);

        getDatabase().delete(objects);
    }

}