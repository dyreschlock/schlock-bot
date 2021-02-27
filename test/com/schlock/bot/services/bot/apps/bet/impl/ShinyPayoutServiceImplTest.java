package com.schlock.bot.services.bot.apps.bet.impl;

import com.schlock.bot.entities.Persisted;
import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.bet.ShinyBet;
import com.schlock.bot.entities.apps.pokemon.ShinyGet;
import com.schlock.bot.services.DatabaseModule;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.bot.apps.pokemon.PokemonService;
import com.schlock.bot.services.bot.apps.pokemon.impl.PokemonServiceImpl;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.apps.ShinyGetDAO;
import com.schlock.bot.services.database.apps.UserDAO;
import com.schlock.bot.services.impl.DeploymentContextImpl;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShinyPayoutServiceImplTest extends DatabaseTest
{
    private static final Double TEST_POKEMON_WIN_FACTOR = 2.0;
    private static final Double TEST_TIME_WIN_FACTOR = 3.0;
    private static final Double TEST_BOTH_WIN_FACTOR = 2.0;

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

    private ShinyPayoutServiceImpl impl;

    private User user1;
    private User user2;
    private User user3;

    private ShinyBet bet1;
    private ShinyBet bet2;
    private ShinyBet bet3;


    @Test
    public void testUseCase1()
    {
        final String ADMIN = getDeploymentContext().getOwnerUsername();
        final String MARK = getDeploymentContext().getCurrencyMark();
        final String GET = "!shinyget catch beedrill 100";

        List<String> responses = impl.process(USERNAME1, GET);

        assertEquals(0, responses.size());

        responses = impl.process(ADMIN, GET);

        assertEquals(6, responses.size());

        user1 = getDatabase().get(UserDAO.class).getByUsername(user1.getUsername());
        user2 = getDatabase().get(UserDAO.class).getByUsername(user2.getUsername());
        user3 = getDatabase().get(UserDAO.class).getByUsername(user3.getUsername());


        Double winningPokemon = BET1_AMOUNT.doubleValue() * TEST_POKEMON_WIN_FACTOR;
        Double winningTime = BET1_AMOUNT.doubleValue() * TEST_TIME_WIN_FACTOR;
        Double user1winnings = (winningPokemon + winningTime) * TEST_BOTH_WIN_FACTOR;

        Double user2winnings = BET2_AMOUNT.doubleValue() * TEST_POKEMON_WIN_FACTOR;

        Integer user1balance = BALANCE + user1winnings.intValue();
        Integer user2balance = BALANCE + user2winnings.intValue();
        Integer user3balance = BALANCE;

        assertEquals(user1balance, user1.getBalance());
        assertEquals(user2balance, user2.getBalance());
        assertEquals(user3balance, user3.getBalance());

        List<String> winnersPokemon = Arrays.asList(user2.getUsername(), user1.getUsername());
        List<String> winnersTime = Arrays.asList(user1.getUsername());
        List<String> winnersBoth = Arrays.asList(user1.getUsername());

        String response1 = String.format(ShinyPayoutServiceImpl.WINNERS_POKEMON, StringUtils.join(winnersPokemon, ", "));
        String response2 = String.format(ShinyPayoutServiceImpl.WINNERS_TIME, StringUtils.join(winnersTime, ", "));
        String response3 = String.format(ShinyPayoutServiceImpl.WINNERS_BOTH, StringUtils.join(winnersBoth, ", "));

        String response4 = String.format(ShinyPayoutServiceImpl.USER_UPDATE, USERNAME1, user1winnings.intValue(), MARK, user1balance, MARK);
        String response5 = String.format(ShinyPayoutServiceImpl.USER_UPDATE, USERNAME2, user2winnings.intValue(), MARK, user2balance, MARK);
        String response6 = String.format(ShinyPayoutServiceImpl.USER_UPDATE, USERNAME3, 0, MARK, BALANCE, MARK);

        List<String> expectedResponses = Arrays.asList(response1, response2, response3, response4, response5, response6);
        for (String expectedResponse : expectedResponses)
        {
            assertTrue(responses.contains(expectedResponse), expectedResponse);
        }
    }


    @BeforeEach
    public void setup() throws Exception
    {
        setupDatabase();
        createTestObjects();

        DeploymentContext context = getDeploymentContext();
        DatabaseModule database = getDatabase();

        PokemonService pokemonService = new PokemonServiceImpl(context);

        impl = new ShinyPayoutServiceImpl(pokemonService, database, context);
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

    protected DeploymentContext createDeploymentContext()
    {
        return new DeploymentContextImpl(DeploymentContext.TEST)
        {
            public Double getBetsPokemonWinFactor() { return TEST_POKEMON_WIN_FACTOR; }
            public Double getBetsTimeWinFactor() { return TEST_TIME_WIN_FACTOR; }
            public Double getBetsBothWinFactor() { return TEST_BOTH_WIN_FACTOR; }
        };
    }

    @AfterEach
    public void teardown()
    {
        removeTestObjects();
    }

    private void removeTestObjects()
    {
        ShinyGet get = getDatabase().get(ShinyGetDAO.class).getMostRecent();

        List<Persisted> objects = Arrays.asList(user1, user2, user3, get);
        getDatabase().delete(objects);
    }
}