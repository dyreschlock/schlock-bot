package com.schlock.bot.services.database;

import com.schlock.bot.entities.Persisted;

import java.util.List;

public interface BaseDAO<T>
{
    List<T> getAll();

    T getById(Long id);

    void save(Persisted obj);

    void save(Persisted... obj);

    void delete(Persisted obj);

    void delete(Persisted... obj);
}
