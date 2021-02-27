package com.schlock.bot.services;

import com.schlock.bot.services.bot.DiscordBotModule;
import com.schlock.bot.services.impl.DeploymentContextImpl;
import com.schlock.bot.services.bot.TwitchBotModule;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.ImportModule;

@ImportModule({
        DatabaseModule.class,
        DiscordBotModule.class,
        TwitchBotModule.class
})
public class AppModule
{
    public static void bind(ServiceBinder binder)
    {
        binder.bind(DeploymentContext.class, DeploymentContextImpl.class);

        String temp = "";
    }


}
