package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.ShinyGetLetsGo;
import com.schlock.bot.entities.pokemon.ShinyGetType;
import com.schlock.bot.services.database.pokemon.ShinyGetLetsGoDAO;
import com.schlock.bot.services.database.AbstractBaseDAO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class ShinyGetLetsGoDAOImpl extends AbstractBaseDAO<ShinyGetLetsGo> implements ShinyGetLetsGoDAO
{
    public ShinyGetLetsGoDAOImpl(SessionFactory sessionFactory)
    {
        super(ShinyGetLetsGo.class, sessionFactory);
    }

    public Double getCurrentAverageTimeToShiny()
    {
        String totalTimeText = " select sum(g.timeInMinutes) " +
                                " from ShinyGetLetsGo g " +
                                " where g.type != :type ";

        String totalShinyText = " select count(g) " +
                                    " from ShinyGetLetsGo g " +
                                    " where g.type != :type ";


        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query1 = session.createQuery(totalShinyText);
        query1.setParameter("type", ShinyGetType.RESET);

        Query query2 = session.createQuery(totalTimeText);
        query2.setParameter("type", ShinyGetType.RESET);

        Long totalTime = (Long) query2.list().get(0);
        Long totalShiny = (Long) query1.list().get(0);

        session.getTransaction().commit();
        session.close();

        Double ave = totalTime.doubleValue() / totalShiny.doubleValue();
        return ave;
    }

    public Double getCurrentAverageNumberOfRareChecks()
    {
        String totalRareShinyText = " select count(g) " +
                                        " from ShinyGetLetsGo g " +
                                        " where g.numOfRareChecks != null " +
                                        " and g.type != :type ";


        String totalRareChecksText = " select sum(g.numOfRareChecks)" +
                                        " from ShinyGetLetsGo g " +
                                        " where g.numOfRareChecks != null " +
                                        " and g.type != :type ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query1 = session.createQuery(totalRareShinyText);
        query1.setParameter("type", ShinyGetType.RESET);

        Query query2 = session.createQuery(totalRareChecksText);
        query2.setParameter("type", ShinyGetType.RESET);

        Long totalRareShiny = (Long) query1.list().get(0);
        Long totalRareChecks = (Long) query2.list().get(0);

        session.getTransaction().commit();
        session.close();

        Double ave = totalRareChecks.doubleValue() / totalRareShiny.doubleValue();
        return ave;
    }

    public Integer getCurrentShinyNumber()
    {
        String text = " select count(g) from ShinyGetLetsGo g ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);
        Long count = (Long) query.list().get(0);

        session.getTransaction().commit();
        session.close();

        Integer next = count.intValue() +1;
        return next;
    }

    public ShinyGetLetsGo getMostRecent()
    {
        String text = " from ShinyGetLetsGo g " +
                        " order by g.shinyNumber desc ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);

        ShinyGetLetsGo shinyGet = singleResult(query);

        session.getTransaction().commit();
        session.close();

        return shinyGet;
    }
}
