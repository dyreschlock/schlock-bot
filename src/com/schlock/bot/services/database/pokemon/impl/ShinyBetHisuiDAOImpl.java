package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.ShinyBetHisui;
import com.schlock.bot.services.database.AbstractBaseDAO;
import com.schlock.bot.services.database.pokemon.ShinyBetHisuiDAO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class ShinyBetHisuiDAOImpl extends AbstractBaseDAO<ShinyBetHisui> implements ShinyBetHisuiDAO
{
    public ShinyBetHisuiDAOImpl(SessionFactory sessionFactory)
    {
        super(ShinyBetHisui.class, sessionFactory);
    }

    public ShinyBetHisui getByUsername(String username)
    {
        String text = " select b " +
                        " from ShinyBetHisui b " +
                        " join b.user u " +
                        " where u.username = :name " +
                        " and b.shiny is null ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);
        query.setParameter("name", username);

        List<ShinyBetHisui> bets = query.list();

        session.getTransaction().commit();
        session.close();

        if (bets.size() == 0)
        {
            return null;
        }
        return bets.get(0);
    }

    public List<ShinyBetHisui> getAllCurrent()
    {
        String text = " from ShinyBetHisui b" +
                        " where b.shiny is null ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);

        List<ShinyBetHisui> bets = query.list();

        session.getTransaction().commit();
        session.close();

        return bets;
    }
}
