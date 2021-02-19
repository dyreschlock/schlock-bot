package com.schlock.bot.services.database.impl;

import com.schlock.bot.services.database.BaseDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.Query;
import java.util.List;

public abstract class BaseDAOImpl<T> implements BaseDAO<T>
{
    private final SessionFactory sessionFactory;

    private final Class entityClass;

    public BaseDAOImpl(Class<T> entityClass,SessionFactory sessionFactory)
    {
        this.entityClass = entityClass;
        this.sessionFactory = sessionFactory;
    }

    protected SessionFactory getSessionFactory()
    {
        return sessionFactory;
    }

    public List<T> getAll()
    {
        String text = String.format("from %s order by id", entityClass.getName());

        Session session = getSessionFactory().openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);
        List results = query.getResultList();

        session.getTransaction().commit();
        session.close();

        return results;
    }

    public T getById(Long id)
    {
        Session session = getSessionFactory().openSession();
        session.beginTransaction();

        Object result = session.load(entityClass, id);

        session.getTransaction().commit();
        session.close();

        return (T) result;
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
