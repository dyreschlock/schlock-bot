package com.schlock.bot.services.bot.apps.bet;

import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.entities.apps.pokemon.ShinyGet;

public interface ShinyGetFormatter
{
    String formatNewlyCaught(ShinyGet get);

    String formatMostRecent(ShinyGet get);
}
