package com.schlock.bot.services.entities.game.impl;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.schlock.bot.entities.game.PocketGame;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.entities.JSONEntityManagement;
import com.schlock.bot.services.entities.game.RetroGameManagement;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RetroGameManagementImpl extends JSONEntityManagement implements RetroGameManagement
{
    public static final String ARCADE = "Arcade";

    private static final String DATA_FILE = "games.json";

    private final DeploymentConfiguration config;

    private List<String> cachedGenre;
    private List<PocketGame> cachedGames;


    public RetroGameManagementImpl(DeploymentConfiguration config)
    {
        this.config = config;
    }


    public String getGenre(String text)
    {
        initialize();

        for(String genre : cachedGenre)
        {
            if (StringUtils.equalsIgnoreCase(genre, text))
            {
                return genre;
            }
        }
        return null;
    }

    public List<PocketGame> getGames(String title)
    {
        initialize();

        List<PocketGame> results = new ArrayList<>();
        for(PocketGame game : cachedGames)
        {
            String gameformat = "%s (%s)";
            String gamestring = String.format(gameformat, game.getGameName(), game.getCoreName());

            if (StringUtils.startsWithIgnoreCase(gamestring, title))
            {
                results.add(game);
            }
        }
        return results;
    }

    public PocketGame getRandom()
    {
        initialize();

        int index = new Random().nextInt(cachedGames.size());
        return cachedGames.get(index);
    }


    private void initialize()
    {
        if (cachedGames == null || cachedGenre == null)
        {
            cachedGames = loadGames();
            cachedGenre = new ArrayList<>();

            cachedGenre.add(ARCADE);
            for(PocketGame game : cachedGames)
            {
                String genre = game.getGenre();
                if (!cachedGenre.contains(genre))
                {
                    cachedGenre.add(genre);
                }
            }
        }
    }

    private List<PocketGame> loadGames()
    {
        String filepath = config.getRetroGameDataDirectory() + DATA_FILE;
        String jsonString = readFileContents(filepath);

        Gson gson = new GsonBuilder().create();

        Type listOfGames = new TypeToken<ArrayList<PocketGame>>(){}.getType();

        List<PocketGame> games = gson.fromJson(jsonString, listOfGames);
        return games;
    }
}
