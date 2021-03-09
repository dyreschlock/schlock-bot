package com.schlock.bot.services.bot;

import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.ListenerService;

import java.util.Set;

public abstract class AbstractBot implements Bot
{
    private final Set<ListenerService> listeners;

    private final DeploymentConfiguration config;

    private boolean isStarted = false;

    public AbstractBot(Set<ListenerService> listeners,
                       DeploymentConfiguration config)
    {
        this.listeners = listeners;
        this.config = config;
    }

    protected abstract void startService() throws Exception;

    public void startup() throws Exception
    {
        if (!isStarted)
        {
            startService();
            isStarted = true;
        }
    }

    public boolean isStarted()
    {
        return isStarted;
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
