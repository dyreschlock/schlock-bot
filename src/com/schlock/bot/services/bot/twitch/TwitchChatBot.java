package com.schlock.bot.services.bot.twitch;

import com.schlock.bot.services.bot.Bot;

public interface TwitchChatBot extends Bot
{
    void relayMessage(String message);
}
