package com.schlock.bot.pages.pokemon;

import com.schlock.bot.entities.pokemon.ShinyDexLegendsEntry;
import com.schlock.bot.services.commands.pokemon.shiny.ShinyDexService;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

public class ShinyDexLegends
{
    private static final Integer COLUMNS = 20;

    @Inject
    private ShinyDexService dexEntryService;

    @Inject
    private Messages messages;

    public String getShinyDexMessage()
    {
        List<ShinyDexLegendsEntry> entries = dexEntryService.getShinyLegendsEntries();
        Integer dexCount = entries.size();

        String message = messages.format("shiny-dex", dexCount.toString());
        return message;
    }

    public String getTableHTML()
    {
        String html = "<table class=\"dex\"";

        List<ShinyDexLegendsEntry> entries = dexEntryService.getShinyLegendsEntries();

        html += "<tr>";
        for(Integer i = 1; i < entries.size()+1; i++)
        {
            String imageName = getImageFilename(i);

            html += "<td><img src=\"../img/legends/" + imageName + "\"/></td>";
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

    private String getImageFilename(int index)
    {
        String indexString = Integer.toString(index);
        while (indexString.length() < 3)
        {
            indexString = "0" + indexString;
        }

        return indexString + ".png";
    }

}
