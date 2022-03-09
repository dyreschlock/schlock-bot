package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.ShinyDexEntryGo;
import com.schlock.bot.services.database.AbstractBaseDAO;
import com.schlock.bot.services.database.pokemon.ShinyDexEntryGoDAO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class ShinyDexEntryGoDAOImpl extends AbstractBaseDAO<ShinyDexEntryGo> implements ShinyDexEntryGoDAO
{
    public ShinyDexEntryGoDAOImpl(SessionFactory sessionFactory)
    {
        super(ShinyDexEntryGo.class, sessionFactory);
    }

    public List<ShinyDexEntryGo> getInPokemonOrder()
    {
        String text = " from ShinyDexEntryGo e " +
                        "order by e.pokemonNumber asc ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);
        List<ShinyDexEntryGo> entries = query.list();

        session.getTransaction().commit();
        session.close();

        return entries;
    }
}
