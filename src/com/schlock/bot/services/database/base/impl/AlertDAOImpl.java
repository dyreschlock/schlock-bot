package com.schlock.bot.services.database.base.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.entities.base.alert.Alert;
import com.schlock.bot.entities.base.alert.AnimationAlert;
import com.schlock.bot.entities.base.alert.TwitchAlert;
import com.schlock.bot.services.database.base.AlertDAO;
import com.schlock.bot.services.database.AbstractBaseDAO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

public class AlertDAOImpl extends AbstractBaseDAO<Alert> implements AlertDAO
{
    public AlertDAOImpl(SessionFactory sessionFactory)
    {
        super(Alert.class, sessionFactory);
    }

    public List<Alert> getByUser(User user)
    {
        String text = " select a " +
                        " from Alert a, User u " +
                        " where u.username = :name " +
                        " and u.id = a.userId ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);
        query.setParameter("name", user.getUsername());

        List<Alert> alerts = query.list();

        session.getTransaction().commit();
        session.close();

        return alerts;
    }

    @Override
    public TwitchAlert getEarliestUnfinishedTwitchAlert()
    {
        return (TwitchAlert) getEarliestUnfinishedByClass(TwitchAlert.class);
    }

    @Override
    public AnimationAlert getEarliestUnfinishedAnimationAlert()
    {
        return (AnimationAlert) getEarliestUnfinishedByClass(AnimationAlert.class);
    }

    private Alert getEarliestUnfinishedByClass(Class classType)
    {
        String text = " from " + classType.getSimpleName() + " a " +
                        " where a.finishDate is null " +
                        " order by a.requestDate asc ";

        Session session = sessionFactory.openSession();
        session.beginTransaction();

        Query query = session.createQuery(text);

        Alert alert = singleResult(query);

        session.getTransaction().commit();
        session.close();

        return alert;
    }
}
