package com.schlock.bot.services;

public interface PokemonService
{
    public static final String POKEMON_COMMAND = "!pokemon";
    public static final String POKEMON_E_COMMAND = "!pokémon";

    public String process(String in);
}
