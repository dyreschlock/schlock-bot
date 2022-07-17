package com.schlock.bot.services.entities.pokemon;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.entities.pokemon.PokemonRegion;

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

    boolean isAlolanAvailable(Pokemon pokemon);

    List<Integer> getAlolanPokemonNumbers();

    List<Pokemon> getAllPokemonInGen(String gen);

    List<Pokemon> getAllPokemonInNumberOrder();

    List<Pokemon> getHisuiPokemonInNumberOrder();

    List<Pokemon> getAllPokemonInRegion(PokemonRegion region);
}
