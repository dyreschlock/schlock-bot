package com.schlock.bot;

import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.bot.ListenerService;

import java.util.Set;

public abstract class AbstractBot implements Bot
{
    private final Set<ListenerService> listeners;

    private final DeploymentContext context;


    public AbstractBot(Set<ListenerService> listeners,
                       DeploymentContext context)
    {
        this.listeners = listeners;
        this.context = context;
    }

    protected Set<ListenerService> getListeners()
    {
        return listeners;
    }

    protected DeploymentContext getContext()
    {
        return context;
    }
}
