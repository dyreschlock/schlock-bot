package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.PokemonGoDexEntry;
import com.schlock.bot.services.database.AbstractBaseDAO;
import com.schlock.bot.services.database.pokemon.PokemonGoDexEntryDAO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class PokemonGoDexEntryDAOImpl extends AbstractBaseDAO<PokemonGoDexEntry> implements PokemonGoDexEntryDAO
{
    public PokemonGoDexEntryDAOImpl(SessionFactory sessionFactory)
    {
        super(PokemonGoDexEntry.class, sessionFactory);
    }

    public List<PokemonGoDexEntry> getInPokemonOrder()
    {
        String text = " from PokemonGoDexEntry e " +
                        "order by e.pokemonNumber asc ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);
        List<PokemonGoDexEntry> entries = query.list();

        session.getTransaction().commit();
        session.close();

        return entries;
    }
}
