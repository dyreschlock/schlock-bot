package com.schlock.bot.components.pokemon;

import org.apache.commons.codec.binary.Base64;
import org.apache.tapestry5.ioc.Messages;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class OverallShinyDex
{
    protected static final Integer COLUMNS = 6;
    protected static final Integer ROWS = 5;

    protected static final Integer MAX_POKEMON = 900;

    @Inject
    private Messages messages;

    public String getShinyDexMessage()
    {
        int count = 0;

        String message = messages.format("shiny-dex", count);
        return message;
    }

    public String getTableHTML()
    {
        String html = "<table class=\"dex\">";

        html += "<tr>";

        for(int i = 0; i < MAX_POKEMON; i++)
        {
            int cellNumber = i+1;

            int pokemonNumber = getPokemonNumber(cellNumber);

            html += getCell(pokemonNumber, true);

            if ((i + 1) % (COLUMNS * COLUMNS) == 0)
            {
                html += "</tr><tr>";
            }
        }

        html += "</tr>";
        return html + "</table>";
    }

    protected Integer getPokemonNumber(int cellNumber)
    {
        int i = cellNumber -1;

        int columnPositionInBox = (i % COLUMNS) +1;

        int numberedSetOfSix = (i / COLUMNS);

        int rowInBox = (numberedSetOfSix / COLUMNS) % ROWS;

        int boxNumber = getBoxNumber(numberedSetOfSix);
        int startNumInBox = (boxNumber) * (COLUMNS * ROWS);

        int poke = columnPositionInBox + startNumInBox + (rowInBox * COLUMNS);

        return poke;
    }

    protected Integer getBoxNumber(int numberedSetOfSix)
    {
        int columnForBox = numberedSetOfSix % COLUMNS;
        int rowForBox = numberedSetOfSix / (COLUMNS * ROWS);

        return columnForBox + (rowForBox * COLUMNS);
    }


    private String getCell(Integer number, boolean shiny)
    {
        String numberText = getNumberText(number);
        String filepath = "/Users/jimhendricks/GoogleDrive/Blog/stream/pokemon/" + numberText + ".png";

        String imgSrc = getDataUrl(filepath);

        String imgClass = "";
        if (shiny)
        {
            imgClass = "have shiny";
        }

        String cell = "<td class=\"mini\"><img class=\"" + imgClass + "\" src=\"" + imgSrc + "\" /></td>";
        return cell;
    }

    private String getNumberText(Integer n)
    {
        String number = Integer.toString(n);
        if (number.length() == 1)
        {
            number = "00" + number;
        }
        if (number.length() == 2)
        {
            number = "0" + number;
        }
        return number;
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
