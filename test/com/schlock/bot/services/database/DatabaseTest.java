package com.schlock.bot.services.database;

import com.schlock.bot.services.DatabaseModule;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.impl.DeploymentConfigurationImpl;

public abstract class DatabaseTest
{
    private DeploymentConfiguration config;
    private DatabaseModule database;


    protected DeploymentConfiguration getDeploymentConfiguration()
    {
        return config;
    }

    protected DatabaseModule getDatabase()
    {
        return database;
    }

    protected void setupDatabase() throws Exception
    {
        config = createDeploymentConfiguration();
        config.loadProperties();

        database = new DatabaseModule(config);
        database.setup();
    }

    protected DeploymentConfiguration createDeploymentConfiguration()
    {
        return new DeploymentConfigurationImpl(DeploymentConfiguration.TEST);
    }
}
