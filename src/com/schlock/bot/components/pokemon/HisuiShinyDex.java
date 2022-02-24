package com.schlock.bot.components.pokemon;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.entities.pokemon.ShinyHisuiGet;
import com.schlock.bot.services.commands.pokemon.shiny.ShinyDexService;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

public class HisuiShinyDex
{
    private static final Integer COLUMNS = 20;

    @Inject
    private ShinyDexService dexEntryService;

    @Inject
    private PokemonManagement pokemonManagement;


    @Inject
    private Messages messages;

    public String getShinyDexMessage()
    {
        List<ShinyHisuiGet> entries = dexEntryService.getShinyLegendsEntries();
        Integer dexCount = entries.size();

        String message = messages.format("shiny-dex", dexCount.toString());
        return message;
    }

    public String getTableHTML()
    {
        String html = "<table class=\"dex\"";

        List<ShinyHisuiGet> entries = dexEntryService.getShinyLegendsEntries();

        html += "<tr>";
        for(Integer i = 1; i < entries.size()+1; i++)
        {
            ShinyHisuiGet entry = entries.get(i-1);

            Pokemon pokemon = pokemonManagement.getPokemonFromText(entry.getPokemonId());
            String imageName = pokemon.getHisuiNumberString() + ".png";

            html += "<td><img class=\"pshow\" src=\"/img/hisui/" + imageName + "\"/></td>";
            if (i % COLUMNS == 0)
            {
                html += "</tr><tr>";
            }
        }

        int remainingTD = COLUMNS - (entries.size() % COLUMNS);
        for(int i = 0; i < remainingTD; i++)
        {
            html += "<td></td>";
        }

        html += "</tr>";
        return html + "</table>";
    }
}
