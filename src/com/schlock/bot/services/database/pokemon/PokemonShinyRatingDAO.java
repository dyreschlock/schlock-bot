package com.schlock.bot.services.database.pokemon;

import com.schlock.bot.entities.pokemon.PokemonShinyRating;
import com.schlock.bot.services.database.BaseDAO;

import java.util.List;

public interface PokemonShinyRatingDAO extends BaseDAO<PokemonShinyRating>
{
    List<String> getNumCodesOfRated();
}
