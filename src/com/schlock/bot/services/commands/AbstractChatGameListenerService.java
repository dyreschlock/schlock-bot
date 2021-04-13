package com.schlock.bot.services.commands;

import org.apache.tapestry5.ioc.Messages;

public abstract class AbstractChatGameListenerService extends AbstractListenerService implements ChatGameListenerService
{
    private boolean on;

    protected AbstractChatGameListenerService(Messages messages)
    {
        super(messages);

        this.on = false;
    }

    @Override
    public boolean isOn()
    {
        return on;
    }

    @Override
    public boolean turnOn()
    {
        this.on = true;
        return true;
    }

    @Override
    public boolean turnOff()
    {
        if (isStarted())
        {
            return false;
        }

        this.on = false;
        return true;
    }
}
