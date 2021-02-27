package com.schlock.bot.services.bot.apps.guess.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.entities.apps.pokemon.PokemonUtils;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.bot.UserService;
import com.schlock.bot.services.bot.apps.guess.GuessingService;
import com.schlock.bot.services.bot.apps.pokemon.PokemonService;
import com.schlock.bot.services.database.apps.UserDAO;

import java.util.Arrays;
import java.util.List;

public class GuessingServiceImpl implements GuessingService
{
    private static final String START_COMMAND = "!whodat";

    protected static final String GAME_ALREADY_STARTED = "Game has started. Current Hint: ";
    protected static final String WINNER_MESSAGE = "Congratulations, %s! %s is correct! You get %s%s";

    private final PokemonService pokemonService;
    private final UserService userService;

    private final UserDAO userDAO;

    private final DeploymentConfiguration config;

    protected Pokemon currentPokemon;

    public GuessingServiceImpl(PokemonService pokemonService,
                               UserService userService,
                               UserDAO userDAO,
                               DeploymentConfiguration config)
    {
        this.pokemonService = pokemonService;
        this.userService = userService;

        this.userDAO = userDAO;

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
                String hint = PokemonUtils.formatHint1(currentPokemon);
                return GAME_ALREADY_STARTED + hint;
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
        return PokemonUtils.formatHint1(currentPokemon);
    }

    private String processWinner(String username)
    {
        Integer points = config.getQuizCorrectPoints();
        String mark = config.getCurrencyMark();

        User user = userService.getUser(username);
        user.incrementBalance(points);

        userDAO.save(user);

        String pokemonName = currentPokemon.getName();
        currentPokemon = null;

        return String.format(WINNER_MESSAGE, username, pokemonName, points.toString(), mark);
    }
}
