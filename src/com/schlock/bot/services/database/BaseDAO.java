package com.schlock.bot.services.database;

import java.util.List;

public interface BaseDAO<T>
{
    List<T> getAll();
}
