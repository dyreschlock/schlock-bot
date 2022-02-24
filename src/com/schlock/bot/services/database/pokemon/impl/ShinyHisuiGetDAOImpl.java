package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.ShinyHisuiGet;
import com.schlock.bot.services.database.AbstractBaseDAO;
import com.schlock.bot.services.database.pokemon.ShinyHisuiGetDAO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class ShinyHisuiGetDAOImpl extends AbstractBaseDAO<ShinyHisuiGet> implements ShinyHisuiGetDAO
{
    public ShinyHisuiGetDAOImpl(SessionFactory sessionFactory)
    {
        super(ShinyHisuiGet.class, sessionFactory);
    }

    public Double getCurrentResetAverage()
    {
        return null;
    }

    public Integer getCurrentShinyNumber()
    {
        String text = " select count(g) from ShinyHisuiGet g ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);
        Long count = (Long) query.list().get(0);

        session.getTransaction().commit();
        session.close();

        Integer next = count.intValue() + 1;
        return next;
    }
}
