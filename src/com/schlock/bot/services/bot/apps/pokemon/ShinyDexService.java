package com.schlock.bot.services.bot.apps.pokemon;

import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.services.bot.apps.ListenerService;

import java.util.List;

public interface ShinyDexService extends ListenerService
{
    List<Pokemon> getShinyDexEntries();
}
