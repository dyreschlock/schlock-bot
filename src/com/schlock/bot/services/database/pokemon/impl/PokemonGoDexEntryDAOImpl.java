package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.PokemonGoDexEntry;
import com.schlock.bot.services.database.AbstractBaseDAO;
import com.schlock.bot.services.database.pokemon.PokemonGoDexEntryDAO;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

public class PokemonGoDexEntryDAOImpl extends AbstractBaseDAO<PokemonGoDexEntry> implements PokemonGoDexEntryDAO
{
    public PokemonGoDexEntryDAOImpl(Session session)
    {
        super(PokemonGoDexEntry.class, session);
    }

    public List<PokemonGoDexEntry> getInPokemonOrder()
    {
        String text = " from PokemonGoDexEntry e " +
                        "order by e.pokemonNumber asc ";

        Query query = session.createQuery(text);
        return query.list();
    }
}
