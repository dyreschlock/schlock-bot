package com.schlock.bot.services.commands.base.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.entities.base.alert.AlertType;
import com.schlock.bot.entities.base.alert.AnimationAlert;
import com.schlock.bot.services.commands.AbstractListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.base.AnimationService;
import com.schlock.bot.services.database.base.AlertDAO;
import com.schlock.bot.services.entities.base.UserManagement;
import org.apache.tapestry5.ioc.Messages;

public class AnimationServiceImpl extends AbstractListenerService implements AnimationService
{
    protected static final String ANIMATION_REGISTERED_KEY = "animation-registered";
    protected static final String ANIMATION_WRONG_FORMAT_KEY = "animation-wrong-format";

    private final String ANIMATION_COMMAND = "!animation ";

    private final UserManagement userManagement;
    private final AlertDAO alertDAO;

    public AnimationServiceImpl(UserManagement userManagement,
                                AlertDAO alertDAO,
                                Messages messages)
    {
        super(messages);

        this.userManagement = userManagement;
        this.alertDAO = alertDAO;
    }

    public boolean isAcceptRequest(String username, String message)
    {
        return message != null &&
                message.toLowerCase().startsWith(ANIMATION_COMMAND);
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    protected ListenerResponse processRequest(String username, String in)
    {
        String commandText = in.toLowerCase().trim();
        if (commandText.startsWith(ANIMATION_COMMAND))
        {
            AlertType type = getTypeFromCommandText(in);
            if(type != null)
            {
                User user = userManagement.getUser(username);

                AnimationAlert alert = AnimationAlert.create(type, user);

                alertDAO.save(alert);
                alertDAO.commit();

                return formatSingleResponse(ANIMATION_REGISTERED_KEY, username, type.name());
            }
            return formatSingleResponse(ANIMATION_WRONG_FORMAT_KEY, username);
        }
        return nullResponse();
    }

    private AlertType getTypeFromCommandText(String commandText)
    {
        String type = commandText.substring(ANIMATION_COMMAND.length());

        try
        {
            return AlertType.valueOf(type.toUpperCase());
        }
        catch(Exception e)
        {
        }
        return null;
    }
}
