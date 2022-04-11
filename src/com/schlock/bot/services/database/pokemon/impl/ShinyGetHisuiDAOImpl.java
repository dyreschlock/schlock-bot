package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.ShinyGetHisui;
import com.schlock.bot.entities.pokemon.ShinyGetType;
import com.schlock.bot.services.database.AbstractBaseDAO;
import com.schlock.bot.services.database.pokemon.ShinyGetHisuiDAO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class ShinyGetHisuiDAOImpl extends AbstractBaseDAO<ShinyGetHisui> implements ShinyGetHisuiDAO
{
    public ShinyGetHisuiDAOImpl(SessionFactory sessionFactory)
    {
        super(ShinyGetHisui.class, sessionFactory);
    }

    public Double getCurrentResetAverage()
    {
        String totalOutbreakShiny = " select count(g) " +
                                    " from ShinyGetHisui g " +
                                    " where g.resets != null " +
                                    " and g.method = :method ";

        String totalOutbreakResets = " select sum(g.resets) " +
                                        " from ShinyGetHisui g " +
                                        " where g.resets != null " +
                                        " and g.method = :method ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query1 = session.createQuery(totalOutbreakShiny);
        query1.setParameter("method", ShinyGetType.OUTBREAK);
        Long totalShiny = (Long) query1.list().get(0);

        Query query2 = session.createQuery(totalOutbreakResets);
        query2.setParameter("method", ShinyGetType.OUTBREAK);
        Long totalResets = (Long) query2.list().get(0);

        session.getTransaction().commit();
        session.close();

        Double ave = totalResets.doubleValue() / totalShiny.doubleValue();
        return ave;
    }

    public Integer getCurrentShinyNumber()
    {
        String text = " select count(g) from ShinyGetHisui g ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);
        Long count = (Long) query.list().get(0);

        session.getTransaction().commit();
        session.close();

        Integer next = count.intValue() + 1;
        return next;
    }

    public Integer getCurrentAlphaNumber()
    {
        String text = " select count(g) from ShinyGetHisui g " +
                " where g.alphaNumber is not null ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);
        Long count = (Long) query.list().get(0);

        session.getTransaction().commit();
        session.close();

        Integer next = count.intValue() + 1;
        return next;
    }

    public ShinyGetHisui getMostRecent()
    {
        String text = " from ShinyGetHisui g " +
                        " order by g.shinyNumber desc ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);

        ShinyGetHisui shinyGet = singleResult(query);

        session.getTransaction().commit();
        session.close();

        return shinyGet;
    }
}
