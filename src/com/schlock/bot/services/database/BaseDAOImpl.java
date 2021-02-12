package com.schlock.bot.services.database;

import org.hibernate.SessionFactory;

import java.util.List;

public abstract class BaseDAOImpl<T> implements BaseDAO<T>
{
    private final SessionFactory sessionFactory;

    public BaseDAOImpl(SessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }

    public List<T> getAll()
    {
        return null;
    }

    public T getById(Long id)
    {
        return null;
    }

    public void save(T obj)
    {

    }
}
