package com.schlock.bot.services.bot.apps;

import java.util.ArrayList;
import java.util.List;

public class ListenerResponse
{
    private boolean relayToDiscord;

    private List<String> messages = new ArrayList<>();

    private ListenerResponse(boolean relayToDiscord)
    {
        this.relayToDiscord = relayToDiscord;
    }

    public ListenerResponse addMessage(String message)
    {
        messages.add(message);

        return this;
    }

    public boolean isRelayToDiscord()
    {
        return relayToDiscord;
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

    public static ListenerResponse respondRelayToDiscord()
    {
        return new ListenerResponse(true);
    }

    public static ListenerResponse respondOnce()
    {
        return new ListenerResponse(false);
    }

    public static ListenerResponse empty()
    {
        return new ListenerResponse(false);
    }
}
