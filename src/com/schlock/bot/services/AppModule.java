package com.schlock.bot.services;

import com.schlock.bot.services.bot.BotModule;
import com.schlock.bot.services.database.DatabaseModule;
import com.schlock.bot.services.impl.DeploymentConfigurationImpl;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.EagerLoad;
import org.apache.tapestry5.ioc.annotations.ImportModule;

import java.io.IOException;

@ImportModule({
        DatabaseModule.class,
        BotModule.class
})
public class AppModule
{
    public static void bind(ServiceBinder binder)
    {
    }

    @EagerLoad
    public static DeploymentConfiguration build()
    {
        DeploymentConfiguration config = new DeploymentConfigurationImpl();

        try
        {
            config.loadProperties();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return config;
    }
}
