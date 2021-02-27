package com.schlock.bot.services;

import com.schlock.bot.entities.Persisted;
import com.schlock.bot.services.database.BaseDAO;
import com.schlock.bot.services.database.apps.ShinyBetDAO;
import com.schlock.bot.services.database.apps.ShinyGetDAO;
import com.schlock.bot.services.database.apps.UserDAO;
import com.schlock.bot.services.database.apps.impl.ShinyBetDAOImpl;
import com.schlock.bot.services.database.apps.impl.ShinyGetDAOImpl;
import com.schlock.bot.services.database.apps.impl.UserDAOImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.cfgxml.spi.LoadedConfig;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.*;

public class StandaloneDatabase
{
    private final DeploymentConfiguration config;

    private SessionFactory sessionFactory;

    private Map<Class, BaseDAO> daos = new HashMap<>();

    public StandaloneDatabase(DeploymentConfiguration config)
    {
        this.config = config;
    }

    private void createDAOs()
    {
//        daos.put(ShinyBetDAO.class, new ShinyBetDAOImpl(sessionFactory));
//        daos.put(UserDAO.class, new UserDAOImpl(sessionFactory));
//        daos.put(ShinyGetDAO.class, new ShinyGetDAOImpl(sessionFactory));
    }

    public <T> T get(Class<T> dao)
    {
        return (T) daos.get(dao);
    }

    public void save(Persisted obj)
    {
        save(Arrays.asList(obj));
    }

    public void save(Collection<Persisted> obj)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        for (Persisted o : obj)
        {
            if (o.getId() == null)
            {
                session.save(o);
            }
            else
            {
                session.update(o);
            }
        }

        session.getTransaction().commit();
        session.close();
    }

    public void delete(Persisted obj)
    {
        delete(Arrays.asList(obj));
    }

    public void delete(List<Persisted> obj)
    {
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        for (Persisted o : obj)
        {
            Object instance = session.load(o.getClass(), o.getId());
            if (instance != null)
            {
                session.delete(o);
            }
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
//        final String username = config.getHibernateProperty(HIBERNATE_USERNAME);
//        final String password = config.getHibernateProperty(HIBERNATE_PASSWORD);
//        final String url = config.getHibernateProperty(HIBERNATE_URL) + "?characterEncoding=utf-8";
//
//
//        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();
//        LoadedConfig configuration = builder.getConfigLoader().loadConfigXmlResource(StandardServiceRegistryBuilder.DEFAULT_CFG_RESOURCE_NAME);
//
//        configuration.getConfigurationValues().put(HIBERNATE_USERNAME, username);
//        configuration.getConfigurationValues().put(HIBERNATE_PASSWORD, password);
//        configuration.getConfigurationValues().put(HIBERNATE_URL, url);
//
//        configuration.getConfigurationValues().put(HIBERNATE_HIKARI_USERNAME, username);
//        configuration.getConfigurationValues().put(HIBERNATE_HIKARI_PASSWORD, password);
//        configuration.getConfigurationValues().put(HIBERNATE_HIKARI_URL, url);
//
//        final StandardServiceRegistry registry = builder.configure(configuration).build();
//
//        try
//        {
//            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
//        }
//        catch (Exception e)
//        {
//            StandardServiceRegistryBuilder.destroy(registry);
//            throw e;
//        }
//
//
//        createDAOs();
    }

}
