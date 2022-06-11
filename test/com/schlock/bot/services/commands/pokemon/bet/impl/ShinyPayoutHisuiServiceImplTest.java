package com.schlock.bot.services.commands.pokemon.bet.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.entities.pokemon.ShinyBetHisui;
import com.schlock.bot.entities.pokemon.ShinyDexEntryHisui;
import com.schlock.bot.entities.pokemon.ShinyGetHisui;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.base.UserDAO;
import com.schlock.bot.services.database.pokemon.ShinyDexEntryHisuiDAO;
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
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShinyPayoutHisuiServiceImplTest extends DatabaseTest
{
    private static final Double TEST_POKEMON_WIN_FACTOR = 10.0;
    private static final Double TEST_OUTBREAK_WIN_FACTOR = 2.0;
    private static final Double TEST_OUTBREAK_POT_WIN_FACTOR = 0.5;

    private static final String USERNAME1 = "username1";
    private static final String USERNAME2 = "username2";
    private static final String USERNAME3 = "username3";
    private static final String USERNAME4 = "username4";

    private static final Long BALANCE = 10000l;

    private static final Integer BET1_AMOUNT = 500;
    private static final Integer BET1_OUTBREAKS = 20;

    private static final Integer BET2_AMOUNT = 600;
    private static final Integer BET2_OUTBREAKS = 30;

    private static final Integer BET3_AMOUNT = 700;
    private static final Integer BET3_OUTBREAKS = 45;
    private static final String BET3_POKEMON = "turtwig";

    private static final Integer BET4_AMOUNT = 1000;
    private static final Integer BET4_OUTBREAKS = 40;

    private ShinyGetFormatter shinyFormatter;
    private UserManagement userManagement;

    private ShinyPayoutHisuiServiceImpl impl;

    private User user1, user2, user3, user4;
    private ShinyBetHisui bet1, bet2, bet3, bet4;
    private ShinyDexEntryHisui dex1;

    @Test
    public void testRegistration1()
    {
        final String ADMIN = config().getOwnerUsername();

        String getCommand = "!hisuiget MASSIVE turtwig";

        ListenerResponse resp = impl.process(ADMIN, getCommand);


    }

    @Test
    public void testPayoutUseCase1()
    {
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

        bet4 = new ShinyBetHisui();
        bet4.setUser(user4);
        bet4.setBetAmount(BET4_AMOUNT);
        bet4.setNumberOfChecks(BET4_OUTBREAKS);

        database().save(bet1, bet2, bet3, bet4);


        final String ADMIN = config().getOwnerUsername();
        final String MARK = config().getCurrencyMark();
        final String GET = "!hisuiget MASSIVE turtwig 25";

        ListenerResponse resp = impl.process(USERNAME1, GET);
        List<String> responses = resp.getMessages();

        assertFalse(resp.isRelayAll());
        assertEquals(0, responses.size());

        resp = impl.process(ADMIN, GET);
        responses = resp.getMessages();

        assertTrue(resp.isRelayAll());
        assertEquals(7, responses.size());

        user1 = database().get(UserDAO.class).getByUsername(user1.getUsername());
        user2 = database().get(UserDAO.class).getByUsername(user2.getUsername());
        user3 = database().get(UserDAO.class).getByUsername(user3.getUsername());
        user4 = database().get(UserDAO.class).getByUsername(user4.getUsername());

        Integer betSum = BET1_AMOUNT + BET2_AMOUNT + BET3_AMOUNT + BET4_AMOUNT;
        Double potWinnings = betSum.doubleValue() * TEST_OUTBREAK_POT_WIN_FACTOR;

        Double winningOutbreaks1 = BET1_AMOUNT.doubleValue() * TEST_OUTBREAK_WIN_FACTOR;
        Long user1winnings = user1.modifyPointsWithDoubler(winningOutbreaks1.longValue() + potWinnings.longValue());

        Double winningOutbraeks2 = BET2_AMOUNT.doubleValue() * TEST_OUTBREAK_WIN_FACTOR;
        Long user2winnings = user2.modifyPointsWithDoubler(winningOutbraeks2.longValue() + potWinnings.longValue());

        Double winningPokemon = BET3_AMOUNT.doubleValue() * TEST_POKEMON_WIN_FACTOR;
        Long user3winnings = user3.modifyPointsWithDoubler(winningPokemon.longValue());

        Long user4winnings = 0l;

        Long user1balance = BALANCE + user1winnings;
        Long user2balance = BALANCE + user2winnings;
        Long user3balance = BALANCE + user3winnings;
        Long user4balance = BALANCE + user4winnings;

        assertEquals(user1.getBalance(), user1balance);
        assertEquals(user2.getBalance(), user2balance);
        assertEquals(user3.getBalance(), user3balance);
        assertEquals(user4.getBalance(), user4balance);

        List<String> winnersOutbreaks = Arrays.asList(user2.getUsername(), user1.getUsername());
        List<String> winnersPokemon = Arrays.asList(user3.getUsername());

        ShinyGetHisui mostRecent = database().get(ShinyGetHisuiDAO.class).getMostRecent();

        String response1 = shinyFormatter.formatNewlyCaughtHisui(mostRecent);

        String response2 = messages().format(ShinyPayoutHisuiServiceImpl.WINNERS_POKEMON_KEY, StringUtils.join(winnersPokemon, ", "));
        String response3 = messages().format(ShinyPayoutHisuiServiceImpl.WINNERS_OUTBREAK_KEY, StringUtils.join(winnersOutbreaks, ", "));

        //one for each user
        String response4 = messages().format(ShinyPayoutHisuiServiceImpl.USER_UPDATE_KEY, USERNAME1, user1winnings.intValue(), MARK, user1balance, MARK);
        response4 += " " + messages().format(ShinyPayoutHisuiServiceImpl.DOUBLER_BONUS, user1.getPointsDoubler());

        String response5 = messages().format(ShinyPayoutHisuiServiceImpl.USER_UPDATE_KEY, USERNAME2, user2winnings.intValue(), MARK, user2balance, MARK);
        String response6 = messages().format(ShinyPayoutHisuiServiceImpl.USER_UPDATE_KEY, USERNAME3, user3winnings.intValue(), MARK, user3balance, MARK);

        String response7 = messages().format(ShinyPayoutHisuiServiceImpl.USER_UPDATE_KEY, USERNAME4, user4winnings.intValue(), MARK, user4balance, MARK);

        List<String> expectedResponses = Arrays.asList(response1, response2, response3, response4, response5, response6, response7);
        for (String expectedResponse : expectedResponses)
        {
            assertTrue(responses.contains(expectedResponse), expectedResponse);
        }

        dex1 = database().get(ShinyDexEntryHisuiDAO.class).getEntryByPokemonId(dex1.getPokemonId());

        assertTrue(dex1.isHaveShiny());
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
        user1.setPointsDoubler(2);

        user2 = userManagement.createNewDefaultUser(USERNAME2);
        user2.setBalance(BALANCE);

        user3 = userManagement.createNewDefaultUser(USERNAME3);
        user3.setBalance(BALANCE);

        user4 = userManagement.createNewDefaultUser(USERNAME4);
        user4.setBalance(BALANCE);

        dex1 = new ShinyDexEntryHisui();
        dex1.setNumberCode("387");
        dex1.setPokemonNumber(130);
        dex1.setPokemonId("turtwig");

        database().save(user1, user2, user3, user4, dex1);
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
        database().delete(user1, user2, user3, user4, dex1);

        ShinyGetHisui get = database().get(ShinyGetHisuiDAO.class).getMostRecent();
        if (get != null)
        {
            database().delete(get);
        }

        for (ShinyBetHisui bet : Arrays.asList(bet1, bet2, bet3, bet4))
        {
            if (bet != null)
            {
                database().delete(bet);
            }
        }
    }
}
