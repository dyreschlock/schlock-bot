package com.schlock.bot.services.commands;

import org.apache.tapestry5.ioc.Messages;

public abstract class AbstractListenerService implements ListenerService
{
    protected final Messages messages;

    protected AbstractListenerService(Messages messages)
    {
        this.messages = messages;
    }

    public final synchronized ListenerResponse process(String username, String command)
    {
        return processRequest(username, command);
    }

    protected abstract ListenerResponse processRequest(String username, String command);

    protected ListenerResponse formatSingleResponse(String messageKey, Object... args)
    {
        String response = messages.get(messageKey);
        if (args.length != 0)
        {
            response = String.format(response, args);
        }
        return ListenerResponse.relaySingle().addMessage(response);
    }

    protected ListenerResponse formatAllResponse(String messageKey, Object... args)
    {
        String response = messages.get(messageKey);
        if (args.length != 0)
        {
            response = String.format(response, args);
        }
        return ListenerResponse.relayAll().addMessage(response);
    }

    protected ListenerResponse nullResponse()
    {
        String response = messages.get(NULL_RESPONSE_KEY);
        return ListenerResponse.relaySingle().addMessage(response);
    }
}
