package com.schlock.bot.services.entities.pokemon;

import com.schlock.bot.entities.pokemon.Pokemon;

import java.util.List;

public interface PokemonManagement
{
    Pokemon getPokemonFromText(String text);

    Pokemon getRandomPokemon();

    boolean isGenSearch(String params);

    Pokemon getRandomPokemonInGen(String gen);

    boolean isHisuiSearch(String params);

    Pokemon getRandomPokemonInHisui();

    boolean isRangeSearch(String params);

    Pokemon getRandomPokemonInRange(String rangeText);

    List<Pokemon> getAllPokemonInGen(String gen);

    List<Pokemon> getHisuiPokemonInNumberOrder();
}
