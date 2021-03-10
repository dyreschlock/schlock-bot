package com.schlock.bot.services.entities.pokemon;

import com.schlock.bot.entities.pokemon.ShinyGet;

public interface ShinyGetFormatter
{
    String formatNewlyCaught(ShinyGet get);

    String formatMostRecent(ShinyGet get);
}
