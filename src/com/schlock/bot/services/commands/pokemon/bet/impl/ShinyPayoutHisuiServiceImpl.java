package com.schlock.bot.services.commands.pokemon.bet.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.entities.pokemon.*;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.AbstractListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.pokemon.bet.ShinyPayoutHisuiService;
import com.schlock.bot.services.database.adhoc.DatabaseManager;
import com.schlock.bot.services.database.pokemon.ShinyBetHisuiDAO;
import com.schlock.bot.services.database.pokemon.ShinyDexEntryHisuiDAO;
import com.schlock.bot.services.database.pokemon.ShinyGetHisuiDAO;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.ShinyGetFormatter;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ioc.Messages;

import java.util.*;

public class ShinyPayoutHisuiServiceImpl extends AbstractListenerService implements ShinyPayoutHisuiService
{
    protected static final String NO_WINNERS_NO_BETS_KEY = "no-winners-no-bets";
    protected static final String NO_WINNERS_ONE_BET_KEY = "no-winners-one-bet";

    protected static final String BAD_FORMAT_MESSAGE_KEY = "get-hisui-wrong-format";

    protected static final String WINNERS_POKEMON_KEY = "payout-pokemon-winners";
    protected static final String WINNERS_OUTBREAK_KEY = "payout-outbreak-winners";
    protected static final String USER_UPDATE_KEY = "payout-user-update";
    protected static final String DOUBLER_BONUS = "doubler-bonus";

    private static final String SHINY_GET_COMMAND = "!hisuiget ";

    private static final String ALPHA_CHECK = "alpha";

    private final PokemonManagement pokemonManagement;
    private final ShinyGetFormatter shinyFormatter;

    private final DatabaseManager database;
    private final DeploymentConfiguration config;

    public ShinyPayoutHisuiServiceImpl(PokemonManagement pokemonManagement,
                                       ShinyGetFormatter shinyFormatter,
                                       DatabaseManager database,
                                       Messages messages,
                                       DeploymentConfiguration config)
    {
        super(messages);

        this.pokemonManagement = pokemonManagement;
        this.shinyFormatter = shinyFormatter;

        this.database = database;
        this.config = config;
    }

