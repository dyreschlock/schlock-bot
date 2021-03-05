package com.schlock.bot.services.bot.apps.alert.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.alert.AlertType;
import com.schlock.bot.entities.apps.alert.AnimationAlert;
import com.schlock.bot.services.bot.apps.AbstractListenerService;
import com.schlock.bot.services.bot.apps.ListenerResponse;
import com.schlock.bot.services.bot.apps.UserService;
import com.schlock.bot.services.bot.apps.alert.AnimationService;
import com.schlock.bot.services.database.apps.AlertDAO;
import org.apache.tapestry5.ioc.Messages;

public class AnimationServiceImpl extends AbstractListenerService implements AnimationService
{
    protected static final String ANIMATION_REGISTERED_KEY = "animation-registered";
    protected static final String ANIMATION_WRONG_FORMAT_KEY = "animation-wrong-format";

    private final String ANIMATION_COMMAND = "!animation ";

    private final UserService userService;
    private final AlertDAO alertDAO;

    public AnimationServiceImpl(UserService userService,
                                AlertDAO alertDAO,
                                Messages messages)
    {
        super(messages);

        this.userService = userService;
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

    public ListenerResponse process(String username, String in)
    {
        String commandText = in.toLowerCase().trim();
        if (commandText.startsWith(ANIMATION_COMMAND))
        {
            AlertType type = getTypeFromCommandText(in);
            if(type != null)
            {
                User user = userService.getUser(username);

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
