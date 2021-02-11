package com.schlock.bot.services;

public interface PokemonService
{
    public String process(String in);

    public boolean isPokemonCommand(String in);
}