    public boolean isAcceptRequest(String username, String in)
    {
        String admin = config.getOwnerUsername();

        return username.equals(admin) &&
                in != null &&
                in.toLowerCase().startsWith(SHINY_GET_COMMAND);
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    protected ListenerResponse processRequest(String username, String in)
    {
        if (in.toLowerCase().trim().startsWith(SHINY_GET_COMMAND))
        {
            String params = in.substring(SHINY_GET_COMMAND.length()).trim();

            ShinyGetHisui get = createGetFromParams(params);
            if (get == null)
            {
                return formatSingleResponse(BAD_FORMAT_MESSAGE_KEY);
            }

            registerDexEntry(get);

            database.save(get);

            ListenerResponse response = ListenerResponse.relayAll();
            response.addMessage(shinyFormatter.formatNewlyCaughtHisui(get));

            List<ShinyBetHisui> bets = database.get(ShinyBetHisuiDAO.class).getAllCurrent();
            if (bets.size() == 0)
            {
                return response.addMessage(messages.get(NO_WINNERS_NO_BETS_KEY));
            }

            Map<User, Long> totalWinnings = new HashMap<>();
            Set<String> usersWinningPokemon = new HashSet<>();
            Set<String> usersWinningOutbreak = new HashSet<>();

            Integer closestRange = calculateClosestRange(get.getOutbreakChecks(), bets);

            for (ShinyBetHisui bet : bets)
            {
                bet.setShiny(get);
                database.save(bet);

                boolean winningPokemon = isWinningPokemon(bet, get.getPokemonId());
                boolean winningOutbreaks = isWinningOutbreaks(bet, get.getOutbreakChecks(), closestRange);

                Long winnings = 0l;
                if (winningPokemon)
                {
                    Double wins = bet.getBetAmount() * config.getBetsHisuiPokemonWinFactor();
                    winnings += wins.longValue();

                    usersWinningPokemon.add(bet.getUser().getUsername());
                }
                if (winningOutbreaks && bets.size() > 1)
                {
                    Double wins = bet.getBetAmount() * config.getBetsHisuiOutbreakWinFactor();
                    winnings += wins.longValue();

                    usersWinningOutbreak.add(bet.getUser().getUsername());
                }

                Long total = totalWinnings.get(bet.getUser());
                if (total == null)
                {
                    totalWinnings.put(bet.getUser(), winnings);
                }
                else
                {
                    totalWinnings.put(bet.getUser(), total + winnings);
                }
            }

            if (usersWinningPokemon.size() != 0)
            {
                response.addMessage(messages.format(WINNERS_POKEMON_KEY, StringUtils.join(usersWinningPokemon, ", ")));
            }

            if (bets.size() == 1)
            {
                response.addMessage(messages.format(NO_WINNERS_ONE_BET_KEY));
            }
            else if (usersWinningOutbreak.size() != 0)
            {
                response.addMessage(messages.format(WINNERS_OUTBREAK_KEY, StringUtils.join(usersWinningOutbreak, ", ")));
            }

            for (User user : totalWinnings.keySet())
            {
                String doublerMsg = "";
                Long winnings = totalWinnings.get(user);

                if (user.hasDoubler())
                {
                    winnings = user.modifyPointsWithDoubler(winnings);
                    doublerMsg = " " + messages.format(DOUBLER_BONUS, user.getPointsDoubler());
                }

                user.incrementBalance(winnings);

                String message = messages.format(USER_UPDATE_KEY,
                                                    user.getUsername(),
                                                    winnings,
                                                    config.getCurrencyMark(),
                                                    user.getBalance(),
                                                    config.getCurrencyMark()) + doublerMsg;
                response.addMessage(message);

                database.save(user);
            }
            return response;
        }
        return nullResponse();
    }

    protected Integer calculateClosestRange(final Integer ACTUAL, List<ShinyBetHisui> bets)
    {
        int closestRange = 10000;

        for (ShinyBetHisui bet : bets)
        {
            int range = ACTUAL - bet.getNumberOfChecks();
            if (range < 0)
            {
                range = range * -1;
            }

            if (range < closestRange)
            {
                closestRange = range;
            }
        }
        return closestRange;
    }

    private boolean isWinningPokemon(ShinyBetHisui bet, String pokemonId)
    {
        return pokemonId.equalsIgnoreCase(bet.getPokemonId());
    }

    private boolean isWinningOutbreaks(ShinyBetHisui bet, final Integer ACTUAL, Integer closestRange)
    {
        int range = ACTUAL - bet.getNumberOfChecks();
        if (range < 0)
        {
            range = range * -1;
        }
        return closestRange.equals(range);
    }

    protected ShinyGetHisui createGetFromParams(String paramString)
    {
        //[type] [pokemon] [checks] ([alpha])
        String params = paramString.toLowerCase();

        boolean alpha = false;
        if (params.contains(ALPHA_CHECK))
        {
            alpha = true;

            String[] p = params.split(ALPHA_CHECK);

            params = p[0];
        }

        String[] p = params.trim().split(" ");
        try
        {
            Pokemon pokemon = pokemonManagement.getPokemonFromText(p[1]);
            if (pokemon == null)
            {
                return null;
            }

            ShinyGetType type = ShinyGetType.valueOf(p[0].toUpperCase());
            if (type == null)
            {
                return null;
            }

            Integer checks = Integer.parseInt(p[2]);
            if (checks == null)
            {
                return null;
            }

            Integer shinyNumber = database.get(ShinyGetHisuiDAO.class).getCurrentShinyNumber();
            Integer alphaNumber = null;
            if (alpha)
            {
                alphaNumber = database.get(ShinyGetHisuiDAO.class).getCurrentAlphaNumber();
            }

            ShinyGetHisui get = new ShinyGetHisui();
            get.setMethod(type);
            get.setPokemonId(pokemon.getId());
            get.setOutbreakChecks(checks);
            get.setShinyNumber(shinyNumber);
            get.setAlphaNumber(alphaNumber);

            return get;
        } catch (Exception e)
        {
        }
        return null;
    }

    protected void registerDexEntry(ShinyGetHisui get)
    {
        String pokemonId = get.getPokemonId();

        ShinyDexEntryHisui entry = database.get(ShinyDexEntryHisuiDAO.class).getEntryByPokemonId(pokemonId);
        if (entry != null)
        {
            if (get.isAlpha())
            {
                entry.setHaveShiny(true);
                entry.setHaveAlpha(true);
                database.save(entry);
            } else if (!entry.isHaveShiny())
            {
                entry.setHaveShiny(true);
                database.save(entry);
            }
        }
    }
}
