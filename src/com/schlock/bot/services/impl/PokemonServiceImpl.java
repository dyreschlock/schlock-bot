package com.schlock.bot.services.impl;

import com.schlock.bot.entities.Pokemon;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.PokemonService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PokemonServiceImpl implements PokemonService
{
    private static final String LIST_OF_POKEMON_FILE = "pokemon.html";

    private static final String DIV_CONTENT_ID = "content";


    private final DeploymentContext context;

    private Map<Integer, Pokemon> pokemonByNumber;
    private Map<String, Pokemon> pokemonByName;


    public PokemonServiceImpl(DeploymentContext context)
    {
        this.context = context;
    }

    public String process(String in)
    {
        initialize();


        return null;
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
            String id = href.split("/pokemon/")[1];

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
