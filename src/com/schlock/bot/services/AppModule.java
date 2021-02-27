package com.schlock.bot.services;

import com.schlock.bot.services.bot.BotModule;
import com.schlock.bot.services.database.DatabaseModule;
import com.schlock.bot.services.impl.DeploymentConfigurationImpl;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.ImportModule;

@ImportModule({
        DatabaseModule.class,
        BotModule.class
})
public class AppModule
{
    public static void bind(ServiceBinder binder)
    {
        binder.bind(DeploymentConfiguration.class, DeploymentConfigurationImpl.class);
    }
}
