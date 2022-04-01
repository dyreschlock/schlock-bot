package com.schlock.bot.services.commands.pokemon.shiny;

import com.schlock.bot.entities.pokemon.*;
import com.schlock.bot.services.commands.ListenerService;

import java.util.List;

public interface ShinyDexService extends ListenerService
{
    int getOverallShinyCount();

    boolean isHaveShiny(String pokemonNumberCode);

    List<ShinyDexEntryLetsGo> getShinyDexLetsGoEntries();

    List<ShinyDexEntryHisui> getShinyDexHisuiEntries();

    List<ShinyHisuiGet> getShinyDexHisuiGets();

    List<ShinyDexEntryGo> getPokemonGoEntries();

    String getHisuiRemainingCountsMessage();
}
