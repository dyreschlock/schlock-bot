package com.schlock.bot.services.commands.pokemon.shiny;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.entities.pokemon.ShinyDexLegendsEntry;
import com.schlock.bot.services.commands.ListenerService;

import java.util.List;

public interface ShinyDexService extends ListenerService
{
    List<Pokemon> getShinyDexEntries();

    List<ShinyDexLegendsEntry> getShinyLegendsEntries();
}
