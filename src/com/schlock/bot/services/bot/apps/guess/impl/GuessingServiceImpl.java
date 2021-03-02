package com.schlock.bot.services.bot.apps.guess.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.services.bot.apps.pokemon.PokemonUtils;
import com.schlock.bot.services.bot.apps.pokemon.impl.PokemonUtilsImpl;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.bot.apps.UserService;
import com.schlock.bot.services.bot.apps.guess.GuessingService;
import com.schlock.bot.services.bot.apps.pokemon.PokemonService;
import com.schlock.bot.services.database.apps.UserDAO;
import org.apache.tapestry5.ioc.Messages;

import java.util.Arrays;
import java.util.List;

public class GuessingServiceImpl implements GuessingService
{
    private static final String START_COMMAND = "!whodat";

    protected static final String GAME_ALREADY_STARTED_KEY = "whodat-already-started";
    protected static final String WINNER_KEY = "whodat-winner";

    private final PokemonService pokemonService;
    private final UserService userService;
    private final PokemonUtils pokemonUtils;

    private final UserDAO userDAO;

    private final Messages messages;
    private final DeploymentConfiguration config;

    protected Pokemon currentPokemon;

    public GuessingServiceImpl(PokemonService pokemonService,
                               UserService userService,
                               PokemonUtils pokemonUtils,
                               UserDAO userDAO,
                               Messages messages,
                               DeploymentConfiguration config)
    {
        this.pokemonService = pokemonService;
        this.userService = userService;
        this.pokemonUtils = pokemonUtils;

        this.userDAO = userDAO;

        this.messages = messages;
        this.config = config;
    }

    public boolean isAcceptRequest(String username, String message)
    {
        return true;
    }

    public boolean isTerminateAfterRequest()
    {
        return false;
    }

    public List<String> process(String username, String command)
    {
        return Arrays.asList(processSingleResult(username, command));
    }

    public String processSingleResult(String username, String in)
    {
        String command = in.toLowerCase();
        if (command.startsWith(START_COMMAND))
        {
//            if (!username.equalsIgnoreCase(context.getOwnerUsername()))
//            {
//                return NOT_ADMIN_RESPONSE;
//            }
            if (currentPokemon != null)
            {
                String hint = pokemonUtils.formatHint1(currentPokemon);
                return messages.format(GAME_ALREADY_STARTED_KEY, hint);
            }

            String params = command.substring(START_COMMAND.length()).trim();
            return startGame(params);
        }

        if (currentPokemon != null)
        {
            String text = command.replaceAll(" ", "");
            String id = currentPokemon.getId();

            if (text.contains(id))
            {
                return processWinner(username);
            }

        }
        return null;
    }

    private String startGame(String params)
    {
        if (pokemonService.isGenSearch(params))
        {
            currentPokemon = pokemonService.getRandomPokemonInGen(params);
        }
        else if (pokemonService.isRangeSearch(params))
        {
            currentPokemon = pokemonService.getRandomPokemonInRange(params);
        }
        else
        {
            currentPokemon = pokemonService.getRandomPokemon();
        }
        return pokemonUtils.formatHint1(currentPokemon);
    }

    private String processWinner(String username)
    {
        Integer points = config.getQuizCorrectPoints();
        String mark = config.getCurrencyMark();

        User user = userService.getUser(username);
        user.incrementBalance(points);

        userDAO.save(user);

        userDAO.commit();

        String pokemonName = currentPokemon.getName();
        currentPokemon = null;

        return messages.format(WINNER_KEY, username, pokemonName, points.toString(), mark);
    }
}
