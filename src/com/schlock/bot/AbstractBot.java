package com.schlock.bot;

import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.bot.apps.ListenerService;

import java.util.Set;

public abstract class AbstractBot implements Bot
{
    private final Set<ListenerService> listeners;

    private final DeploymentConfiguration config;


    public AbstractBot(Set<ListenerService> listeners,
                       DeploymentConfiguration config)
    {
        this.listeners = listeners;
        this.config = config;
    }

    protected Set<ListenerService> getListeners()
    {
        return listeners;
    }

    protected DeploymentConfiguration getConfig()
    {
        return config;
    }
}
