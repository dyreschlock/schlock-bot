package com.schlock.bot.services.database;

import com.schlock.bot.services.StandaloneDatabase;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.impl.DeploymentConfigurationImpl;

public abstract class DatabaseTest
{
    private DeploymentConfiguration config;
    private StandaloneDatabase database;


    protected DeploymentConfiguration getDeploymentConfiguration()
    {
        return config;
    }

    protected StandaloneDatabase getDatabase()
    {
        return database;
    }

    protected void setupDatabase() throws Exception
    {
        config = createDeploymentConfiguration();
        config.loadProperties();

        database = new StandaloneDatabase(config);
        database.setup();
    }

    protected DeploymentConfiguration createDeploymentConfiguration()
    {
        return DeploymentConfigurationImpl.createDeploymentConfiguration(DeploymentConfigurationImpl.TEST);
    }
}
