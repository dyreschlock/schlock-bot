package com.schlock.bot.services.database;

import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.database.adhoc.DatabaseManager;
import com.schlock.bot.services.database.adhoc.impl.DatabaseManagerImpl;
import org.apache.tapestry5.hibernate.HibernateConfigurer;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class DatabaseModule
{
    public static void bind(ServiceBinder binder)
    {
        binder.bind(DatabaseManager.class, DatabaseManagerImpl.class);
    }


    public final static String HIBERNATE_USERNAME = "hibernate.connection.username";
    public final static String HIBERNATE_PASSWORD = "hibernate.connection.password";
    public final static String HIBERNATE_URL = "hibernate.connection.url";

    public final static String HIBERNATE_HIKARI_USERNAME = "hibernate.hikari.dataSource.user";
    public final static String HIBERNATE_HIKARI_PASSWORD = "hibernate.hikari.dataSource.password";
    public final static String HIBERNATE_HIKARI_URL = "hibernate.hikari.dataSource.url";


    public void contributeHibernateSessionSource(OrderedConfiguration<HibernateConfigurer> configuration,
                                                 @Inject DeploymentConfiguration config)
    {
        final String username = config.getHibernateProperty(HIBERNATE_USERNAME);
        final String password = config.getHibernateProperty(HIBERNATE_PASSWORD);
        final String url = config.getHibernateProperty(HIBERNATE_URL) + "?characterEncoding=utf-8";

        HibernateConfigurer configurer = new HibernateConfigurer() {

            public void configure(org.hibernate.cfg.Configuration configuration)
            {
                configuration.configure(StandardServiceRegistryBuilder.DEFAULT_CFG_RESOURCE_NAME);

                configuration.setProperty(HIBERNATE_USERNAME, username);
                configuration.setProperty(HIBERNATE_PASSWORD, password);
                configuration.setProperty(HIBERNATE_URL, url);

                configuration.setProperty(HIBERNATE_HIKARI_USERNAME, username);
                configuration.setProperty(HIBERNATE_HIKARI_PASSWORD, password);
                configuration.setProperty(HIBERNATE_HIKARI_URL, url);
            }
        };

        configuration.add("hibernate-session-source", configurer);
    }
}
