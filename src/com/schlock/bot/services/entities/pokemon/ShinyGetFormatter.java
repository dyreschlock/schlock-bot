package com.schlock.bot.services.entities.pokemon;

import com.schlock.bot.entities.pokemon.ShinyGet;
import com.schlock.bot.entities.pokemon.ShinyHisuiGet;

public interface ShinyGetFormatter
{
    String formatNewlyCaught(ShinyGet get);

    String formatMostRecent(ShinyGet get);

    String formatNewlyCaughtHisui(ShinyHisuiGet get);
}
