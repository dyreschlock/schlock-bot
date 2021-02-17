package com.schlock.bot.services.database.apps.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.bet.ShinyBet;
import com.schlock.bot.services.database.impl.BaseDAOImpl;
import com.schlock.bot.services.database.apps.ShinyBetDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import java.util.List;

public class ShinyBetDAOImpl extends BaseDAOImpl<ShinyBet> implements ShinyBetDAO
{
    public ShinyBetDAOImpl(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    public List<ShinyBet> getBetsByUsername(String username)
    {
        String text = " select b " +
                        "from ShinyBet b " +
                        " join b.user u " +
                        " where u.username = :name ";

        Session session = getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createQuery(text, ShinyBet.class);
        query.setParameter("name", username);

        List<ShinyBet> bets = query.getResultList();

        session.getTransaction().commit();
        session.close();

        return bets;
    }
}
