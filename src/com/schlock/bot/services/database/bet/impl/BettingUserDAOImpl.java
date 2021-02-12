package com.schlock.bot.services.database.bet.impl;

import com.schlock.bot.entities.bet.BettingUser;
import com.schlock.bot.services.database.BaseDAOImpl;
import com.schlock.bot.services.database.bet.BettingUserDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class BettingUserDAOImpl extends BaseDAOImpl<BettingUser> implements BettingUserDAO
{
    public BettingUserDAOImpl(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    public BettingUser getByUsername(String username)
    {
        String text = "from BettingUser u " +
                        " where u.username = :name ";

        Session session = getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createQuery(text, BettingUser.class);
        query.setParameter("name", username);

        BettingUser user = singleResult(query);

        session.getTransaction().commit();
        session.close();

        return user;
    }
}
