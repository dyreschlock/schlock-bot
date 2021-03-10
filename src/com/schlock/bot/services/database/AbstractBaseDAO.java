package com.schlock.bot.services.database;

import com.schlock.bot.entities.Persisted;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

public abstract class AbstractBaseDAO<T> implements BaseDAO<T>
{
    protected final Session session;

    private final Class entityClass;

    public AbstractBaseDAO(Class<T> entityClass, Session session)
    {
        this.entityClass = entityClass;
        this.session = session;
    }

    public List<T> getAll()
    {
        String text = String.format("from %s order by id", entityClass.getName());

        Query query = session.createQuery(text);

        List results = query.list();
        return results;
    }

    public T getById(Long id)
    {
        Object result = session.load(entityClass, id);
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

    @Override
    public void save(Persisted... objs)
    {
        for(Persisted obj : objs)
        {
            save(obj);
        }
    }

    @Override
    public void save(Persisted o)
    {
        if (o.getId() == null)
        {
            session.save(o);
        }
        else
        {
            session.update(o);
        }
    }

    @Override
    public void delete(Persisted... objs)
    {
        for(Persisted obj : objs)
        {
            delete(obj);
        }
    }

    @Override
    public void delete(Persisted o)
    {
        session.delete(o);
    }

    @Override
    public void commit()
    {
        session.getTransaction().commit();
    }
}
