package com.schlock.bot.services.entities.pokemon.impl;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.PokemonUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PokemonManagementImpl implements PokemonManagement
{
    private static final String LIST_OF_POKEMON_FILE = "pokemon.html";

    private static final String DIV_CONTENT_ID = "content";

    private final PokemonUtils pokemonUtils;

    private final DeploymentConfiguration config;

    private Map<Integer, Pokemon> pokemonByNumber;
    private Map<String, Pokemon> pokemonByName;


    public PokemonManagementImpl(PokemonUtils pokemonUtils,
                                 DeploymentConfiguration config)
    {
        this.pokemonUtils = pokemonUtils;
        this.config = config;
    }

    public boolean isGenSearch(String c)
    {
        String command = cleanText(c);
        return pokemonUtils.isGenerationId(command);
    }

    public boolean isRangeSearch(String c)
    {
        String command = cleanText(c);
        return command.contains(("-"));
    }

    public Pokemon getRandomPokemon()
    {
        initialize();

        Pokemon start = getFirstPokemon();
        Pokemon end = getLastPokemon();

        return getRandomPokemonInRange(start, end);
    }

    public Pokemon getRandomPokemonInGen(String g)
    {
        String gen = cleanText(g);
        String range = pokemonUtils.returnGenerationRange(gen);
        return getRandomPokemonInRange(range);
    }

    private Pokemon getFirstPokemon()
    {
        int first = 1;
        return pokemonByNumber.get(first);
    }

    private Pokemon getLastPokemon()
    {
        int last = pokemonByNumber.size();
        return pokemonByNumber.get(last);
    }

    public Pokemon getPokemonFromText(String commandText)
    {
        initialize();

        String text = cleanText(commandText);

        Integer number = getNumber(text);
        if(number != null)
        {
            if (number <= 0)
            {
                return getFirstPokemon();
            }
            if (number >= pokemonByNumber.size())
            {
                return getLastPokemon();
            }

            return pokemonByNumber.get(number);
        }
        return pokemonByName.get(text);
    }

    private String cleanText(String text)
    {
        return text.trim().toLowerCase().replace(" ", "");
    }

    public Pokemon getRandomPokemonInRange(String r)
    {
        initialize();

        String rangeText = cleanText(r);

        Pokemon start;
        Pokemon end;

        if (rangeText.startsWith("-"))
        {
            String pokemon = rangeText.split("-")[1];

            start = getFirstPokemon();
            end = getPokemonFromTextOrLastInGen(pokemon);
        }
        else if (rangeText.endsWith("-"))
        {
            String pokemon = rangeText.split("-")[0];

            start = getPokemonFromTextOrFirstInGen(pokemon);
            end = getLastPokemon();
        }
        else
        {
            String pokemonStart = rangeText.split("-")[0];
            String pokemonEnd = rangeText.split("-")[1];

            start = getPokemonFromTextOrFirstInGen(pokemonStart);
            end = getPokemonFromTextOrLastInGen(pokemonEnd);
        }
        return getRandomPokemonInRange(start, end);
    }

    private Pokemon getPokemonFromTextOrFirstInGen(String input)
    {
        if (isGenSearch(input))
        {
            Integer s = pokemonUtils.returnFirstPokemonNumberInGeneration(input);
            return pokemonByNumber.get(s);
        }
        return getPokemonFromText(input);
    }

    private Pokemon getPokemonFromTextOrLastInGen(String input)
    {
        if (isGenSearch(input))
        {
            Integer e = pokemonUtils.returnLastPokemonNumberInGeneration(input);
            return pokemonByNumber.get(e);
        }
        return getPokemonFromText(input);
    }

    private Pokemon getRandomPokemonInRange(Pokemon start, Pokemon finish)
    {
        if (start == null || finish == null)
        {
            return null;
        }

        int startNumber = start.getNumber();
        int finishNumber = finish.getNumber();
        if (startNumber > finishNumber)
        {
            startNumber = finish.getNumber();
            finishNumber = start.getNumber();
        }
        if (startNumber == finishNumber)
        {
            return start;
        }

        int total = finishNumber - startNumber +1;
        int random = new Random().nextInt(total);
        random = random + startNumber;

        return pokemonByNumber.get(random);
    }


    private Integer getNumber(String in)
    {
        try
        {
            return Integer.parseInt(in);
        }
        catch (NumberFormatException e)
        {
        }
        return null;
    }


    public List<Pokemon> getAllPokemonInGen(String gen)
    {
        List<Pokemon> pokemon = new ArrayList<>();

        int start = pokemonUtils.returnFirstPokemonNumberInGeneration(gen);
        int end = pokemonUtils.returnLastPokemonNumberInGeneration(gen);

        for(int i = start; i <= end; i++)
        {
            pokemon.add(pokemonByNumber.get(i));
        }
        return pokemon;
    }

    private void initialize()
    {
        if (pokemonByName == null || pokemonByNumber == null)
        {
            pokemonByName = new HashMap<>();
            pokemonByNumber = new HashMap<>();

            loadPokemon();
        }
    }

    private void loadPokemon()
    {
        String fileLocation = config.getDataDirectory() + LIST_OF_POKEMON_FILE;

        Document htmlFile = null;
        try
        {
            htmlFile = Jsoup.parse(new File(fileLocation), "ISO-8859-1");
        }
        catch(IOException e)
        {
            e.printStackTrace();
            System.out.println(fileLocation);
        }

        /* element structure
        <div id="content">
            <div></div>
            <main> (child no. 2)
                ...bunch of stuff
                <table> (child no. 15)
                    <tbody>
                        <tr></tr>
                        <tr></tr> (Headers)
                        <tr> ... </tr> (from no. 3) <-- iterate on these
                        ...
                    </tbody>
                </table>
            </main>
        </div>
         */

        Element contentElement = htmlFile.getElementById(DIV_CONTENT_ID);
        Element tbody = contentElement.child(1).child(14).child(0);

        Element trElement;
        for (int i = 2; i < 900; i++)
        {
            trElement = tbody.child(i);

            /* structure
            <tr>
            0   <td> #001 </td>  (Number)
            1   <td></td> (Picture)
            2   <td>
                    <a href="/pokemon/bulbasaur">Bulbasaur</a> (ID) (Name)
                </td>
            3   <td>
                    <a href="/pokemon/type/grass"> ... </a> (Type 1)
                    <a href="/pokemon/type/poison"> ... </a> (Type 2)
                </td>
            4   <td></td> (Abilities)
            5   <td></td> (Base Stats x6)
            6   <td></td>
            7   <td></td>
            8   <td></td>
            9   <td></td>
            10  <td></td>
            </tr>
             */

            Pokemon pokemon = new Pokemon();

            String numberText = trElement.child(0).textNodes().get(0).text();
            Integer number = Integer.parseInt(cleanNumberText(numberText));
            pokemon.setNumber(number);

            Element name_ahref = trElement.child(2).child(0);
            String name = name_ahref.textNodes().get(0).text();
            pokemon.setName(name);

            String name_href = name_ahref.attr("href");
            String id = name_href.split("/pokemon/")[1].toLowerCase();
            pokemon.setId(id);

            Element type1_ahref = trElement.child(3).child(0);
            String type1_href = type1_ahref.attr("href");
            String type1 = type1_href.split("/pokemon/type/")[1].toLowerCase();
            pokemon.setType1(StringUtils.capitalize(type1));

            try
            {
                Element type2_ahref = trElement.child(3).child(1);
                String type2_href = type2_ahref.attr("href");
                String type2 = type2_href.split("/pokemon/type/")[1].toLowerCase();
                pokemon.setType2(StringUtils.capitalize(type2));
            }
            catch (IndexOutOfBoundsException e)
            {
            }

            for (int e = 5; e < 11; e++)
            {
                String stat = trElement.child(e).textNodes().get(0).text();
                Integer s = Integer.parseInt(stat);

                pokemon.incrementBasestats(s);
            }

            pokemonByNumber.put(number, pokemon);
            pokemonByName.put(id, pokemon);
        }
    }

    private String cleanNumberText(String numberText)
    {
        String clean = numberText;

        clean = clean.replace("#", "");
        clean = clean.trim();

        return clean;
    }
}
