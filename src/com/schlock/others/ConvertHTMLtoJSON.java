package com.schlock.others;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.entities.pokemon.impl.PokemonManagementImpl;
import com.schlock.bot.services.impl.DeploymentConfigurationImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConvertHTMLtoJSON
{
    private static final String POKEMON_DATA_FILE = PokemonManagementImpl.POKEMON_DATA_FILE;

    private static final String LIST_OF_POKEMON_FILE = "pokemon.html";
    private static final String LIST_OF_HISUIAN_POKEMON_FILE = "hisuian_pokedex.html";

    private static final String DIV_CONTENT_ID = "content";

    private DeploymentConfiguration config;

    private Map<Integer, Pokemon> pokemonByNumber = new HashMap<>();
    private Map<String, Pokemon> pokemonByName = new HashMap<>();

    public void run() throws Exception
    {
        config = new DeploymentConfigurationImpl();

        loadPokemon();
        loadHisuianPokemon();

        outputPokemonAsJSON();
    }

    private void outputPokemonAsJSON() throws Exception
    {
        JSONArray array = new JSONArray();

        for (Pokemon pokemon : pokemonByNumber.values())
        {
            JSONObject object = pokemon.createJSON();

            array.add(object);
        }

        String outputLocation = config.getDataDirectory() + POKEMON_DATA_FILE;

        File pokemonDataFile = new File(outputLocation);
        if (pokemonDataFile.exists())
        {
            pokemonDataFile.delete();
        }
        pokemonDataFile.createNewFile();


        String pokemonData = array.toString();

        FileWriter fileWriter = new FileWriter(pokemonDataFile);
        fileWriter.write(pokemonData);
        fileWriter.close();
    }


    private void loadHisuianPokemon()
    {
        String fileLocation = config.getPokemonDataDirectory() + LIST_OF_HISUIAN_POKEMON_FILE;

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

        /*
        <div id="content">
            <main> (child no. 2)
                <table> (child no. 5)
                </table>
            </main>
        </div>
         */

        Element contentElement = htmlFile.getElementById(DIV_CONTENT_ID);
        Element tbody = contentElement.child(1).child(4).child(0);

        Element trElement;
        for(int i = 1; i < 225; i++)
        {
            trElement = tbody.child(i);

            /*
            <tr>
                <td> #001 </td>
                <td> table - image </td>
                <td><a> Rowlet </a></td>
                <td>
                    <a href=" ... pokedex-swsh/grass.shtml"></a>
                    <a href=" ... pokedex-swsh/flying.shtml"></a>
                </td>
            </tr>
             */


            Element name_ahref = trElement.child(2).child(0);

            String name_href = name_ahref.attr("href");
            String id = name_href.split("/pokedex-swsh/")[1].toLowerCase();
            id = id.split("/")[0];

            Pokemon pokemon = pokemonByName.get(id);

            if (pokemon == null)
            {
                String temp = "";
            }

            String numberText = trElement.child(0).textNodes().get(0).text();
            Integer hisuiNumber = Integer.parseInt(cleanNumberText(numberText));
            pokemon.setHisuiNumber(hisuiNumber);

            Element type1_ahref = trElement.child(3).child(0);
            String type1_href = type1_ahref.attr("href");
            String type1 = type1_href.split("pokedex-swsh/")[1].toLowerCase();
            type1 = type1.split(".shtml")[0];
            pokemon.setHisuiType1(StringUtils.capitalize(type1));

            try
            {
                Element type2_ahref = trElement.child(3).child(1);
                String type2_href = type2_ahref.attr("href");
                String type2 = type2_href.split("pokedex-swsh/")[1].toLowerCase();
                type2 = type2.split(".shtml")[0];
                pokemon.setHisuiType2(StringUtils.capitalize(type2));
            }
            catch (IndexOutOfBoundsException e)
            {
            }
        }
    }

    private void loadPokemon()
    {
        String fileLocation = config.getPokemonDataDirectory() + LIST_OF_POKEMON_FILE;

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
        for (int i = 2; i < 1012; i++)
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

    public static void main(String[] args) throws Exception
    {
        new ConvertHTMLtoJSON().run();
    }
}
