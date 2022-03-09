package com.schlock.bot.components.pokemon;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.entities.pokemon.ShinyDexEntryHisui;
import com.schlock.bot.services.commands.pokemon.shiny.ShinyDexService;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HisuiShinyDex
{
    private static final Boolean DEX_FORMAT_DEFAULT = false;
    private static final Integer COLUMNS = 20;

    @Inject
    private ShinyDexService dexEntryService;

    @Inject
    private PokemonManagement pokemonManagement;


    @Inject
    private Messages messages;

    @Property
    private Boolean dexOrder;


    public boolean isListInDexFormat()
    {
        if (dexOrder == null)
        {
            dexOrder = DEX_FORMAT_DEFAULT;
        }
        return dexOrder;
    }

    public String getShinyDexMessage()
    {
        if (isListInDexFormat())
        {
            Integer dexCount = 0;

            List<ShinyDexEntryHisui> entries = dexEntryService.getShinyDexHisuiEntries();
            for(ShinyDexEntryHisui entry : entries)
            {
                if (entry.isHaveShiny())
                {
                    dexCount++;
                }
            }

            String message = messages.format("shiny-dex", dexCount.toString());
            return message;
        }

        List<Pokemon> pokemon = dexEntryService.getShinyDexHisuiGets();
        Integer dexCount = pokemon.size();

        String message = messages.format("shiny-count", dexCount.toString());
        return message;
    }

    public String getTableHTML()
    {
        if (isListInDexFormat())
        {
            return getTableHTMLDexFormat();
        }
        return getTableHTMLDexOrder();
    }

    public String getTableHTMLDexOrder()
    {
        List<Pokemon> pokemon = dexEntryService.getShinyDexHisuiGets();
        Collections.sort(pokemon, new Comparator<Pokemon>()
        {
            @Override
            public int compare(Pokemon o1, Pokemon o2)
            {
                return o1.getHisuiNumber() - o2.getHisuiNumber();
            }
        });
        return getTableHTMLInListOrder(pokemon);
    }

    private String getTableHTMLInListOrder(List<Pokemon> pokemon)
    {
        String html = "<table class=\"dex\"";

        html += "<tr>";
        for(Integer i = 1; i < pokemon.size()+1; i++)
        {
            Pokemon p = pokemon.get(i-1);
            String imageName = p.getHisuiNumberString() + ".png";

            html += "<td><img class=\"pshow\" src=\"/img/hisui/" + imageName + "\"/></td>";
            if (i % COLUMNS == 0)
            {
                html += "</tr><tr>";
            }
        }

        int remainingTD = COLUMNS - (pokemon.size() % COLUMNS);
        for(int i = 0; i < remainingTD; i++)
        {
            html += "<td></td>";
        }

        html += "</tr>";
        return html + "</table>";
    }

    public String getTableHTMLDexFormat()
    {
        String html = "<table class=\"dex\"";

        List<ShinyDexEntryHisui> entries = dexEntryService.getShinyDexHisuiEntries();

        final Integer MAX = 225;

        html += "<tr>";
        for(ShinyDexEntryHisui entry : entries)
        {
            String numberString = entry.getNumberString();
            String imgClass = "";

            if (entry.isHaveShiny())
            {
                imgClass = "show";
            }

            html += "<td><img class=\"p"+imgClass +"\" src=\"/img/hisui/" + numberString + ".png\"/></td>";
            if (entry.getPokemonNumber() % COLUMNS == 0)
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

    public String getNumberString(Integer i)
    {
        String number = i.toString();
        while (number.length() < 3)
        {
            number = "0" + number;
        }
        return number;
    }

    public boolean containsPokemon(Integer number, List<Pokemon> pokemon)
    {
        for(Pokemon p : pokemon)
        {
            if (p.getHisuiNumber().equals(number))
            {
                return true;
            }
        }
        return false;
    }
}
