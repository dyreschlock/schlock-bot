package com.schlock.bot.components.pokemon;

import com.schlock.bot.entities.pokemon.PokemonRegion;
import com.schlock.bot.services.commands.pokemon.shiny.ShinyDexService;
import org.apache.tapestry5.ioc.Messages;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

public class OverallShinyDex
{
    protected static final Integer COLUMNS = 6;
    protected static final Integer ROWS = 5;

    protected static final Integer MAX_POKEMON = 900;

    private static final String[] REMAINING_ROW = {"901", "902", "903", "904", "905", null};

    private static final String[] ROW1_ALOLA = {"a_019", "a_020", "a_026", "a_027", "a_028", "a_037"};
    private static final String[] ROW2_ALOLA = {"a_038", "a_050", "a_051", "a_052", "a_053", "a_074"};
    private static final String[] ROW3_ALOLA = {"a_075", "a_076", "a_088", "a_089", "a_103", "a_105"};

    private static final String[] ROW1_GALAR = {"g_052", "863", "g_077", "g_078", "g_083", "865"};
    private static final String[] ROW2_GALAR = {"g_110", "g_122", "866", "g_222", "864", "g_263"};
    private static final String[] ROW3_GALAR = {"g_264", "862", "g_554", "g_555", "g_562", "867"};
    private static final String[] ROW4_GALAR = {"g_618", "g_079", "g_080", "g_199", null, null};
    private static final String[] ROW5_GALAR = {"g_144", "g_145", "g_146", null, null, null};

    private static final String[] ROW1_HISUI = {"h_157", "h_503", "h_724", null, null, null};
    private static final String[] ROW2_HISUI = {"h_058", "h_059", "h_100", "h_101", "h_211", "904"};
    private static final String[] ROW3_HISUI = {"h_215", "903", "h_549", "h_550", "902", null};
    private static final String[] ROW4_HISUI = {"h_570", "h_571", "h_628", "h_705", "h_706", "h_713"};
    private static final String[] ROW5_HISUI = {"899", "900", "901", null, null, null};

    private static final String[] ROW_BLANK = {null, null, null, null, null, null};

    private static final List<String[]> ROW1_REMAINING = Arrays.asList(REMAINING_ROW, ROW1_ALOLA, ROW1_GALAR, ROW1_HISUI, ROW_BLANK, ROW_BLANK);
    private static final List<String[]> ROW2_REMAINING = Arrays.asList(ROW_BLANK, ROW2_ALOLA, ROW2_GALAR, ROW2_HISUI, ROW_BLANK, ROW_BLANK);
    private static final List<String[]> ROW3_REMAINING = Arrays.asList(ROW_BLANK, ROW3_ALOLA, ROW3_GALAR, ROW3_HISUI, ROW_BLANK, ROW_BLANK);
    private static final List<String[]> ROW4_REMAINING = Arrays.asList(ROW_BLANK, ROW_BLANK, ROW4_GALAR, ROW4_HISUI, ROW_BLANK, ROW_BLANK);
    private static final List<String[]> ROW5_REMAINING = Arrays.asList(ROW_BLANK, ROW_BLANK, ROW5_GALAR, ROW5_HISUI, ROW_BLANK, ROW_BLANK);

    @Inject
    private ShinyDexService shinyDexService;

    @Inject
    private Messages messages;

    public String getShinyDexMessage()
    {
        int count = shinyDexService.getOverallShinyCount();
        int total = shinyDexService.getOverallTotalCount();

        String overall = messages.format("overall", count, total);

        count = shinyDexService.getNormalShinyCount();
        total = shinyDexService.getNormalTotalCount();

        String normal = messages.format("normal", count, total);

        count = shinyDexService.getRegionShinyCount(PokemonRegion.ALOLA);
        total = shinyDexService.getRegionTotalCount(PokemonRegion.ALOLA);

        String alola = messages.format("alola", count, total);

        count = shinyDexService.getRegionShinyCount(PokemonRegion.GALAR);
        total = shinyDexService.getRegionTotalCount(PokemonRegion.GALAR);

        String galar = messages.format("galar", count, total);

        count = shinyDexService.getRegionShinyCount(PokemonRegion.HISUI);
        total = shinyDexService.getRegionTotalCount(PokemonRegion.HISUI);

        String hisui = messages.format("hisui", count, total);

        String message = messages.format("header-message", overall, normal, alola, galar, hisui);
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

            String pokeNumberText = getNumberText(pokemonNumber);
            boolean shiny = shinyDexService.isHaveShiny(pokeNumberText);

            html += getCell(pokemonNumber, shiny);

            if ((i + 1) % (COLUMNS * COLUMNS) == 0)
            {
                html += "</tr><tr>";
            }
        }
        html += "</tr>";

        html += processRowList(ROW1_REMAINING);
        html += processRowList(ROW2_REMAINING);
        html += processRowList(ROW3_REMAINING);
        html += processRowList(ROW4_REMAINING);
        html += processRowList(ROW5_REMAINING);

        return html + "</table>";
    }

    private String processRowList(List<String[]> row)
    {
        String html = "<tr>";

        for(int boxIndex = 0; boxIndex < row.size(); boxIndex++)
        {
            String[] box = row.get(boxIndex);

            boolean bottom = boxIndex == row.size() -1;

            for(int colIndex = 0; colIndex < box.length; colIndex++)
            {
                String pokeNumber = box[colIndex];
                boolean edge = colIndex == box.length -1;

                if (pokeNumber != null)
                {
                    boolean shiny = shinyDexService.isHaveShiny(pokeNumber);

                    html += getCell(pokeNumber, shiny, edge, bottom);
                }
                else
                {
                    if (edge)
                    {
                        html += "<td class=\"mini edge\"></td>";
                    }
                    else
                    {
                        html += "<td class=\"mini\"></td>";
                    }
                }
            }
        }

        return html + "</tr>";
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

        boolean edge = (number % COLUMNS) == 0;
        boolean bottom = isBottomRowOfBox(number);

        return getCell(numberText, shiny, edge, bottom);
    }

    private String getCell(String numberCode, boolean shiny, boolean edge, boolean bottom)
    {
        String imgSrc = getImageUrl(numberCode);

        String imgClass = "have available ";
        if (shiny)
        {
            imgClass += "shiny ";
        }

        String tdClass = "mini ";
        if (edge)
        {
            tdClass += "edge ";
        }
        if (bottom)
        {
            tdClass += "bottom ";
        }

        String cell = "<td class=\"" + tdClass + "\"><img class=\"" + imgClass + "\" src=\"" + imgSrc + "\" /></td>";
        return cell;
    }

    private boolean isBottomRowOfBox(Integer pokemonNumber)
    {
        int positionInBox = (pokemonNumber - 1) % (COLUMNS * ROWS);
        int minusTopRows = positionInBox - (COLUMNS * (ROWS - 1));

        boolean bottom = minusTopRows >= 0;
        return bottom;
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

    private String getImageUrl(String number)
    {
        return "/img/pokemon/" + number + ".png";
    }
}
