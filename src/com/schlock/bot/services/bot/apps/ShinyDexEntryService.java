package com.schlock.bot.services.bot.apps;

import com.schlock.bot.entities.apps.pokemon.Pokemon;

import java.util.List;

public interface ShinyDexEntryService
{
    List<Pokemon> getShinyDexEntries();
}
