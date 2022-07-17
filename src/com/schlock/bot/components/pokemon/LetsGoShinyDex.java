package com.schlock.bot.components.pokemon;

import com.schlock.bot.entities.pokemon.ShinyDexEntryLetsGo;
import com.schlock.bot.services.commands.pokemon.shiny.ShinyDexService;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.Arrays;
import java.util.List;

public class LetsGoShinyDex
{
    private static final Integer COLUMNS = 22;

    @Inject
    private PokemonManagement pokemonManagement;

    @Inject
    private ShinyDexService dexEntryService;

    @Inject
    private Messages messages;


    public String getShinyDexMessage()
    {
        List<ShinyDexEntryLetsGo> entries = dexEntryService.getShinyDexLetsGoEntries();
        Integer dexCount = entries.size();

        String message = messages.format("shiny-dex", dexCount.toString());
        return message;
    }

    public String getTableHTML()
    {
        String html = "<table class=\"dex\">";

        List<ShinyDexEntryLetsGo> entries = dexEntryService.getShinyDexLetsGoEntries();

        final Integer MAX = 151;

        html += "<tr>";
        for (Integer i = 1; i < MAX + 1; i++)
        {
            String numberCode = i.toString();
            if (numberCode.length() == 1)
            {
                numberCode = "00" + numberCode;
            }
            if (numberCode.length() == 2)
            {
                numberCode = "0" + numberCode;
            }

            String imgClass = "";

            boolean showPokemon = containsPokemon(numberCode, entries);
            if (showPokemon)
            {
                imgClass = "pshow";
            }
            else
            {
                imgClass = "have";
            }

            html += "<td><img class=\"" + imgClass + "\" src=\"/img/pokemon/" + numberCode + ".png\"/></td>";
            if (i % COLUMNS == 0)
            {
                html += "</tr><tr>";
            }
        }

        int remainingTD = COLUMNS - (MAX % COLUMNS);
        for (int i = 0; i < remainingTD; i++)
        {
            html += "<td></td>";
        }

        html += "</tr><tr>";

        List<Integer> alolan = pokemonManagement.getAlolanPokemonNumbers();

        remainingTD = COLUMNS - (alolan.size() % COLUMNS);
        for (int i = 0; i < remainingTD; i++)
        {
            html += "<td></td>";
        }

        for(int i = 0; i < alolan.size(); i++)
        {
            Integer alola = alolan.get(i);

            String numberCode = alola.toString();
            if (numberCode.length() == 2)
            {
                numberCode = "0" + numberCode;
            }
            numberCode = "a_" + numberCode;

            String imgClass = "have";
            boolean showPokemon = containsPokemon(numberCode, entries);
            if(showPokemon)
            {
                imgClass = "pshow";
            }

            html += "<td><img class=\"" + imgClass + "\" src=\"/img/pokemon/" + numberCode + ".png\"/></td>";
            if (i+1 % COLUMNS == 0)
            {
                html += "</tr><tr>";
            }
        }

        html += "</tr>";
        return html + "</table>";
    }

    private boolean containsPokemon(String numberCode, List<ShinyDexEntryLetsGo> entries)
    {
        for (ShinyDexEntryLetsGo entry : entries)
        {
            if(numberCode.equalsIgnoreCase(entry.getNumberCode()))
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
