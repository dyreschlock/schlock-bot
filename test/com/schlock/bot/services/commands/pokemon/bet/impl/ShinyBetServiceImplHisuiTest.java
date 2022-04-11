package com.schlock.bot.services.commands.pokemon.bet.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.entities.pokemon.ShinyBetHisui;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.pokemon.ShinyBetHisuiDAO;
import com.schlock.bot.services.entities.base.UserManagement;
import com.schlock.bot.services.entities.base.impl.UserManagementImpl;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.PokemonUtils;
import com.schlock.bot.services.entities.pokemon.ShinyBetFormatter;
import com.schlock.bot.services.entities.pokemon.impl.PokemonManagementImpl;
import com.schlock.bot.services.entities.pokemon.impl.PokemonUtilsImpl;
import com.schlock.bot.services.entities.pokemon.impl.ShinyBetFormatterImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShinyBetServiceImplHisuiTest extends DatabaseTest
{
    private static final String OPEN_BETS_HISUI = "!openbets hisui";

    private static final String USERNAME1 = "username1";
    private static final Long BALANCE1 = Long.valueOf(10000);

    private static final String USERNAME2 = "username2";
    private static final Long BALANCE2 = Long.valueOf(10000);

    private static final String BET1_POKEMON = "turtwig";
    private static final Integer BET1_OUTBREAKS = 20;
    private static final Integer BET1_AMOUNT = 200;

    private static final Integer BET1U_OUTBREAKS = 40;
    private static final Integer BET1U_AMOUNT = 400;

    private static final Integer BET2_OUTBREAKS = 30;
    private static final Integer BET2_AMOUNT = 500;

    private PokemonManagement pokemonManagement;
    private UserManagement userManagerment;

    private ShinyBetServiceImpl impl;

    private User user1;
    private User user2;

    @Test
    public void testNoBet()
    {
        impl.openBetting(OPEN_BETS_HISUI);

        String newBet = "!bet";

        ListenerResponse resp = impl.process(USERNAME2, newBet);
        String response = resp.getFirstMessage();
        String expected = messages().format(ShinyBetServiceImpl.BET_HISUI_WRONG_FORMAT_KEY);

        assertFalse(resp.isRelayAll());
        assertEquals(expected, response);
    }

    @Test
    public void testNewBetHisui()
    {
        impl.openBetting(OPEN_BETS_HISUI);

        final String MARK = config().getCurrencyMark();


        String newBet = "!bet " + BET2_AMOUNT + " " + BET2_OUTBREAKS;

        ListenerResponse resp = impl.process(USERNAME2, newBet);
        String response = resp.getFirstMessage();
        String expected = messages().format(ShinyBetServiceImpl.BET_HISUI_SUCCESS_KEY, USERNAME2, BET2_AMOUNT, MARK, BET2_OUTBREAKS);

        assertTrue(resp.isRelayAll());
        assertEquals(expected, response);
    }

    @Test
    public void testUpdateBet()
    {
        impl.openBetting(OPEN_BETS_HISUI);

        final String MARK = config().getCurrencyMark();

        ShinyBetHisui current = database().get(ShinyBetHisuiDAO.class).getByUsername(USERNAME1);
        assertTrue(current != null);

        String newBet = "!bet " + BET1U_AMOUNT + " " + BET1U_OUTBREAKS;

        ListenerResponse resp = impl.process(USERNAME1, newBet);
        String response = resp.getFirstMessage();
        String expected = messages().format(ShinyBetServiceImpl.BET_HISUI_UPDATE_SUCCESS_KEY, USERNAME1, BET1U_AMOUNT, MARK, BET1U_OUTBREAKS);

        assertTrue(resp.isRelayAll());
        assertEquals(expected, response);
    }

    @Test
    public void testCurrentBets()
    {
        impl.openBetting(OPEN_BETS_HISUI);

        final String MARK = config().getCurrencyMark();
        Pokemon pokemon1 = pokemonManagement.getPokemonFromText(BET1_POKEMON);

        ListenerResponse resp = impl.process(USERNAME1, "!currentbets");
        List<String> responses = resp.getMessages();

        assertFalse(resp.isRelayAll());
        assertEquals(1, responses.size());

        String response = responses.get(0);
        String expected1 = messages().format(ShinyBetFormatterImpl.CURRENT_BET_HISUI_KEY,
                                                USERNAME1,
                                                BET1_AMOUNT.toString(),
                                                MARK,
                                                BET1_OUTBREAKS.toString());

        expected1 += " " + messages().format(ShinyBetFormatterImpl.BET_HISUI_POKEMON_KEY, pokemon1.getName());

        assertEquals(expected1, response);
    }

    @Override
    protected void before() throws Exception
    {
        PokemonUtils pokemonUtils = new PokemonUtilsImpl(messages());

        pokemonManagement = new PokemonManagementImpl(pokemonUtils, config());

        userManagerment = new UserManagementImpl(database(), config());

        ShinyBetFormatter betFormatter = new ShinyBetFormatterImpl(pokemonManagement, messages(), config());

        impl = new ShinyBetServiceImpl(pokemonManagement, userManagerment, betFormatter, database(), messages(), config());

        createTestObjects();
    }

    private void createTestObjects()
    {
        user1 = userManagerment.createNewDefaultUser(USERNAME1);
        user1.setBalance(BALANCE1);

        user2 = userManagerment.createNewDefaultUser(USERNAME2);
        user2.setBalance(BALANCE2);

        ShinyBetHisui bet = new ShinyBetHisui();
        bet.setUser(user1);
        bet.setPokemonId(BET1_POKEMON);
        bet.setBetAmount(BET1_AMOUNT);
        bet.setNumberOfChecks(BET1_OUTBREAKS);

        database().save(user1, user2, bet);
    }

    @Override
    protected void after() throws Exception
    {
        removeTestObjects();
    }

    private void removeTestObjects()
    {
        List<ShinyBetHisui> bets = database().get(ShinyBetHisuiDAO.class).getAll();
        for (ShinyBetHisui bet : bets)
        {
            database().delete(bet);
        }
        database().delete(user1, user2);
    }
}
