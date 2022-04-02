package com.schlock.bot.services.entities.pokemon;

import com.schlock.bot.entities.pokemon.ShinyGetLetsGo;
import com.schlock.bot.entities.pokemon.ShinyGetHisui;

public interface ShinyGetFormatter
{
    String formatNewlyCaughtLetsGo(ShinyGetLetsGo get);

    String formatMostRecent(ShinyGetLetsGo get);

    String formatNewlyCaughtHisui(ShinyGetHisui get);
}
