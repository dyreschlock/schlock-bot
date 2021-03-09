package com.schlock.bot.services.commands.base.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.alert.Alert;
import com.schlock.bot.entities.apps.alert.AlertType;
import com.schlock.bot.services.commands.base.impl.AnimationServiceImpl;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.apps.AlertDAO;
import com.schlock.bot.services.database.apps.UserDAO;
import com.schlock.bot.services.database.apps.impl.AlertDAOImpl;
import com.schlock.bot.services.database.apps.impl.UserDAOImpl;
import com.schlock.bot.services.entities.base.UserManagement;
import com.schlock.bot.services.entities.base.impl.UserManagementImpl;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnimationServiceImplTest extends DatabaseTest
{
    private static final String USERNAME1 = "username1";

    private AlertDAO alertDAO;

    private AnimationServiceImpl impl;

    private User user;

    @Test
    public void testCreateAnimation()
    {
        String animation = AlertType.MONSTER_FRISBEE.toString();
        String command = "!animation " + animation;

        String response = impl.process(USERNAME1, command).getFirstMessage();
        String expected = messages().format(AnimationServiceImpl.ANIMATION_REGISTERED_KEY, USERNAME1, animation);

        assertEquals(expected, response);

        String badcommand = "!animation " + "asdf";

        response = impl.process(USERNAME1, badcommand).getFirstMessage();
        expected = messages().format(AnimationServiceImpl.ANIMATION_WRONG_FORMAT_KEY, USERNAME1);

        assertEquals(expected, response);
    }

    @Override
    protected void before() throws Exception
    {
        UserDAO userDAO = new UserDAOImpl(session)
        {
            public void commit()
            {
            }
        };

        UserManagement userManagement = new UserManagementImpl(userDAO, config());

        alertDAO = new AlertDAOImpl(session)
        {
            public void commit()
            {
            }
        };

        impl = new AnimationServiceImpl(userManagement, alertDAO, messages());

        createTestObjects();
    }

    private void createTestObjects()
    {
        user = new User();
        user.setUsername(USERNAME1);

        alertDAO.save(user);
    }

    @Override
    protected void after() throws Exception
    {
        removeTestObjects();
    }

    private void removeTestObjects()
    {
        List<Alert> alerts = alertDAO.getByUser(user);

        for(Alert alert : alerts)
        {
            alertDAO.delete(alert);
        }
        alertDAO.delete(user);
    }
}