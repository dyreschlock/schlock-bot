package com.schlock.bot.services.database.base.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.services.database.base.UserDAO;
import com.schlock.bot.services.database.AbstractBaseDAO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Collections;
import java.util.List;

public class UserDAOImpl extends AbstractBaseDAO<User> implements UserDAO
{
    public UserDAOImpl(SessionFactory sessionFactory)
    {
        super(User.class, sessionFactory);
    }

    public User getByUsername(String username)
    {
        String text = "from User u " +
                        " where u.username = :name ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);
        query.setParameter("name", username);

        User user = singleResult(query);

        session.getTransaction().commit();
        session.close();

        return user;
    }

    public User getMostRecentUser()
    {
        String text = " from User u " +
                        " order by u.followDate desc ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);

        User user = singleResult(query);

        session.getTransaction().commit();
        session.close();

        return user;
    }

    public List<User> getOrderByPoints(int count)
    {
        return getOrderByPoints(count, Collections.emptyList());
    }

    public List<User> getOrderByPoints(int count, List<String> ignoreUsers)
    {
        String text = "";

        text += "from User u ";
        if (!ignoreUsers.isEmpty())
        {
            text += " where username not in (:ignore) ";
        }
        text += " order by u.prestige desc, u.balance desc ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);
        query.setMaxResults(count);
        if (!ignoreUsers.isEmpty())
        {
            query.setParameterList("ignore", ignoreUsers);
        }

        List<User> users = query.list();

        session.getTransaction().commit();
        session.close();

        return users;
    }
}
