package com.schlock.bot.services.commands;

import java.util.ArrayList;
import java.util.List;

public class ListenerResponse
{
    private boolean relayAll;

    private List<ListeningResponseMessage> messages = new ArrayList<>();

    private ListenerResponse(boolean relayAll)
    {
        this.relayAll = relayAll;
    }

    public ListenerResponse addMessage(String message)
    {
        return addMessage(message, false);
    }

    public ListenerResponse addMessage(String message, boolean bold)
    {
        ListeningResponseMessage m = new ListeningResponseMessage();
        m.message = message;
        m.bold = bold;

        messages.add(m);

        return this;
    }

    public boolean isRelayAll()
    {
        return relayAll;
    }

    public List<String> getMessages()
    {
        List<String> strings = new ArrayList<>();
        for (ListeningResponseMessage mess : messages)
        {
            strings.add(mess.message);
        }

        return strings;
    }

    public String getFirstMessage()
    {
        if (messages.isEmpty())
        {
            return null;
        }
        return messages.get(0).message;
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

    public String formatForDiscord(String message)
    {
        for (ListeningResponseMessage mess : messages)
        {
            if (mess.message.equals(message) && mess.bold)
            {
                return "**" + message + "**";
            }
        }
        return message;
    }


    private class ListeningResponseMessage
    {
        public String message;
        public boolean bold;
    }
}
