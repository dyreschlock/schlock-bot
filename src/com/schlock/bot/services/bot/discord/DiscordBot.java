package com.schlock.bot.services.bot.discord;

import com.schlock.bot.services.bot.Bot;

import java.util.List;

public interface DiscordBot extends Bot
{
    void relayMessages(List<String> messages);

    void relayMessage(String message);
}
