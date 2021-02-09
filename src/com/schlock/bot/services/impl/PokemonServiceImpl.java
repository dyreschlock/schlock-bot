package com.schlock.bot.services.impl;

import com.schlock.bot.entities.Pokemon;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.PokemonService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PokemonServiceImpl implements PokemonService
{
    private static final String LIST_OF_POKEMON_FILE = "pokemon.html";

    private static final String DIV_CONTENT_ID = "content";

    protected static final String POKEMON_RETURN_FORMAT = "No. %s %s";
    protected static final String POKEMON_RETURN_NULL = "What?";

    private static final Integer FIRST_POKEMON = 1;
    private static final Integer LAST_POKEMON = 898;

    private final DeploymentContext context;

    private Map<Integer, Pokemon> pokemonByNumber;
    private Map<String, Pokemon> pokemonByName;


    public PokemonServiceImpl(DeploymentContext context)
    {
        this.context = context;
    }

    /**
     * Always returns a single pokemon, or nothing if bad syntax
     * @return String "No. 25 Pikachu" (example)
     */
    public String process(String in)
    {
        if (in.startsWith(POKEMON_COMMAND))
        {
            initialize();

            Pokemon pokemon = processPokemon(in);
            if (pokemon != null)
            {
                String number = Integer.toString(pokemon.getNumber());
                String name = pokemon.getName();

                return String.format(POKEMON_RETURN_FORMAT, number, name);
            }
        }
        return POKEMON_RETURN_NULL;
    }

    private Pokemon processPokemon(String in)
    {
        String commandText = in.substring(POKEMON_COMMAND.length());
        commandText = cleanText(commandText);

        if (commandText.startsWith("-"))
        {
            String pokemon = commandText.split("-")[1];

            Pokemon start = pokemonByNumber.get(FIRST_POKEMON);
            Pokemon end = getPokemonByNameNumber(pokemon);

            return getPokemonInRange(start, end);
        }
        if (commandText.endsWith("-"))
        {
            String pokemon = commandText.split("-")[0];

            Pokemon start = getPokemonByNameNumber(pokemon);
            Pokemon end = pokemonByNumber.get(LAST_POKEMON);

            return getPokemonInRange(start, end);
        }
        if (commandText.contains("-"))
        {
            String pokemonStart = commandText.split("-")[0];
            String pokemonEnd = commandText.split("-")[1];

            Pokemon start = getPokemonByNameNumber(pokemonStart);
            Pokemon end = getPokemonByNameNumber(pokemonEnd);

            return getPokemonInRange(start, end);
        }
        return getPokemonByNameNumber(commandText);
    }

    private Pokemon getPokemonByNameNumber(String commandText)
    {
        String text = cleanText(commandText);

        Integer number = getNumber(text);
        if(number != null)
        {
            return pokemonByNumber.get(number);
        }
        return pokemonByName.get(text);
    }

    private Pokemon getPokemonInRange(Pokemon start, Pokemon finish)
    {
        if (start == null || finish == null)
        {
            return null;
        }

        Integer startNumber = start.getNumber();
        Integer finishNumber = finish.getNumber();
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
            Integer number = Integer.parseInt(in);
            return number;
        }
        catch (NumberFormatException excpetion)
        {
        }
        return null;
    }

    private String cleanText(String text)
    {
        String newText = text.trim().toLowerCase().replace(" ", "");
        return newText;
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
        String fileLocation = context.getDataDirectory() + LIST_OF_POKEMON_FILE;

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

        Element trElement = null;
        for(int i = 2; i < 900; i++)
        {
            trElement = tbody.child(i);

            /* structure
            <tr>
                <td> #001 </td>  (Number)
                <td></td> (Picture)
                <td>
                    <a href="/pokemon/bulbasaur">Bulbasaur</a> (ID) (Name)
                </td>
                <td></td> (Type)
                <td></td> ..others
            </tr>
             */

            String numberText = trElement.child(0).textNodes().get(0).text();
            Integer number = Integer.parseInt(cleanNumberText(numberText));

            Element ahref = trElement.child(2).child(0);
            String name = ahref.textNodes().get(0).text();

            String href = ahref.attr("href");
            String id = href.split("/pokemon/")[1].toLowerCase();

            Pokemon pokemon = new Pokemon();
            pokemon.setNumber(number);
            pokemon.setName(name);
            pokemon.setId(id);

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
