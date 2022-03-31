package com.schlock.bot.services.commands.pokemon.shiny;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.entities.pokemon.ShinyDexEntryGo;
import com.schlock.bot.entities.pokemon.ShinyDexEntryHisui;
import com.schlock.bot.entities.pokemon.ShinyHisuiGet;
import com.schlock.bot.services.commands.ListenerService;

import java.util.List;

public interface ShinyDexService extends ListenerService
{
    int getOverallShinyCount();

    boolean isHaveShiny(String pokemonNumberCode);

    List<Pokemon> getShinyDexEntries();

    List<ShinyDexEntryHisui> getShinyDexHisuiEntries();

    List<ShinyHisuiGet> getShinyDexHisuiGets();

    List<ShinyDexEntryGo> getPokemonGoEntries();

    String getHisuiRemainingCountsMessage();
}
