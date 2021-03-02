package com.schlock.bot.services.database;

import com.schlock.bot.AppTestCase;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.impl.DeploymentConfigurationImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public abstract class DatabaseTest extends AppTestCase
{
    protected SessionFactory sessionFactory;
    protected Session session;


    @BeforeEach
    public void abstractBefore() throws Exception
    {
        DeploymentConfiguration config = config();

        final String username = config.getHibernateProperty(DatabaseModule.HIBERNATE_USERNAME);
        final String password = config.getHibernateProperty(DatabaseModule.HIBERNATE_PASSWORD);
        final String url = config.getHibernateProperty(DatabaseModule.HIBERNATE_URL);

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();


        Configuration dbconfig = new Configuration();

        dbconfig.configure(StandardServiceRegistryBuilder.DEFAULT_CFG_RESOURCE_NAME);

        dbconfig.setProperty(DatabaseModule.HIBERNATE_USERNAME, username);
        dbconfig.setProperty(DatabaseModule.HIBERNATE_PASSWORD, password);
        dbconfig.setProperty(DatabaseModule.HIBERNATE_URL, url);

        dbconfig.setProperty(DatabaseModule.HIBERNATE_HIKARI_USERNAME, username);
        dbconfig.setProperty(DatabaseModule.HIBERNATE_HIKARI_PASSWORD, password);
        dbconfig.setProperty(DatabaseModule.HIBERNATE_HIKARI_URL, url);

        sessionFactory = dbconfig.buildSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();

        before();
    }

    protected abstract void before() throws Exception;

    @AfterEach
    public void abstractAfter() throws Exception
    {
        Exception exception = null;
        try
        {
            after();
        }
        catch (Exception e)
        {
            exception = e;
        }

        session.getTransaction().commit();
        session.close();
        sessionFactory.close();

        if (exception != null) {
            throw exception;
        }
    }

    protected abstract void after() throws Exception;
}
