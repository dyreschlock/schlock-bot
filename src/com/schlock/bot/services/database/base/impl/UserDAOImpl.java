package com.schlock.bot.services.database.base.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.services.database.base.UserDAO;
import com.schlock.bot.services.database.AbstractBaseDAO;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

public class UserDAOImpl extends AbstractBaseDAO<User> implements UserDAO
{
    public UserDAOImpl(Session session)
    {
        super(User.class, session);
    }

    public User getByUsername(String username)
    {
        String text = "from User u " +
                        " where u.username = :name ";

        Query query = session.createQuery(text);
        query.setParameter("name", username);

        User user = singleResult(query);
        return user;
    }

    public User getMostRecentUser()
    {
        String text = " from User u " +
                        " order by u.followDate desc ";

        Query query = session.createQuery(text);

        User user = singleResult(query);
        return user;
    }

    public List<User> getOrderByPoints(int count)
    {
        String text = "from User u " +
                        " order by u.balance desc ";

        Query query = session.createQuery(text);
        query.setMaxResults(count);

        return query.list();
    }
}
