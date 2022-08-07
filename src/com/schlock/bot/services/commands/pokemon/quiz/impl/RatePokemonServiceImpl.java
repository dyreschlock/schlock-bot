package com.schlock.bot.services.commands.pokemon.quiz.impl;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.entities.pokemon.PokemonShinyRating;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.AbstractChatGameListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.pokemon.quiz.RatePokemonService;
import com.schlock.bot.services.database.adhoc.DatabaseManager;
import com.schlock.bot.services.database.pokemon.PokemonShinyRatingDAO;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import org.apache.tapestry5.ioc.Messages;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RatePokemonServiceImpl extends AbstractChatGameListenerService implements RatePokemonService
{
    private static final String RATE_STARTED_KEY = "rate-started";
    private static final String RATE_ERROR_KEY = "rate-error";
    private static final String RATE_ACCEPTED_KEY = "rate-accepted";
    private static final String RATE_UPDATED_KEY = "rate-updated";

    private static final String RATE_NO_VOTES_KEY = "rate-no-votes";
    private static final String RATE_VOTING_DONE_KEY = "rate-voting-done";

    private static final String START_COMMAND = "!rate";

    private static final String END_COMMAND = START_COMMAND + " close";

    private static final String SHINY_RATED_BY_PERSON_KEY = "shiny-rated-person";

    private static final Integer GOOD = 4;
    private static final Integer NOT_BAD = 3;
    private static final Integer NOT_GOOD = 2;
    private static final Integer BAD = 1;

    private static final List<Integer> VOTE_VALUES = Arrays.asList(GOOD, NOT_BAD, NOT_GOOD, BAD);

    private final PokemonManagement pokemonManagement;
    private final DatabaseManager database;

    private final DeploymentConfiguration config;

    protected Pokemon currentPokemon;
    private Map<String, Integer> currentVotes;

    public RatePokemonServiceImpl(PokemonManagement pokemonManagement,
                                  DatabaseManager database,
                                  Messages messages,
                                  DeploymentConfiguration config)
    {
        super(messages);

        this.pokemonManagement = pokemonManagement;

        this.database = database;
        this.config = config;
    }

    public String getGameId()
    {
        return START_COMMAND.substring(1).trim();
    }

    public boolean isStarted()
    {
        return currentPokemon != null && currentVotes != null;
    }

    public boolean isAcceptRequest(String username, String message)
    {
        return message != null && message.toLowerCase().trim().startsWith(START_COMMAND.toLowerCase());
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    protected ListenerResponse processRequest(String username, String command)
    {
        if (!isStarted())
        {
            if (command.trim().equalsIgnoreCase(START_COMMAND))
            {
                return startRating();
            }
        }
        else if (isStarted())
        {
            String owner = config.getOwnerUsername();
            if (command.trim().equalsIgnoreCase(END_COMMAND) && username.equals(owner))
            {
                return finishRating();
            }
            else
            {
                return processRating(username, command);
            }
        }
        return nullResponse();
    }

    private ListenerResponse startRating()
    {
        currentPokemon = newPokemon();
        currentVotes = new HashMap<>();

        return formatSingleResponse(RATE_STARTED_KEY, currentPokemon.getName());
    }

    private Pokemon newPokemon()
    {
        List<String> currentlyRated = database.get(PokemonShinyRatingDAO.class).getNumCodesOfRated();

        Pokemon pokemon = pokemonManagement.getRandomPokemon(currentlyRated);
        return pokemon;
    }

    private ListenerResponse finishRating()
    {
        if (currentVotes.size() == 0)
        {
            resetGame();

            return formatSingleResponse(RATE_NO_VOTES_KEY);
        }

        int voteTotal = 0;
        int voteSum = 0;
        for (Integer rating : currentVotes.values())
        {
            voteTotal++;
            voteSum += rating;
        }

        String pokemonName = currentPokemon.getName();
        Double rating = ((double) voteSum) / ((double) voteTotal);

        PokemonShinyRating shinyRating = new PokemonShinyRating(currentPokemon, rating);
        database.save(shinyRating);

        resetGame();

        return formatAllResponse(RATE_VOTING_DONE_KEY, pokemonName, rating);
    }

    private void resetGame()
    {
        currentPokemon = null;
        currentVotes = null;
    }

    private ListenerResponse processRating(String username, String command)
    {
        try
        {
            String[] paramList = command.trim().split(" ");
            String ratingString = paramList[1];

            Integer rating = Integer.parseInt(ratingString);

            if (rating >= 1 && rating <= 4)
            {
                String key = RATE_ACCEPTED_KEY;
                if (currentVotes.containsKey(username))
                {
                    key = RATE_UPDATED_KEY;
                }

                currentVotes.put(username, rating);

                return formatSingleResponse(key, rating, username);
            }
        }
        catch(Exception e)
        {
        }
        return formatSingleResponse(RATE_ERROR_KEY);
    }
}
