package com.schlock.bot.services.bot.apps.alert.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.alert.AlertType;
import com.schlock.bot.entities.apps.alert.AnimationAlert;
import com.schlock.bot.services.bot.apps.UserService;
import com.schlock.bot.services.bot.apps.alert.AnimationService;
import com.schlock.bot.services.database.apps.AlertDAO;
import org.apache.tapestry5.ioc.Messages;

import java.util.Arrays;
import java.util.List;

public class AnimationServiceImpl implements AnimationService
{
    protected static final String ANIMATION_REGISTERED_KEY = "animation-registered";
    protected static final String ANIMATION_WRONG_FORMAT_KEY = "animation-wrong-format";

    private final String ANIMATION_COMMAND = "!animation ";

    private final UserService userService;
    private final AlertDAO alertDAO;
    private final Messages messages;

    public AnimationServiceImpl(UserService userService,
                                AlertDAO alertDAO,
                                Messages messages)
    {
        this.userService = userService;
        this.alertDAO = alertDAO;
        this.messages = messages;
    }

    @Override
    public boolean isAcceptRequest(String username, String message)
    {
        return message != null &&
                message.toLowerCase().startsWith(ANIMATION_COMMAND);
    }

    @Override
    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    @Override
    public List<String> process(String username, String command)
    {
        return Arrays.asList(processSingleResponse(username, command));
    }

    protected String processSingleResponse(String username, String in)
    {
        String commandText = in.toLowerCase().trim();
        if (commandText.startsWith(ANIMATION_COMMAND))
        {
            AlertType type = getTypeFromCommandText(in);
            User user = userService.getUser(username);

            AnimationAlert alert = AnimationAlert.create(type, user);

            alertDAO.save(alert);
            alertDAO.commit();

            return messages.format(ANIMATION_REGISTERED_KEY, username, type.name());
        }
        return messages.get(NULL_RESPONSE_KEY);
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
