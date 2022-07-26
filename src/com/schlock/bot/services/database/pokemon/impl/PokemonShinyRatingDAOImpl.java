package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.PokemonShinyRating;
import com.schlock.bot.services.database.AbstractBaseDAO;
import com.schlock.bot.services.database.pokemon.PokemonShinyRatingDAO;
import org.hibernate.SessionFactory;

public class PokemonShinyRatingDAOImpl extends AbstractBaseDAO<PokemonShinyRating> implements PokemonShinyRatingDAO
{
    public PokemonShinyRatingDAOImpl(SessionFactory sessionFactory)
    {
        super(PokemonShinyRating.class, sessionFactory);
    }


}
