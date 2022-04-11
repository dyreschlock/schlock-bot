package com.schlock.bot.services.commands.pokemon.bet.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.entities.pokemon.ShinyBetHisui;
import com.schlock.bot.entities.pokemon.ShinyGetHisui;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.pokemon.ShinyGetHisuiDAO;
import com.schlock.bot.services.entities.base.UserManagement;
import com.schlock.bot.services.entities.base.impl.UserManagementImpl;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.PokemonUtils;
import com.schlock.bot.services.entities.pokemon.ShinyGetFormatter;
import com.schlock.bot.services.entities.pokemon.impl.PokemonManagementImpl;
import com.schlock.bot.services.entities.pokemon.impl.PokemonUtilsImpl;
import com.schlock.bot.services.entities.pokemon.impl.ShinyGetFormatterImpl;
import com.schlock.bot.services.impl.DeploymentConfigurationImpl;
import org.junit.jupiter.api.Test;

public class ShinyPayoutHisuiServiceImplTest extends DatabaseTest
{
    private static final Double TEST_POKEMON_WIN_FACTOR = 10.0;
    private static final Double TEST_OUTBREAK_WIN_FACTOR = 2.0;
    private static final Double TEST_OUTBREAK_POT_WIN_FACTOR = 0.5;

    private static final String USERNAME1 = "username1";
    private static final String USERNAME2 = "username2";
    private static final String USERNAME3 = "username3";

    private static final Long BALANCE = 10000l;

    private static final Integer BET1_AMOUNT = 500;
    private static final Integer BET1_OUTBREAKS = 20;

    private static final Integer BET2_AMOUNT = 600;
    private static final Integer BET2_OUTBREAKS = 30;

    private static final Integer BET3_AMOUNT = 700;
    private static final Integer BET3_OUTBREAKS = 45;
    private static final String BET3_POKEMON = "turtwig";

    private ShinyGetFormatter shinyFormatter;
    private UserManagement userManagement;

    private ShinyPayoutHisuiServiceImpl impl;

    private User user1;
    private User user2;
    private User user3;

    private ShinyBetHisui bet1;
    private ShinyBetHisui bet2;
    private ShinyBetHisui bet3;

    @Test
    public void testUseCase1()
    {

    }

    @Override
    protected void before() throws Exception
    {
        userManagement = new UserManagementImpl(database(), overriddenConfiguration());

        PokemonUtils pokemonUtils = new PokemonUtilsImpl(messages());

        PokemonManagement pokemonManagement = new PokemonManagementImpl(pokemonUtils, overriddenConfiguration());

        shinyFormatter = new ShinyGetFormatterImpl(pokemonManagement, messages());

        impl = new ShinyPayoutHisuiServiceImpl(pokemonManagement, shinyFormatter, database(), messages(), overriddenConfiguration());

        createTestObjects();
    }

    @Override
    protected void after() throws Exception
    {
        removeTestObjects();
    }

    private void createTestObjects()
    {
        user1 = userManagement.createNewDefaultUser(USERNAME1);
        user1.setBalance(BALANCE);

        user2 = userManagement.createNewDefaultUser(USERNAME2);
        user2.setBalance(BALANCE);

        user3 = userManagement.createNewDefaultUser(USERNAME3);
        user3.setBalance(BALANCE);


        bet1 = new ShinyBetHisui();
        bet1.setUser(user1);
        bet1.setBetAmount(BET1_AMOUNT);
        bet1.setNumberOfChecks(BET1_OUTBREAKS);

        bet2 = new ShinyBetHisui();
        bet2.setUser(user2);
        bet2.setBetAmount(BET2_AMOUNT);
        bet2.setNumberOfChecks(BET2_OUTBREAKS);

        bet3 = new ShinyBetHisui();
        bet3.setUser(user3);
        bet3.setBetAmount(BET3_AMOUNT);
        bet3.setNumberOfChecks(BET3_OUTBREAKS);
        bet3.setPokemonId(BET3_POKEMON);


        database().save(user1, user2, user3, bet1, bet2, bet3);
    }

    protected DeploymentConfiguration overriddenConfiguration()
    {
        return new DeploymentConfigurationImpl()
        {
            protected String getContext()
            {
                return DeploymentConfigurationImpl.TEST;
            }

            public Double getBetsHisuiPokemonWinFactor()
            {
                return TEST_POKEMON_WIN_FACTOR;
            }

            public Double getBetsHisuiOutbreakWinFactor()
            {
                return TEST_OUTBREAK_WIN_FACTOR;
            }

            public Double getBetsHisuiOutbreakPotWinFactor()
            {
                return TEST_OUTBREAK_POT_WIN_FACTOR;
            }
        };
    }

    private void removeTestObjects()
    {
        ShinyGetHisui get = database().get(ShinyGetHisuiDAO.class).getMostRecent();

        database().delete(user1, user2, user3, bet1, bet2, bet3, get);
    }
}
