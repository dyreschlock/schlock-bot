package com.schlock.bot.components.pokemon;

import com.schlock.bot.entities.pokemon.ShinyDexEntryGo;
import com.schlock.bot.services.commands.pokemon.shiny.ShinyDexService;
import org.apache.commons.codec.binary.Base64;
import org.apache.tapestry5.ioc.Messages;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class GoShinyDex
{
    private static final Integer COLUMNS = 30;

    @Inject
    private ShinyDexService shinyDexService;

    @Inject
    private Messages messages;

    public String getShinyDexMessage()
    {
        int have = 0;
        int shiny = 0;

        List<ShinyDexEntryGo> entries = shinyDexService.getPokemonGoEntries();

        for(ShinyDexEntryGo entry : entries)
        {
            if (entry.isHave())
            {
                have++;
            }
            if (entry.isShinyGo() || entry.isShinyHome())
            {
                shiny++;
            }
        }

        String message = messages.format("shiny-dex", have, shiny);
        return message;
    }

    public String getTableHTML()
    {
        String html = "<table class=\"dex\">";

        List<ShinyDexEntryGo> entries = shinyDexService.getPokemonGoEntries();

        html += "<tr>";

        for(int i = 0; i < entries.size();i++)
        {
            ShinyDexEntryGo entry = entries.get(i);

            String imgClass = "";
            if (entry.isHave())
            {
                imgClass += " have";
            }
            if (entry.isShinyGo())
            {
                imgClass += " shiny_go";
            }
            if (entry.isShinyHome())
            {
                imgClass += " shiny_home";
            }

            String imgSrc = entry.getGoogleImageLink();
            if (imgSrc.isBlank())
            {
                String number = entry.getPokemonNumberText();
//                String filepath = "/Users/jimhendricks/GoogleDrive/Blog/stream/pokemon/" + number + ".png";
//                imgSrc = getDataUrl(filepath);
                imgSrc = getImageUrl(number);
            }

            html += "<td class=\"mini\"><img class=\"" + imgClass + "\" src=\"" + imgSrc + "\" /></td>";

            if ((i + 1) % COLUMNS == 0)
            {
                html += "</tr><tr>";
            }
        }

        html += "</tr>";
        return html + "</table>";
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
