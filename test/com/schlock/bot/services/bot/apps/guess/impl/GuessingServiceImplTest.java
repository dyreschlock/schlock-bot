package com.schlock.bot.services.bot.apps.guess.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.entities.apps.pokemon.PokemonUtils;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.StandaloneDatabase;
import com.schlock.bot.services.bot.apps.impl.UserServiceImpl;
import com.schlock.bot.services.bot.apps.pokemon.PokemonService;
import com.schlock.bot.services.bot.apps.pokemon.impl.PokemonServiceImpl;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.apps.UserDAO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GuessingServiceImplTest extends DatabaseTest
{
    private final static String USERNAME1 = "username1";
    private final static Integer DEFAULT_BALANCE = 10000;

    private static final String TEST_POKEMON_NAME = "Bulbasaur";
    private static final String TEST_POKEMON_ID = "bulbasaur";
    private static final Integer TEST_POKEMON_NUMBER = 1;
    private static final String TEST_POKEMON_TYPE1 = "Grass";
    private static final String TEST_POKEMON_TYPE2 = "Poison";

    private UserServiceImpl userService;

    private GuessingServiceImpl impl;

    private User testUser1;

    private Pokemon testPokemon = new Pokemon();

    @Test
    public void testUsecase()
    {
        final String ADMIN = getDeploymentConfiguration().getOwnerUsername();

        final String MARK = getDeploymentConfiguration().getCurrencyMark();
        final String POINTS = getDeploymentConfiguration().getQuizCorrectPoints().toString();


        //start the game
        String response = impl.processSingleResult(ADMIN, "!whodat");
        String expected = PokemonUtils.formatHint1(testPokemon);

        assertEquals(expected, response);

        //try to start the game, again
        response = impl.processSingleResult(ADMIN, "!whodat");
        expected = GuessingServiceImpl.GAME_ALREADY_STARTED + expected;

        assertEquals(expected, response);

        //send a message that doesn't contain the answer
        response = impl.processSingleResult(ADMIN, "asdf");

        assertNull(response);

        //send a message that contains the answer
        response = impl.processSingleResult(ADMIN, TEST_POKEMON_ID);
        expected = String.format(GuessingServiceImpl.WINNER_MESSAGE, ADMIN, TEST_POKEMON_NAME, POINTS, MARK);

        assertEquals(expected, response);

        User admin = userService.getUser(ADMIN);

        Integer currentBalance = admin.getBalance();
        Integer expectedBalance = getDeploymentConfiguration().getUserDefaultBalance() + getDeploymentConfiguration().getQuizCorrectPoints();

        assertEquals(expectedBalance, currentBalance);

        //send a message that contains the answer, again
        response = impl.processSingleResult(ADMIN, TEST_POKEMON_ID);

        assertNull(response);


        //start a new game
        response = impl.processSingleResult(ADMIN, "!whodat");
        expected = PokemonUtils.formatHint1(testPokemon);

        assertEquals(expected, response);

        //send response with answer from user
        response = impl.processSingleResult(USERNAME1, " asdf " + TEST_POKEMON_ID + " asdf ");
        expected = String.format(GuessingServiceImpl.WINNER_MESSAGE, USERNAME1, TEST_POKEMON_NAME, POINTS, MARK);

        assertEquals(expected, response);

        testUser1 = userService.getUser(USERNAME1);

        currentBalance = testUser1.getBalance();
        expectedBalance = DEFAULT_BALANCE + getDeploymentConfiguration().getQuizCorrectPoints();

        assertEquals(currentBalance, expectedBalance);

    }

    @BeforeEach
    public void setup() throws Exception
    {
        setupDatabase();
        setupServices();
        createTestObjects();
    }

    @AfterEach
    public void teardown() throws Exception
    {
        removeTestObjects();
    }

    private void setupServices()
    {
        DeploymentConfiguration config = getDeploymentConfiguration();
        StandaloneDatabase database = getDatabase();

        PokemonService pokemonService = new PokemonServiceImpl(config)
        {
            public Pokemon getRandomPokemon()
            {
                return testPokemon;
            }

            public Pokemon getRandomPokemonInGen(String gen)
            {
                return testPokemon;
            }

            public Pokemon getRandomPokemonInRange(String rangeText)
            {
                return testPokemon;
            }
        };

        userService = new UserServiceImpl(database.get(UserDAO.class), config);

        impl = new GuessingServiceImpl(pokemonService, userService, database.get(UserDAO.class), config);
    }

    private void createTestObjects()
    {
        testUser1 = new User();
        testUser1.setUsername(USERNAME1);
        testUser1.setBalance(DEFAULT_BALANCE);

        getDatabase().save(testUser1);

        testPokemon.setName(TEST_POKEMON_NAME);
        testPokemon.setId(TEST_POKEMON_ID);
        testPokemon.setNumber(TEST_POKEMON_NUMBER);
        testPokemon.setType1(TEST_POKEMON_TYPE1);
        testPokemon.setType2(TEST_POKEMON_TYPE2);
    }

    private void removeTestObjects()
    {
        getDatabase().delete(testUser1);

        final String ADMIN = getDeploymentConfiguration().getOwnerUsername();
        User admin = getDatabase().get(UserDAO.class).getByUsername(ADMIN);

        getDatabase().delete(admin);
    }
}