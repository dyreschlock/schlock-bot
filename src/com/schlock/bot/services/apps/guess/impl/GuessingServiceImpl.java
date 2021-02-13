package com.schlock.bot.services.apps.guess.impl;

import com.schlock.bot.services.DatabaseModule;
import com.schlock.bot.services.apps.UserService;
import com.schlock.bot.services.apps.guess.GuessingService;
import com.schlock.bot.services.apps.impl.UserServiceImpl;
import com.schlock.bot.services.apps.pokemon.PokemonService;

public class GuessingServiceImpl implements GuessingService
{
    private final PokemonService pokemonService;
    private final UserService userService;

    private final DatabaseModule database;

    public GuessingServiceImpl(PokemonService pokemonService,
                               UserService userService,
                               DatabaseModule database)
    {
        this.pokemonService = pokemonService;
        this.userService = userService;
        this.database = database;
    }

    public boolean isCommand(String message)
    {
        return true;
    }

    public String process(String username, String command)
    {
        return null;
    }
}
