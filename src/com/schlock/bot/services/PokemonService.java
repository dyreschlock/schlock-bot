package com.schlock.bot.services;

public interface PokemonService
{
    public static final String POKEMON_COMMAND = "!pokemon";

    public String process(String in);
}
