package com.schlock.bot.services.database;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public abstract class AbstractBaseDAO<T> implements BaseDAO<T>
{
    protected final SessionFactory sessionFactory;

    private final Class entityClass;

    public AbstractBaseDAO(Class<T> entityClass, SessionFactory sessionFactory)
    {
        this.entityClass = entityClass;
        this.sessionFactory = sessionFactory;
    }

    public List<T> getAll()
    {
        String text = String.format("from %s order by id", entityClass.getName());

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);
        List results = query.list();

        session.getTransaction().commit();
        session.close();

        return results;
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
