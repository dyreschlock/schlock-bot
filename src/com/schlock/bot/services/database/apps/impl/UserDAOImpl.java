package com.schlock.bot.services.database.apps.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.services.database.impl.BaseDAOImpl;
import com.schlock.bot.services.database.apps.UserDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;

public class UserDAOImpl extends BaseDAOImpl<User> implements UserDAO
{
    public UserDAOImpl(SessionFactory sessionFactory)
    {
        super(User.class, sessionFactory);
    }

    public User getByUsername(String username)
    {
        String text = "from User u " +
                        " where u.username = :name ";

        Session session = getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createQuery(text, User.class);
        query.setParameter("name", username);

        User user = singleResult(query);

        session.getTransaction().commit();
        session.close();

        return user;
    }
}
