package com.schlock.bot.services;

import com.schlock.bot.entities.Persisted;
import com.schlock.bot.services.database.BaseDAO;
import com.schlock.bot.services.database.bet.BetDAO;
import com.schlock.bot.services.database.bet.BettingUserDAO;
import com.schlock.bot.services.database.bet.impl.BetDAOImpl;
import com.schlock.bot.services.database.bet.impl.BettingUserDAOImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.cfgxml.spi.LoadedConfig;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.*;

public class DatabaseModule
{
    private final DeploymentContext context;

    private SessionFactory sessionFactory;

    private Map<Class, BaseDAO> daos = new HashMap<>();

    public DatabaseModule(DeploymentContext context)
    {
        this.context = context;
    }

    private void createDAOs()
    {
        daos.put(BetDAO.class, new BetDAOImpl(sessionFactory));
        daos.put(BettingUserDAO.class, new BettingUserDAOImpl(sessionFactory));
    }

    public <T> T get(Class<T> dao)
    {
        return (T) daos.get(dao);
    }

    public void save(Persisted obj)
    {
        save(Arrays.asList(obj));
    }

    public void save(List<Persisted> obj)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        for (Persisted o : obj)
        {
            session.save(o);
        }

        session.getTransaction().commit();
        session.close();
    }


    private final static String HIBERNATE_USERNAME = "hibernate.connection.username";
    private final static String HIBERNATE_PASSWORD = "hibernate.connection.password";
    private final static String HIBERNATE_URL = "hibernate.connection.url";

    private final static String HIBERNATE_HIKARI_USERNAME = "hibernate.hikari.dataSource.user";
    private final static String HIBERNATE_HIKARI_PASSWORD = "hibernate.hikari.dataSource.password";
    private final static String HIBERNATE_HIKARI_URL = "hibernate.hikari.dataSource.url";

    public void setup() throws Exception
    {
        final String username = context.getHibernateProperty(HIBERNATE_USERNAME);
        final String password = context.getHibernateProperty(HIBERNATE_PASSWORD);
        final String url = context.getHibernateProperty(HIBERNATE_URL) + "?characterEncoding=utf-8";


        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
        LoadedConfig configuration = builder.getConfigLoader().loadConfigXmlResource(StandardServiceRegistryBuilder.DEFAULT_CFG_RESOURCE_NAME);

        configuration.getConfigurationValues().put(HIBERNATE_USERNAME, username);
        configuration.getConfigurationValues().put(HIBERNATE_PASSWORD, password);
        configuration.getConfigurationValues().put(HIBERNATE_URL, url);

        configuration.getConfigurationValues().put(HIBERNATE_HIKARI_USERNAME, username);
        configuration.getConfigurationValues().put(HIBERNATE_HIKARI_PASSWORD, password);
        configuration.getConfigurationValues().put(HIBERNATE_HIKARI_URL, url);

        final StandardServiceRegistry registry = builder.configure(configuration).build();

        try
        {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        }
        catch (Exception e)
        {
            StandardServiceRegistryBuilder.destroy(registry);
            throw e;
        }


        createDAOs();
    }

}
