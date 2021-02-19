package com.schlock.bot.services.database;

import com.schlock.bot.services.DatabaseModule;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.impl.DeploymentContextImpl;

public abstract class DatabaseTest
{
    private DeploymentContext context;
    private DatabaseModule database;


    protected DeploymentContext getDeploymentContext()
    {
        return context;
    }

    protected DatabaseModule getDatabase()
    {
        return database;
    }

    protected void setupDatabase() throws Exception
    {
        context = createDeploymentContext();
        context.loadProperties();

        database = new DatabaseModule(context);
        database.setup();
    }

    protected DeploymentContext createDeploymentContext()
    {
        return new DeploymentContextImpl(DeploymentContext.TEST);
    }
}
