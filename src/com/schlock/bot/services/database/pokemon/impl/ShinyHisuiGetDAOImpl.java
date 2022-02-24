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
        String totalOutbreakShiny = " select count(g) " +
                                    " from ShinyHisuiGet g " +
                                    " where g.resets != null ";

        String totalOutbreakResets = " select sum(g.resets) " +
                                        " from ShinyHisuiGet g " +
                                        " where g.resets != null ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query1 = session.createQuery(totalOutbreakShiny);
        Long totalShiny = (Long) query1.list().get(0);

        Query query2 = session.createQuery(totalOutbreakResets);
        Long totalResets = (Long) query2.list().get(0);

        session.getTransaction().commit();
        session.close();

        Double ave = totalResets.doubleValue() / totalShiny.doubleValue();
        return ave;
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
