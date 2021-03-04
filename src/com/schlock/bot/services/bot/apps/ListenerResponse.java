package com.schlock.bot.services.bot.apps;

import java.util.ArrayList;
import java.util.List;

public class ListenerResponse
{
    private boolean relayAll;

    private List<String> messages = new ArrayList<>();

    private ListenerResponse(boolean relayAll)
    {
        this.relayAll = relayAll;
    }

    public ListenerResponse addMessage(String message)
    {
        messages.add(message);

        return this;
    }

    public boolean isRelayAll()
    {
        return relayAll;
    }

    public List<String> getMessages()
    {
        return messages;
    }

    public String getFirstMessage()
    {
        if (messages.isEmpty())
        {
            return null;
        }
        return messages.get(0);
    }

    public static ListenerResponse relayAll()
    {
        return new ListenerResponse(true);
    }

    public static ListenerResponse relaySingle()
    {
        return new ListenerResponse(false);
    }

    public static ListenerResponse relayNothing()
    {
        return new ListenerResponse(false);
    }
}
