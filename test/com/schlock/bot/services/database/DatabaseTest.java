package com.schlock.bot.services.database;

import com.schlock.bot.AppTestCase;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.database.adhoc.DatabaseManager;
import com.schlock.bot.services.database.adhoc.impl.DatabaseManagerImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class DatabaseTest extends AppTestCase
{
    protected DatabaseManager database;

    @BeforeEach
    public void abstractBefore() throws Exception
    {
        DeploymentConfiguration config = config();

        database = new DatabaseManagerImpl(config);

        before();
    }

    protected abstract void before() throws Exception;

    @AfterEach
    public void abstractAfter() throws Exception
    {
        after();
    }

    protected abstract void after() throws Exception;
}
