package com.schlock.bot.services.database.apps.impl;

import com.schlock.bot.entities.apps.pokemon.ShinyGet;
import com.schlock.bot.services.database.apps.ShinyGetDAO;
import com.schlock.bot.services.database.impl.BaseDAOImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;

public class ShinyGetDAOImpl extends BaseDAOImpl<ShinyGet> implements ShinyGetDAO
{
    public ShinyGetDAOImpl(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    public Double getCurrentAverageTimeToShiny()
    {
        String totalShinyText = " select count(g) from ShinyGet g ";
        String totalTimeText = " select sum(g.timeInMinutes) from ShinyGet g ";

        Session session = getSessionFactory().openSession();
        session.beginTransaction();

        Query query1 = session.createQuery(totalShinyText, Long.class);
        Long totalShiny = (Long) query1.getSingleResult();

        Query query2 = session.createQuery(totalTimeText, Long.class);
        Long totalTime = (Long) query2.getSingleResult();

        session.getTransaction().commit();
        session.close();

        Double ave = totalTime.doubleValue() / totalShiny.doubleValue();
        return ave;
    }

    public Double getCurrentAverageNumberOfRareChecks()
    {
        String totalRareShinyText = " select count(g) " +
                                        " from ShinyGet g " +
                                        " where g.numOfRareChecks != null ";


        String totalRareChecksText = " select sum(g.numOfRareChecks)" +
                                        " from ShinyGet g " +
                                        " where g.numOfRareChecks != null ";

        Session session = getSessionFactory().openSession();
        session.beginTransaction();

        Query query1 = session.createQuery(totalRareShinyText, Long.class);
        Long totalRareShiny = (Long) query1.getSingleResult();

        Query query2 = session.createQuery(totalRareChecksText, Long.class);
        Long totalRareChecks = (Long) query2.getSingleResult();

        session.getTransaction().commit();
        session.close();

        Double ave = totalRareChecks.doubleValue() / totalRareShiny.doubleValue();
        return ave;
    }

    public Integer getCurrentShinyNumber()
    {
        String text = " select count(g) from ShinyGet g ";

        Session session = getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createQuery(text, Long.class);
        Long count = (Long) query.getSingleResult();

        session.getTransaction().commit();
        session.close();

        Integer next = count.intValue() +1;
        return next;
    }

    public ShinyGet getMostRecent()
    {
        String text = " from ShinyGet g " +
                        " order by g.shinyNumber desc ";

        Session session = getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createQuery(text, ShinyGet.class);

        ShinyGet shinyGet = singleResult(query);

        session.getTransaction().commit();
        session.close();

        return shinyGet;
    }
}
