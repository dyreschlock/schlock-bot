package com.schlock.bot.services.entities.pokemon.impl;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.entities.pokemon.PokemonRegion;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.entities.JSONEntityManagement;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.PokemonUtils;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import java.io.*;
import java.util.*;

public class PokemonManagementImpl extends JSONEntityManagement implements PokemonManagement
{
    public static final String POKEMON_DATA_FILE = "pokemon_data.json";

    private static final String HISUI_PARAM = PokemonUtilsImpl.HISUI;

    private static final List<Integer> ALOLAN_POKEMON_NUMBERS = Arrays.asList(19, 20, 26, 27, 28, 37, 38, 50, 51, 52, 53, 74, 75, 76, 88, 89, 103, 105);

    private final PokemonUtils pokemonUtils;

    private final DeploymentConfiguration config;

    private Map<Integer, Pokemon> pokemonByNumber;
    private Map<String, Pokemon> pokemonByName;

    private Map<Integer, Pokemon> hisuianByNumber;


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

    public boolean isHisuiSearch(String c)
    {
        String command = cleanText(c);
        return HISUI_PARAM.equalsIgnoreCase(command);
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

    public Pokemon getRandomPokemonInHisui()
    {
        initialize();

        int total = hisuianByNumber.size();
        int random = new Random().nextInt(total);

        return hisuianByNumber.get(random);
    }

    public Pokemon getRandomPokemon(List<String> exceptions)
    {
        initialize();

        List<Integer> dontInclude = new ArrayList<>();
        for (String except : exceptions)
        {
            Integer number = Integer.parseInt(except);
            dontInclude.add(number);
        }

        List<Integer> availableList = new ArrayList<>();
        for (Integer number : pokemonByNumber.keySet())
        {
            if (!dontInclude.contains(number))
            {
                availableList.add(number);
            }
        }

        int total = availableList.size();
        int random = new Random().nextInt(total);

        Integer number = availableList.get(random);

        return pokemonByNumber.get(number);
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
        initialize();

        List<Pokemon> pokemon = new ArrayList<>();

        int start = pokemonUtils.returnFirstPokemonNumberInGeneration(gen);
        int end = pokemonUtils.returnLastPokemonNumberInGeneration(gen);

        for(int i = start; i <= end; i++)
        {
            pokemon.add(pokemonByNumber.get(i));
        }
        return pokemon;
    }

    public List<Pokemon> getAllPokemonInNumberOrder()
    {
        initialize();

        List<Pokemon> pokemon = new ArrayList<>();
        pokemon.addAll(pokemonByNumber.values());

        Collections.sort(pokemon, new Comparator<Pokemon>()
        {
            @Override
            public int compare(Pokemon o1, Pokemon o2)
            {
                return o1.getNumber() - o2.getNumber();
            }
        });

        return pokemon;
    }

    public List<Pokemon> getHisuiPokemonInNumberOrder()
    {
        initialize();

        List<Pokemon> pokemon = new ArrayList<>();

        pokemon.addAll(hisuianByNumber.values());

        Collections.sort(pokemon, new Comparator<Pokemon>()
        {
            @Override
            public int compare(Pokemon o1, Pokemon o2)
            {
                return o1.getHisuiNumber() - o2.getHisuiNumber();
            }
        });

        return pokemon;
    }

    public List<Pokemon> getAllPokemonInRegion(PokemonRegion region)
    {
        initialize();

        List<Pokemon> pokemon = new ArrayList<>();
        for (Integer number : region.pokemonNumbers())
        {
            Pokemon poke = pokemonByNumber.get(number);
            pokemon.add(poke);
        }
        return pokemon;
    }

    public boolean isAlolanAvailable(Pokemon pokemon)
    {
        Integer number = pokemon.getNumber();

        return ALOLAN_POKEMON_NUMBERS.contains(number);
    }

    public List<Integer> getAlolanPokemonNumbers()
    {
        return ALOLAN_POKEMON_NUMBERS;
    }

    private void initialize()
    {
        if (pokemonByName == null || pokemonByNumber == null || hisuianByNumber == null)
        {
            pokemonByName = new HashMap<>();
            pokemonByNumber = new HashMap<>();

            hisuianByNumber = new HashMap<>();

            loadPokemon();
        }
    }

    private void loadPokemon()
    {
        JSONArray pokemonData = readJSONfromFile();

        Iterator iter = pokemonData.iterator();
        while (iter.hasNext())
        {
            Object obj = iter.next();
            try
            {
                JSONObject json = (JSONObject) obj;

                Pokemon pokemon = Pokemon.createFromJSON(json);

                pokemonByName.put(pokemon.getId(), pokemon);
                pokemonByNumber.put(pokemon.getNumber(), pokemon);

                if (pokemon.getHisuiNumber() != null)
                {
                    hisuianByNumber.put(pokemon.getHisuiNumber(), pokemon);
                }
            }
            catch (ClassCastException e)
            {
            }
        }
    }

    private JSONArray readJSONfromFile()
    {
        String filepath = config.getDataDirectory() + POKEMON_DATA_FILE;
        String content = readFileContents(filepath);
        return new JSONArray(content);
    }
}
