package com.schlock.bot.components.pokemon;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.entities.pokemon.ShinyDexEntryHisui;
import com.schlock.bot.entities.pokemon.ShinyHisuiGet;
import com.schlock.bot.services.commands.pokemon.shiny.ShinyDexService;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HisuiShinyDex
{
    private static final String DEX_FORMAT_FLAG = "dex";
    private static final String DEX_ORDER_FLAG = "order";
    private static final String DEX_MISSING_FLAG = "missing";

    private static final String DEX_FORMAT_DEFAULT = DEX_ORDER_FLAG;
    private static final Integer COLUMNS = 21;

    @Inject
    private ShinyDexService dexEntryService;

    @Inject
    private PokemonManagement pokemonManagement;


    @Inject
    private Messages messages;

    @Persist
    private String dexFormat;


    public String getDexFormat()
    {
        if (dexFormat == null)
        {
            dexFormat = DEX_FORMAT_DEFAULT;
        }
        return dexFormat;
    }

    public void setDexFormat(String dexFormat)
    {
        this.dexFormat = dexFormat;
    }

    public boolean isListInDexFormat()
    {
        return DEX_FORMAT_FLAG.equalsIgnoreCase(getDexFormat());
    }

    public boolean isListInDexOrder()
    {
        return DEX_ORDER_FLAG.equalsIgnoreCase(getDexFormat());
    }

    public boolean isListInMissingOrder()
    {
        return DEX_MISSING_FLAG.equalsIgnoreCase(getDexFormat());
    }

    public String getShinyDexMessage()
    {
        String message = "";
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

            message = messages.format("shiny-dex", dexCount.toString());
        }
        else if (isListInMissingOrder())
        {
            Integer missingCount = 0;

            List<ShinyDexEntryHisui> entries = dexEntryService.getShinyDexHisuiEntries();
            for(ShinyDexEntryHisui entry : entries)
            {
                if (!entry.isHaveShiny())
                {
                    missingCount++;
                }
            }

            message = messages.format("shiny-missing", missingCount.toString());
        }
        else if (isListInDexOrder())
        {
            List<ShinyHisuiGet> gets = dexEntryService.getShinyDexHisuiGets();
            Integer dexCount = gets.size();

            message = messages.format("shiny-count", dexCount.toString());
        }
        return message;
    }

    public String getRemainingCounts()
    {
        return dexEntryService.getHisuiRemainingCountsMessage();
    }

    public String getTableHTML()
    {
        if (isListInDexFormat())
        {
            return getTableHTMLDexFormat();
        }
        else if (isListInDexOrder())
        {
            return getTableHTMLDexOrder();
        }
        return getTableHTMLMissingOrder();
    }

    public String getTableHTMLDexOrder()
    {
        List<ShinyHisuiGet> gets = dexEntryService.getShinyDexHisuiGets();
        Collections.sort(gets, new Comparator<ShinyHisuiGet>()
        {
            @Override
            public int compare(ShinyHisuiGet o1, ShinyHisuiGet o2)
            {
                return o1.getHisuiNumber() - o2.getHisuiNumber();
            }
        });

        String html = "<table class=\"dex\">";

        html += "<tr>";
        for(Integer i = 1; i < gets.size()+1; i++)
        {
            ShinyHisuiGet get = gets.get(i-1);

            String imageName = get.getHisuiNumberString() + ".png";

            html += "<td><div class=\"img\">";
            html += "<img class=\"pshow\" src=\"/img/hisui/" + imageName + "\"/>";

            if (get.isAlpha())
            {
                html += "<img class=\"alpha\" src=\"/img/alpha_mark.png\" />";
            }

            html += "</div></td>";

            if (i % COLUMNS == 0)
            {
                html += "</tr><tr>";
            }
        }

        int remainingTD = COLUMNS - (gets.size() % COLUMNS);
        for(int i = 0; i < remainingTD; i++)
        {
            html += "<td></td>";
        }

        html += "</tr>";
        return html + "</table>";
    }

    public String getTableHTMLDexFormat()
    {
        String html = "<table class=\"dex\">";

        html += "<tr>";

        List<ShinyDexEntryHisui> entries = dexEntryService.getShinyDexHisuiEntries();
        for(ShinyDexEntryHisui entry : entries)
        {
            String numberString = entry.getNumberString();
            String imgClass = "";

            if (entry.isHaveShiny())
            {
                imgClass = "pshow";
            }
//            else if (entry.isInSomeOutbreak())
//            {
//                imgClass = "have";
//            }

            html += "<td><div class=\"img\">";
            html += "<img class=\""+imgClass +"\" src=\"/img/hisui/" + numberString + ".png\"/>";

            if (entry.isHaveAlpha())
            {
                html += "<img class=\"alpha\" src=\"/img/alpha_mark.png\" />";
            }

            html += "</div></td>";

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

    public String getTableHTMLMissingOrder()
    {
        String html = "<table class=\"dex\">";
        html += "<tr>";

        int index = 0;

        List<ShinyDexEntryHisui> entries = dexEntryService.getShinyDexHisuiEntries();
        for (ShinyDexEntryHisui entry : entries)
        {
            if (!entry.isHaveShiny())
            {
                String numberString = entry.getNumberString();

                html += "<td><img class=\"pshow\" src=\"/img/hisui/" + numberString + ".png\" /></td>";

                index++;

                if (index % COLUMNS == 0)
                {
                    html += "</tr><tr>";
                }
            }
        }

        int remainingTD = COLUMNS - (index % COLUMNS);
        for(int i = 0; i < remainingTD; i++)
        {
            html += "<td></td>";
        }

        html += "</tr></table>";
        return html;
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
