package com.schlock.bot.services.database.impl;

import com.schlock.bot.services.database.BaseDAO;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

public abstract class BaseDAOImpl<T> implements BaseDAO<T>
{
    protected final Session session;

    private final Class entityClass;

    public BaseDAOImpl(Class<T> entityClass, Session session)
    {
        this.entityClass = entityClass;
        this.session = session;
    }

    public List<T> getAll()
    {
        String text = String.format("from %s order by id", entityClass.getName());

        Query query = session.createQuery(text);
        List results = query.list();

        session.getTransaction().commit();

        return results;
    }

    public T getById(Long id)
    {
        Object result = session.load(entityClass, id);

        session.getTransaction().commit();

        return (T) result;
    }

    protected T singleResult(Query query)
    {
        List<T> list = query.list();
        if (list.isEmpty())
        {
            return null;
        }
        return list.get(0);
    }
}
