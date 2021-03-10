package com.schlock.bot.pages.pokemon;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.services.commands.pokemon.shiny.ShinyDexService;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

public class ShinyDex
{
    private static final Integer COLUMNS = 22;

    @Inject
    private ShinyDexService dexEntryService;

    @Inject
    private Messages messages;

    public String getShinyDexMessage()
    {
        List<Pokemon> entries = dexEntryService.getShinyDexEntries();
        Integer dexCount = entries.size();

        String message = messages.format("shiny-dex", dexCount.toString());
        return message;
    }

    public String getTableHTML()
    {
        String html = "<table class=\"dex\">";

        List<Pokemon> entries = dexEntryService.getShinyDexEntries();

        final Integer MAX = 151;

        html += "<tr>";
        for(Integer i = 1; i < MAX+1; i++)
        {
            String number = i.toString();
            if (number.length() == 1)
            {
                number = "00" + number;
            }
            if (number.length() == 2)
            {
                number = "0" + number;
            }

            String imgClass = "";

            boolean showPokemon = containsPokemon(i, entries);
            if (showPokemon)
            {
                imgClass = "show";
            }

            html += "<td><img class=\"p" + imgClass + "\" src=\"../img/pokemon/" + number + ".png\"/></td>";
            if (i % COLUMNS == 0)
            {
                html += "</tr><tr>";
            }
        }

        int remainingTD = COLUMNS - (MAX % COLUMNS);
        for(int i = 0; i < remainingTD; i++)
        {
            html += "<td></td>";
        }

        html += "</tr>";
        return html + "</table>";
    }

    private boolean containsPokemon(Integer number, List<Pokemon> pokemon)
    {
        for (Pokemon p : pokemon)
        {
            if (p.getNumber().equals(number))
            {
                return true;
            }
        }
        return false;
    }

    /*


                .p001, .p004, .p007, .p010, .p011, .p012, .p013, .p014, .p015, .p016, .p017, .p018, .p019,
            .p021, .p022, .p025, .p026, .p029, .p030, .p031, .p032, .p033, .p034, .p035, .p036,
            .p042, .p043, .p044, .p045,
            .p066, .p069, .p070, .p071, .p072, .p073, .p074, .p075,
            .p081, .p083, .p084, .p085, .p088, .p089, .p092, .p093, .p094, .p098, .p099, .p100,
            .p101, .p106, .p108, .p109, .p110, .p111, .p113, .p120,
            .p126, .p129, .p130, .p131, .p132, .p133, .p134, .p135, .p136, .p137,
            .p143, .p147, .p149,

     */
}
