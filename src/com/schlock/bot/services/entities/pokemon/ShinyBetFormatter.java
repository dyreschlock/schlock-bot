package com.schlock.bot.services.entities.pokemon;

import com.schlock.bot.entities.pokemon.ShinyBetLetsGo;
import com.schlock.bot.services.commands.ListenerResponse;

import java.util.List;

public interface ShinyBetFormatter
{
    ListenerResponse formatAllBets(ListenerResponse responses, List<ShinyBetLetsGo> bets);
}
