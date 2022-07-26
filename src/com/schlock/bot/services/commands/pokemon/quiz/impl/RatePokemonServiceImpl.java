package com.schlock.bot.services.commands.pokemon.quiz.impl;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.services.commands.AbstractChatGameListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.pokemon.quiz.RatePokemonService;
import com.schlock.bot.services.database.adhoc.DatabaseManager;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import org.apache.tapestry5.ioc.Messages;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class RatePokemonServiceImpl extends AbstractChatGameListenerService implements RatePokemonService
{
    private static final String START_COMMAND = "!rate";

    private static final Integer GOOD = 4;
    private static final Integer NOT_BAD = 3;
    private static final Integer NOT_GOOD = 2;
    private static final Integer BAD = 1;

    private static final List<Integer> VOTE_VALUES = Arrays.asList(GOOD, NOT_BAD, NOT_GOOD, BAD);

    private final PokemonManagement pokemonManagement;
    private final DatabaseManager database;

    protected Pokemon currentPokemon;
    private Map<String, Integer> currentVotes;

    public RatePokemonServiceImpl(PokemonManagement pokemonManagement,
                                    DatabaseManager database,
                                    Messages messages)
    {
        super(messages);

        this.pokemonManagement = pokemonManagement;

        this.database = database;
    }

    public String getGameId()
    {
        return START_COMMAND.substring(1).trim();
    }

    public boolean isStarted()
    {
        return currentPokemon != null && currentVotes != null;
    }

    @Override
    public boolean isAcceptRequest(String username, String message)
    {
        return message != null && START_COMMAND.startsWith(message.toLowerCase().trim());
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    @Override
    protected ListenerResponse processRequest(String username, String command)
    {
        return null;
    }

    private Pokemon getNewPokemon()
    {
        return null;
    }
}
