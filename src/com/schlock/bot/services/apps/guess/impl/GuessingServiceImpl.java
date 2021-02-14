package com.schlock.bot.services.apps.guess.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.entities.apps.pokemon.PokemonUtils;
import com.schlock.bot.services.DatabaseModule;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.apps.UserService;
import com.schlock.bot.services.apps.guess.GuessingService;
import com.schlock.bot.services.apps.pokemon.PokemonService;

public class GuessingServiceImpl implements GuessingService
{
    private static final String START_COMMAND = "!whodat";

    public static final String NOT_ADMIN_MESSAGE = "Sorry, you are not the admin.";
    public static final String GAME_ALREADY_STARTED = "The game has already started.";

    private final PokemonService pokemonService;
    private final UserService userService;

    private final DatabaseModule database;
    private final DeploymentContext context;

    private Pokemon currentPokemon;

    public GuessingServiceImpl(PokemonService pokemonService,
                               UserService userService,
                               DatabaseModule database,
                               DeploymentContext context)
    {
        this.pokemonService = pokemonService;
        this.userService = userService;

        this.database = database;
        this.context = context;
    }

    public boolean isAcceptRequest(String message)
    {
        return true;
    }

    public boolean isTerminateAfterRequest()
    {
        return false;
    }

    public String process(String username, String in)
    {
        String command = in.toLowerCase();
        if (command.startsWith(START_COMMAND))
        {
            if (!username.equalsIgnoreCase(context.getOwnerUsername()))
            {
                return NOT_ADMIN_MESSAGE;
            }
            if (currentPokemon != null)
            {
                return GAME_ALREADY_STARTED;
            }
            return startGame();
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

    private String startGame()
    {
        currentPokemon = pokemonService.getRandomPokemon();
        return PokemonUtils.formatHint1(currentPokemon);
    }

    private String processWinner(String username)
    {
        Integer points = 50;
        String mark = context.getCurrencyMark();

        User user = userService.getUser(username);
        user.incrementBalance(points);

        database.save(user);

        String pokemonName = currentPokemon.getName();
        currentPokemon = null;

        String message = "Congradulations, %s! %s is correct! You get %s%s";
        return String.format(message, username, pokemonName, points.toString(), mark);
    }
}
