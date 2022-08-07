package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.PokemonShinyRating;
import com.schlock.bot.services.database.AbstractBaseDAO;
import com.schlock.bot.services.database.pokemon.PokemonShinyRatingDAO;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;

public class PokemonShinyRatingDAOImpl extends AbstractBaseDAO<PokemonShinyRating> implements PokemonShinyRatingDAO
{
    public PokemonShinyRatingDAOImpl(SessionFactory sessionFactory)
    {
        super(PokemonShinyRating.class, sessionFactory);
    }

    public List<String> getNumCodesOfRated()
    {
        List<PokemonShinyRating> ratings = getAll();

        List<String> numCodes = new ArrayList<>();
        for (PokemonShinyRating rating : ratings)
        {
            numCodes.add(rating.getNumberCode());
        }

        return numCodes;
    }
}
