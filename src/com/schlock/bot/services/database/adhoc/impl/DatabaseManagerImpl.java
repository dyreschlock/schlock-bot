package com.schlock.bot.services.database.adhoc.impl;

import com.schlock.bot.entities.Persisted;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.database.BaseDAO;
import com.schlock.bot.services.database.DatabaseModule;
import com.schlock.bot.services.database.adhoc.DatabaseManager;
import com.schlock.bot.services.database.base.AlertDAO;
import com.schlock.bot.services.database.base.UserDAO;
import com.schlock.bot.services.database.base.impl.AlertDAOImpl;
import com.schlock.bot.services.database.base.impl.UserDAOImpl;
import com.schlock.bot.services.database.pokemon.*;
import com.schlock.bot.services.database.pokemon.impl.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.HashMap;
import java.util.Map;

public class DatabaseManagerImpl implements DatabaseManager
{
    private final DeploymentConfiguration config;

    private SessionFactory sessionFactory;

    private Map<Class, BaseDAO> daos;

    public DatabaseManagerImpl(DeploymentConfiguration config)
    {
        this.config = config;
    }

    public <T extends BaseDAO> T get(Class<T> dao)
    {
        setup();

        return (T) daos.get(dao);
    }

    @Override
    public synchronized void save(Persisted... obj)
    {
        setup();

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

    @Override
    public synchronized void delete(Persisted... obj)
    {
        setup();

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

    @Override
    public synchronized void commit()
    {
        setup();

        //nothing
    }

    private final static String HIBERNATE_USERNAME = DatabaseModule.HIBERNATE_USERNAME;
    private final static String HIBERNATE_PASSWORD = DatabaseModule.HIBERNATE_PASSWORD;
    private final static String HIBERNATE_URL = DatabaseModule.HIBERNATE_URL;

    private final static String HIBERNATE_HIKARI_USERNAME = DatabaseModule.HIBERNATE_HIKARI_USERNAME;
    private final static String HIBERNATE_HIKARI_PASSWORD = DatabaseModule.HIBERNATE_HIKARI_PASSWORD;
    private final static String HIBERNATE_HIKARI_URL = DatabaseModule.HIBERNATE_HIKARI_URL;

    private void setup()
    {
        if(sessionFactory != null) return;


        final String username = config.getHibernateProperty(HIBERNATE_USERNAME);
        final String password = config.getHibernateProperty(HIBERNATE_PASSWORD);
        final String url = config.getHibernateProperty(HIBERNATE_URL) + "?characterEncoding=utf-8";

        Configuration configuration = new Configuration();

        configuration.configure(StandardServiceRegistryBuilder.DEFAULT_CFG_RESOURCE_NAME);

        configuration.setProperty(HIBERNATE_USERNAME, username);
        configuration.setProperty(HIBERNATE_PASSWORD, password);
        configuration.setProperty(HIBERNATE_URL, url);

        configuration.setProperty(HIBERNATE_HIKARI_USERNAME, username);
        configuration.setProperty(HIBERNATE_HIKARI_PASSWORD, password);
        configuration.setProperty(HIBERNATE_HIKARI_URL, url);

        try
        {
            sessionFactory = configuration.buildSessionFactory();
        }
        catch (Exception e)
        {
            throw e;
        }

        createDAOs();
    }

    private void createDAOs()
    {
        daos = new HashMap<>();

        daos.put(AlertDAO.class, new AlertDAOImpl(sessionFactory));
        daos.put(UserDAO.class, new UserDAOImpl(sessionFactory));

        daos.put(GuessingStreakDAO.class, new GuessingStreakDAOImpl(sessionFactory));

        daos.put(ShinyDexEntryGoDAO.class, new ShinyDexEntryGoDAOImpl(sessionFactory));
        daos.put(ShinyDexEntryHisuiDAO.class, new ShinyDexEntryHisuiDAOImpl(sessionFactory));
        daos.put(ShinyDexEntryLetsGoDAO.class, new ShinyDexEntryLetsGoDAOImpl(sessionFactory));
        daos.put(ShinyDexEntryMainDAO.class, new ShinyDexEntryMainDAOImpl(sessionFactory));

        daos.put(ShinyBetLetsGoDAO.class, new ShinyBetLetsGoDAOImpl(sessionFactory));

        daos.put(ShinyGetLetsGoDAO.class, new ShinyGetLetsGoDAOImpl(sessionFactory));
        daos.put(ShinyGetHisuiDAO.class, new ShinyGetHisuiDAOImpl(sessionFactory));
    }
}
