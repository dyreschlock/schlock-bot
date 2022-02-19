package com.schlock.bot.services.commands.pokemon.quiz.impl;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.AbstractChatGameListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.pokemon.quiz.GenCompletionQuizService;
import com.schlock.bot.services.database.adhoc.DatabaseManager;
import com.schlock.bot.services.entities.base.UserManagement;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import org.apache.tapestry5.ioc.Messages;

import java.util.ArrayList;
import java.util.List;

public class GenCompletionQuizServiceImpl extends AbstractChatGameListenerService implements GenCompletionQuizService
{
    private static final String GAME_COMMAND = "!completionquiz ";
    private static final String START_PARAM = "start";
    private static final String END_PARAM = "end";

    private static final String GAME_STARTED_KEY = "completionquiz-started";
    private static final String GAME_ENDED_KEY = "completionquiz-game-ended";
    private static final String GAME_COMPLETED_KEY = "completionquiz-game-completed";

    private static final String GAME_ALREADY_STARTED_KEY = "completionquiz-already-started";
    private static final String GAME_NOT_STARTED_KEY = "completionquiz-game-not-started";

    private static final String DEFAULT_GEN = "gen1";

    private final PokemonManagement pokemonManagement;
    private final UserManagement userManagement;

    private final DatabaseManager database;
    private final DeploymentConfiguration config;

    private List<Pokemon> remainingPokemon;
    private List<String> usernamesParticipated;

    public GenCompletionQuizServiceImpl(PokemonManagement pokemonManagement,
                                        UserManagement userManagement,
                                        DatabaseManager database,
                                        Messages messages,
                                        DeploymentConfiguration config)
    {
        super(messages);

        this.pokemonManagement = pokemonManagement;
        this.userManagement = userManagement;

        this.database = database;
        this.config = config;
    }

    public String getGameId()
    {
        return GAME_COMMAND.substring(1).trim();
    }

    public boolean isStarted()
    {
        return remainingPokemon != null;
    }

    public boolean isAcceptRequest(String username, String message)
    {
        return isOn();
    }

    public boolean isTerminateAfterRequest()
    {
        return false;
    }

    protected ListenerResponse processRequest(String username, String message)
    {
        if (message.startsWith(GAME_COMMAND))
        {
            final String ADMIN = config.getOwnerUsername().toLowerCase();

            if (!username.toLowerCase().equals(ADMIN))
            {
                return notAdminReponse();
            }

            String params = message.substring(GAME_COMMAND.length()).trim();
            if (params.equals(START_PARAM))
            {
                if (isStarted())
                {
                    return formatSingleResponse(GAME_ALREADY_STARTED_KEY);
                }

                initializeAndStartGame();
                return formatSingleResponse(GAME_STARTED_KEY, DEFAULT_GEN);
            }

            if (params.equals(END_PARAM))
            {
                if (!isStarted())
                {
                    return formatSingleResponse(GAME_NOT_STARTED_KEY);
                }

                resetAndEndGame();
                return formatSingleResponse(GAME_ENDED_KEY);
            }
        }

        if (isStarted())
        {
            return screenInput(username, message);
        }

        return ListenerResponse.relayNothing();
    }

    private void initializeAndStartGame()
    {
        this.remainingPokemon = pokemonManagement.getAllPokemonInGen(DEFAULT_GEN);
        this.usernamesParticipated = new ArrayList<>();
    }

    private void resetAndEndGame()
    {
        this.remainingPokemon = null;
        this.usernamesParticipated = null;
    }

    private ListenerResponse screenInput(String username, String message)
    {
        int oldRemainingNumber = remainingPokemon.size();

        removeGuessedPokemon(message);

        int newRemainingNumber = remainingPokemon.size();


        if (newRemainingNumber == 0)
        {
            if (!usernamesParticipated.contains(username))
            {
                usernamesParticipated.add(username);
            }


            return formatSingleResponse(GAME_COMPLETED_KEY);
        }

        if (newRemainingNumber < oldRemainingNumber)
        {
            if (!usernamesParticipated.contains(username))
            {
                usernamesParticipated.add(username);
            }


        }

        return ListenerResponse.relayNothing();
    }

    private void removeGuessedPokemon(String message)
    {
        String text = message.toLowerCase().replaceAll(" ", "");

        List<Pokemon> guessed = new ArrayList<>();
        for (Pokemon pokemon : remainingPokemon)
        {
            if (text.contains(pokemon.getId().toLowerCase()))
            {
                guessed.add(pokemon);
            }
        }

        for (Pokemon pokemon : guessed)
        {
            remainingPokemon.remove(pokemon);
        }
    }
}
