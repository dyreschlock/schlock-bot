package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.ShinyGet;
import com.schlock.bot.services.database.pokemon.ShinyGetDAO;
import com.schlock.bot.services.database.AbstractBaseDAO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class ShinyGetDAOImpl extends AbstractBaseDAO<ShinyGet> implements ShinyGetDAO
{
    public ShinyGetDAOImpl(SessionFactory sessionFactory)
    {
        super(ShinyGet.class, sessionFactory);
    }

    public Double getCurrentAverageTimeToShiny()
    {
        String totalShinyText = " select count(g) from ShinyGet g ";
        String totalTimeText = " select sum(g.timeInMinutes) from ShinyGet g ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query1 = session.createQuery(totalShinyText);
        Long totalShiny = (Long) query1.list().get(0);

        Query query2 = session.createQuery(totalTimeText);
        Long totalTime = (Long) query2.list().get(0);

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

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query1 = session.createQuery(totalRareShinyText);
        Long totalRareShiny = (Long) query1.list().get(0);

        Query query2 = session.createQuery(totalRareChecksText);
        Long totalRareChecks = (Long) query2.list().get(0);

        session.getTransaction().commit();
        session.close();

        Double ave = totalRareChecks.doubleValue() / totalRareShiny.doubleValue();
        return ave;
    }

    public Integer getCurrentShinyNumber()
    {
        String text = " select count(g) from ShinyGet g ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);
        Long count = (Long) query.list().get(0);

        session.getTransaction().commit();
        session.close();

        Integer next = count.intValue() +1;
        return next;
    }

    public ShinyGet getMostRecent()
    {
        String text = " from ShinyGet g " +
                        " order by g.shinyNumber desc ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);

        ShinyGet shinyGet = singleResult(query);

        session.getTransaction().commit();
        session.close();

        return shinyGet;
    }
}
