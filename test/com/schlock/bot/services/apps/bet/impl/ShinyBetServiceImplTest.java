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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class ShinyBetServiceImplTest extends DatabaseTest
{
    private static final String USERNAME1 = "username1";
    private static final Integer BALANCE = 10000;

    private static final String BET_POKEMON = "magikarp";
    private static final Integer BET_MINUTES = 100;
    private static final Integer BET_AMOUNT = 100;


    private PokemonService pokemonService;

    private ShinyBetServiceImpl impl;

    private User user;


    @Test
    public void testNewBet()
    {
        String newBet = "!bet "+ BET_POKEMON +" "+ BET_MINUTES +" " + BET_AMOUNT;

        String response = impl.processSingleResults(USERNAME1, newBet);

        String mark = getDeploymentContext().getCurrencyMark();
        Pokemon pokemon = pokemonService.getPokemonFromText(BET_POKEMON);

        String expected = String.format(ShinyBetServiceImpl.BET_SUCCESS, USERNAME1, pokemon.getName(), BET_MINUTES.toString(), BET_AMOUNT.toString(), mark);

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

        getDatabase().save(user);
    }

    @AfterEach
    public void tearDown() throws Exception
    {
        removeTestObjects();
    }

    private void removeTestObjects()
    {
        List<ShinyBet> bets = getDatabase().get(ShinyBetDAO.class).getBetsByUser(user);

        List<Persisted> objects = new ArrayList<>();
        objects.addAll(bets);
        objects.add(user);

        getDatabase().delete(objects);
    }

}