package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.ShinyDexEntryHisui;
import com.schlock.bot.services.database.AbstractBaseDAO;
import com.schlock.bot.services.database.pokemon.ShinyDexEntryHisuiDAO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class ShinyDexEntryHisuiDAOImpl extends AbstractBaseDAO<ShinyDexEntryHisui> implements ShinyDexEntryHisuiDAO
{
    public ShinyDexEntryHisuiDAOImpl(SessionFactory sessionFactory)
    {
        super(ShinyDexEntryHisui.class, sessionFactory);
    }

    public List<ShinyDexEntryHisui> getInPokemonOrder()
    {
        String text = " from ShinyDexEntryHisui e " +
                        " order by o.pokemonNumber asc ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);
        List<ShinyDexEntryHisui> entries = query.list();

        session.getTransaction().commit();
        session.close();

        return entries;
    }
}
