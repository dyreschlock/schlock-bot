package com.schlock.bot.services.database;

import org.hibernate.SessionFactory;

import javax.persistence.Query;
import java.util.List;

public abstract class BaseDAOImpl<T> implements BaseDAO<T>
{
    private final SessionFactory sessionFactory;

    public BaseDAOImpl(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    protected SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    public List<T> getAll()
    {
        return null;
    }

    public T getById(Long id)
    {
        return null;
    }

    protected T singleResult(Query query)
    {
        List<T> list = query.getResultList();
        if (list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }
}
