package com.schlock.bot.services.bot.apps;

import org.apache.tapestry5.ioc.Messages;

public abstract class AbstractListenerService implements ListenerService
{
    protected final Messages messages;

    protected AbstractListenerService(Messages messages)
    {
        this.messages = messages;
    }

    protected ListenerResponse singleResponseByKey(String messageKey)
    {
        String response = messages.get(messageKey);
        return ListenerResponse.respondOnce().addMessage(response);
    }

    protected ListenerResponse singleResponseFormat(String messageKey, Object... args)
    {
        String response = messages.format(messageKey, args);
        return ListenerResponse.respondOnce().addMessage(response);
    }

    protected ListenerResponse nullResponse()
    {
        String response = messages.get(NULL_RESPONSE_KEY);
        return ListenerResponse.respondOnce().addMessage(response);
    }
}
