package com.schlock.bot.services.database.apps.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.alert.Alert;
import com.schlock.bot.entities.apps.alert.AlertType;
import com.schlock.bot.entities.apps.alert.AnimationAlert;
import com.schlock.bot.entities.apps.alert.TwitchAlert;
import com.schlock.bot.services.database.DatabaseTest;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AlertDAOImplTest extends DatabaseTest
{
    private static final String USERNAME = "username1";

    private AlertDAOImpl alertDAO;

    private User user;

    private TwitchAlert twitchAlert;
    private AnimationAlert animeAlert;

    @Test
    public void testByUsername()
    {
        List<Alert> alerts = alertDAO.getByUser(user);

        assertEquals(2, alerts.size());

        boolean twitchAlertOk = false;
        boolean animationAlertOk = false;

        for (Alert alert : alerts)
        {
            if (alert.equals(twitchAlert))
            {
                twitchAlertOk = true;
            }
            else if (alert.equals(animeAlert))
            {
                animationAlertOk = true;
            }
        }

        assertTrue(twitchAlertOk);
        assertTrue(animationAlertOk);
    }

    @Test
    public void testMostRecent()
    {
        TwitchAlert t = alertDAO.getEarliestUnfinishedTwitchAlert();

        assertEquals(twitchAlert, t);

        AnimationAlert a = alertDAO.getEarliestUnfinishedAnimationAlert();

        assertEquals(animeAlert, a);
    }

    @Override
    protected void before() throws Exception
    {
        alertDAO = new AlertDAOImpl(session);

        createTestObjects();
    }

    @Override
    protected void after() throws Exception
    {
        removeTestObjects();
    }

    public void createTestObjects()
    {
        user = new User();
        user.setUsername(USERNAME);


        twitchAlert = new TwitchAlert();
        twitchAlert.setUser(user);
        twitchAlert.setRequestDate(new Date());
        twitchAlert.setType(AlertType.FOLLOWER);


        animeAlert = new AnimationAlert();
        animeAlert.setUser(user);
        animeAlert.setRequestDate(new Date());
        animeAlert.setType(AlertType.MONSTER_FRISBEE);


        alertDAO.save(user, twitchAlert, animeAlert);
    }

    public void removeTestObjects()
    {
        alertDAO.delete(twitchAlert, animeAlert, user);
    }
}