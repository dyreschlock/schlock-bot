package com.schlock.bot.services.commands.pokemon.whodat.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.entities.pokemon.GuessingStreak;
import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.services.database.pokemon.GuessingStreakDAO;
import com.schlock.bot.services.database.pokemon.impl.GuessingStreakDAOImpl;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.PokemonUtils;
import com.schlock.bot.services.entities.pokemon.impl.PokemonManagementImpl;
import com.schlock.bot.services.entities.pokemon.impl.PokemonUtilsImpl;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.base.UserDAO;
import com.schlock.bot.services.database.base.impl.UserDAOImpl;
import com.schlock.bot.services.entities.base.UserManagement;
import com.schlock.bot.services.entities.base.impl.UserManagementImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PokemonGuessingServiceImplTest extends DatabaseTest
{
    private final static String USERNAME1 = "username2";
    private final static Integer DEFAULT_BALANCE = 10000;

    private static final String TEST_POKEMON_NAME = "Bulbasaur";
    private static final String TEST_POKEMON_ID = "bulbasaur";
    private static final Integer TEST_POKEMON_NUMBER = 1;
    private static final String TEST_POKEMON_TYPE1 = "Grass";
    private static final String TEST_POKEMON_TYPE2 = "Poison";

    private UserManagement userManagement;
    private PokemonUtils pokemonUtils;

    private UserDAO userDAO;

    private PokemonGuessingServiceImpl impl;

    private GuessingStreak streak;

    private User testUser1;

    private Pokemon testPokemon = new Pokemon();

    @Test
    public void testUsecase()
    {
        final String ADMIN = config().getOwnerUsername();

        final String MARK = config().getCurrencyMark();
        final String POINTS = config().getQuizCorrectPoints().toString();
        final String DOUBLE_POINTS = Integer.valueOf(config().getQuizCorrectPoints() *2).toString();


        //start the game
        String response = impl.process(ADMIN, "!whodat").getFirstMessage();
        String expected = pokemonUtils.formatHint1(testPokemon);

        assertEquals(expected, response);

        //try to start the game, again
        response = impl.process(ADMIN, "!whodat").getFirstMessage();
        expected = messages().format(PokemonGuessingServiceImpl.GAME_ALREADY_STARTED_KEY, expected);

        assertEquals(expected, response);

        //send a message that doesn't contain the answer
        response = impl.process(ADMIN, "asdf").getFirstMessage();

        assertNull(response);

        //send a message that contains the answer
        response = impl.process(ADMIN, TEST_POKEMON_ID).getFirstMessage();
        expected = messages().format(PokemonGuessingServiceImpl.WINNER_KEY, ADMIN, TEST_POKEMON_NAME, POINTS, MARK);

        assertEquals(expected, response);

        User admin = userManagement.getUser(ADMIN);

        Integer currentBalance = admin.getBalance();
        Integer expectedBalance = config().getUserDefaultBalance() + config().getQuizCorrectPoints();

        assertEquals(expectedBalance, currentBalance);

        //send a message that contains the answer, again
        response = impl.process(ADMIN, TEST_POKEMON_ID).getFirstMessage();

        assertNull(response);


        //start a new game
        response = impl.process(ADMIN, "!whodat").getFirstMessage();
        expected = pokemonUtils.formatHint1(testPokemon);

        assertEquals(expected, response);

        //send response with answer from user
        response = impl.process(USERNAME1, " asdf " + TEST_POKEMON_ID + " asdf ").getFirstMessage();
        expected = messages().format(PokemonGuessingServiceImpl.WINNER_KEY, USERNAME1, TEST_POKEMON_NAME, POINTS, MARK);

        assertEquals(expected, response);

        testUser1 = userManagement.getUser(USERNAME1);

        currentBalance = testUser1.getBalance();
        expectedBalance = DEFAULT_BALANCE + config().getQuizCorrectPoints();

        assertEquals(currentBalance, expectedBalance);


        //start another new game
        response = impl.process(ADMIN, "!whodat").getFirstMessage();
        expected = pokemonUtils.formatHint1(testPokemon);

        assertEquals(expected, response);

        //send response with answer for the same user
        response = impl.process(USERNAME1, " asdf " + TEST_POKEMON_ID + " asdf ").getFirstMessage();
        expected = messages().format(PokemonGuessingServiceImpl.WINNER_KEY, USERNAME1, TEST_POKEMON_NAME, DOUBLE_POINTS, MARK);
        expected += " " + messages().format(PokemonGuessingServiceImpl.STREAK_BONUS, 2);

        assertEquals(expected, response);
    }

    @Override
    protected void before() throws Exception
    {
        pokemonUtils = new PokemonUtilsImpl(messages());

        PokemonManagement pokemonManagement = new PokemonManagementImpl(pokemonUtils, config())
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

        GuessingStreakDAO streakDAO = new GuessingStreakDAOImpl(session)
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

        impl = new PokemonGuessingServiceImpl(pokemonManagement, userManagement, pokemonUtils, streakDAO, userDAO, messages(), config());


        createTestObjects();
    }

    @Override
    protected void after() throws Exception
    {
        removeTestObjects();
    }

    private void createTestObjects()
    {
        streak = new GuessingStreak();

        testUser1 = userManagement.createNewDefaultUser(USERNAME1);
        testUser1.setBalance(DEFAULT_BALANCE);

        userDAO.save(streak, testUser1);

        testPokemon.setName(TEST_POKEMON_NAME);
        testPokemon.setId(TEST_POKEMON_ID);
        testPokemon.setNumber(TEST_POKEMON_NUMBER);
        testPokemon.setType1(TEST_POKEMON_TYPE1);
        testPokemon.setType2(TEST_POKEMON_TYPE2);
    }

    private void removeTestObjects()
    {
        User admin = userDAO.getByUsername(config().getOwnerUsername());
        userDAO.delete(streak, admin, testUser1);
    }
}