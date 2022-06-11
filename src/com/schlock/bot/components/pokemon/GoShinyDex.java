package com.schlock.bot.components.pokemon;

import com.schlock.bot.entities.pokemon.PokemonRegion;
import com.schlock.bot.entities.pokemon.ShinyDexEntryGo;
import com.schlock.bot.services.commands.pokemon.shiny.ShinyDexService;
import org.apache.commons.codec.binary.Base64;
import org.apache.tapestry5.ioc.Messages;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GoShinyDex
{
    private static final Integer COLUMNS = 30;

    @Inject
    private ShinyDexService shinyDexService;

    @Inject
    private Messages messages;

    public String getShinyDexMessage()
    {
        List<ShinyDexEntryGo> entries = shinyDexService.getPokemonGoEntries();

        int have = 0;
        int shiny = 0;
        int shinyAlola = 0;
        int shinyGalar = 0;

        for(ShinyDexEntryGo entry : entries)
        {
            if (PokemonRegion.isNormalNumberCode(entry.getNumberCode()))
            {
                if (entry.isHave())
                {
                    have++;
                }
                if (entry.isShinyAtAll())
                {
                    shiny++;
                }
            }
            else if (PokemonRegion.isRegionalNumberCode(entry.getNumberCode(), PokemonRegion.ALOLA))
            {
                if (entry.isShinyAtAll())
                {
                    shinyAlola++;
                }
            }
            else if (PokemonRegion.isRegionalNumberCode(entry.getNumberCode(), PokemonRegion.GALAR))
            {
                if (entry.isShinyAtAll())
                {
                    shinyGalar++;
                }
            }
        }

        String message = messages.format("shiny-dex", have, shiny, shinyAlola, shinyGalar);
        return message;
    }

    public String getTableHTML()
    {
        String html = "<table class=\"dex\">";

        List<ShinyDexEntryGo> entries = shinyDexService.getPokemonGoEntries();

        Predicate<ShinyDexEntryGo> byNormal = entry -> PokemonRegion.isNormalNumberCode(entry.getNumberCode());
        html += createRowCellHTMLForEntries(entries.stream().filter(byNormal).collect(Collectors.toList()));

        Predicate<ShinyDexEntryGo> byAlola = entry -> PokemonRegion.isRegionalNumberCode(entry.getNumberCode(), PokemonRegion.ALOLA);
        html += createRowCellHTMLForEntries(entries.stream().filter(byAlola).collect(Collectors.toList()));

        Predicate<ShinyDexEntryGo> byGalar = entry -> PokemonRegion.isRegionalNumberCode(entry.getNumberCode(), PokemonRegion.GALAR);
        html += createRowCellHTMLForEntries(entries.stream().filter(byGalar).collect(Collectors.toList()));

        Predicate<ShinyDexEntryGo> byHisui = entry -> PokemonRegion.isRegionalNumberCode(entry.getNumberCode(), PokemonRegion.HISUI);
        html += createRowCellHTMLForEntries(entries.stream().filter(byHisui).collect(Collectors.toList()));

        return html + "</table>";
    }

    private String createRowCellHTMLForEntries(List<ShinyDexEntryGo> entries)
    {
        String html = "<tr>";

        int cellCount = 0;
        for(int i = 0; i < entries.size();i++)
        {
            ShinyDexEntryGo entry = entries.get(i);

            html += createCellHTMLForEntry(entry);

            if ((i + 1) % COLUMNS == 0)
            {
                html += "</tr><tr>";
            }
            cellCount++;
        }

        while ((cellCount) % COLUMNS != 0)
        {
            html += "<td class=\"mini\"></td>";
            cellCount++;
        }

        html += "</tr>";

        return html;
    }

    private String createCellHTMLForEntry(ShinyDexEntryGo entry)
    {
        String imgClass = "";
        if (entry.isHave())
        {
            imgClass += " have";
        }
        if (entry.isShinyGoFirst() || entry.isShinyGoSecond())
        {
            imgClass += " shiny_go";
        }
        if (entry.isShinyHomeOwn() || entry.isShinyHomeOther())
        {
            imgClass += " shiny_home";
        }

        String imgSrc = entry.getGoogleImageLink();
        if (imgSrc.isBlank())
        {
            String number = entry.getNumberCode();
//                String filepath = "/Users/jimhendricks/GoogleDrive/Blog/stream/pokemon/" + number + ".png";
//                imgSrc = getDataUrl(filepath);
            imgSrc = getImageUrl(number);
        }

        String html = "<td class=\"mini\"><img class=\"" + imgClass + "\" src=\"" + imgSrc + "\" /></td>";
        return html;
    }

    private String getImageUrl(String number)
    {
        return "/img/pokemon/" + number + ".png";
    }

    private String getDataUrl(String filepath)
    {
        String url = "data:image/png;base64,";

        try
        {
            Path image = Path.of(filepath);
            byte[] body = new byte[0];
            body = Files.readAllBytes(image);
            String base64 = Base64.encodeBase64String(body);

            return url + base64;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return "";
    }
}
