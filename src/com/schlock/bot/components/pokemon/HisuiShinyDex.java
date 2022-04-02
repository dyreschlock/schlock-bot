package com.schlock.bot.components.pokemon;

import com.schlock.bot.entities.pokemon.ShinyDexEntryHisui;
import com.schlock.bot.entities.pokemon.ShinyGetHisui;
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

    private static final String DEX_FORMAT_DEFAULT = DEX_FORMAT_FLAG;
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
            List<ShinyGetHisui> gets = dexEntryService.getShinyDexHisuiGets();
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
        List<ShinyGetHisui> gets = dexEntryService.getShinyDexHisuiGets();
        Collections.sort(gets, new Comparator<ShinyGetHisui>()
        {
            @Override
            public int compare(ShinyGetHisui o1, ShinyGetHisui o2)
            {
                return o1.getHisuiNumber() - o2.getHisuiNumber();
            }
        });

        String html = "<table class=\"dex\">";

        html += "<tr>";
        for(Integer i = 1; i < gets.size()+1; i++)
        {
            ShinyGetHisui get = gets.get(i-1);

            String numberString = get.getHisuiNumberString();
            boolean show = true;
            boolean alpha = get.isAlpha();
            boolean rare = false;

            html += "<td>";
            html += createImageHTML(numberString, show, alpha, rare);
            html += "</td>";

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
            boolean show = entry.isHaveShiny();
            boolean alpha = entry.isHaveAlpha();
            boolean rare = entry.isRare();

            html += "<td>";
            html += createImageHTML(numberString, show, alpha, rare);
            html += "</td>";

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

                boolean show = true;
                boolean rare = entry.isRare();
                boolean alpha = false;

                html += "<td>";
                html += createImageHTML(numberString, show, alpha, rare);
                html += "</td>";

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

    private String createImageHTML(String imageNumber, boolean show, boolean alpha, boolean rare)
    {
        String html = "<div class=\"mark_img\" >";

        String imgClass = "";
        if(show)
        {
            imgClass = "pshow";
        }

        html += "<img class=\"" + imgClass + "\" src=\"/img/hisui/" + imageNumber + ".png\" />";

        if(show)
        {
            if (alpha & rare)
            {
                html += "<div class=\"multiple\">" +
                        "<img src=\"/img/alpha_mark.png\" />" +
                        "<img src=\"/img/shiny_mark.png\" />" +
                        "</div>";
            }
            else if (alpha)
            {
                html += "<div class=\"single\"><img src=\"/img/alpha_mark.png\" /></div>";
            }
            else if (rare)
            {
                html += "<div class=\"single\"><img src=\"/img/shiny_mark.png\" /></div>";
            }
        }

        html += "</div>";

        return html;
    }
 }
