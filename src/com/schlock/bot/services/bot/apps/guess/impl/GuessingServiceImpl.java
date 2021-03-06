package com.schlock.bot.services.bot.apps.guess.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.bot.apps.AbstractListenerService;
import com.schlock.bot.services.bot.apps.ListenerResponse;
import com.schlock.bot.services.bot.apps.UserService;
import com.schlock.bot.services.bot.apps.guess.GuessingService;
import com.schlock.bot.services.bot.apps.pokemon.PokemonService;
import com.schlock.bot.services.bot.apps.pokemon.PokemonUtils;
import com.schlock.bot.services.database.apps.UserDAO;
import org.apache.tapestry5.ioc.Messages;

public class GuessingServiceImpl extends AbstractListenerService implements GuessingService
{
    private static final String START_COMMAND = "!whodat";

    protected static final String GAME_ALREADY_STARTED_KEY = "whodat-already-started";
    protected static final String WINNER_KEY = "whodat-winner";

    private final PokemonService pokemonService;
    private final UserService userService;
    private final PokemonUtils pokemonUtils;

    private final UserDAO userDAO;

    private final DeploymentConfiguration config;

    protected Pokemon currentPokemon;

    public GuessingServiceImpl(PokemonService pokemonService,
                               UserService userService,
                               PokemonUtils pokemonUtils,
                               UserDAO userDAO,
                               Messages messages,
                               DeploymentConfiguration config)
    {
        super(messages);

        this.pokemonService = pokemonService;
        this.userService = userService;
        this.pokemonUtils = pokemonUtils;

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

    public ListenerResponse process(String username, String in)
    {
        String command = in.toLowerCase();
        if (command.startsWith(START_COMMAND))
        {
            if (currentPokemon != null)
            {
                String hint = pokemonUtils.formatHint1(currentPokemon);

                return formatSingleResponse(GAME_ALREADY_STARTED_KEY, hint);
            }

            String params = command.substring(START_COMMAND.length()).trim();

            return startGame(params);
        }

        if (currentPokemon != null)
        {
            String text = command.toLowerCase().replaceAll(" ", "");
            String id = currentPokemon.getId().toLowerCase();

            if (text.contains(id))
            {
                return processWinner(username);
            }
        }
        return ListenerResponse.relayNothing();
    }

    private ListenerResponse startGame(String params)
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
        String response = pokemonUtils.formatHint1(currentPokemon);
        return ListenerResponse.relaySingle().addMessage(response);
    }

    private ListenerResponse processWinner(String username)
    {
        Integer points = config.getQuizCorrectPoints();
        String mark = config.getCurrencyMark();

        User user = userService.getUser(username);
        user.incrementBalance(points);

        userDAO.save(user);

        userDAO.commit();

        String pokemonName = currentPokemon.getName();
        currentPokemon = null;

        return formatSingleResponse(WINNER_KEY, username, pokemonName, points.toString(), mark);
    }
}
