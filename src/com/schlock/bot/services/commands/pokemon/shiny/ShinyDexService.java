package com.schlock.bot.services.commands.pokemon.shiny;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.entities.pokemon.ShinyDexEntryGo;
import com.schlock.bot.services.commands.ListenerService;

import java.util.List;

public interface ShinyDexService extends ListenerService
{
    List<Pokemon> getShinyDexEntries();

    List<Pokemon> getShinyDexHisuiEntries();

    List<Pokemon> getShinyDexHisuiGets();

    List<ShinyDexEntryGo> getPokemonGoEntries();
}
