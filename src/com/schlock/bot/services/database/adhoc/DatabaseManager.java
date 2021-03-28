package com.schlock.bot.services.database.adhoc;

import com.schlock.bot.entities.Persisted;
import com.schlock.bot.services.database.BaseDAO;

public interface DatabaseManager
{
    <T extends BaseDAO> T get(Class<T> dao);

    void save(Persisted... obj);

    void delete(Persisted... obj);

    void commit();
}
