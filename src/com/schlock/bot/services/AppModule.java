package com.schlock.bot.services;

import com.schlock.bot.services.impl.DeploymentContextImpl;
import org.apache.tapestry5.ioc.ServiceBinder;

public class AppModule
{
    public static void bind(ServiceBinder binder)
    {
        binder.bind(DeploymentContext.class, DeploymentContextImpl.class);

        String temp = "";
    }


}
