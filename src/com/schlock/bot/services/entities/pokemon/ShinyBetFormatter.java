package com.schlock.bot.services.entities.pokemon;

import com.schlock.bot.entities.apps.bet.ShinyBet;
import com.schlock.bot.services.commands.ListenerResponse;

import java.util.List;

public interface ShinyBetFormatter
{
    ListenerResponse formatAllBets(ListenerResponse responses, List<ShinyBet> bets);
}
