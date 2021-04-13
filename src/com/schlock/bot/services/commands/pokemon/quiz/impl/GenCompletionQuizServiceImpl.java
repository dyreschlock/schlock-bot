package com.schlock.bot.services.commands.pokemon.quiz.impl;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.AbstractChatGameListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.pokemon.quiz.GenCompletionQuizService;
import org.apache.tapestry5.ioc.Messages;

import java.util.List;

public class GenCompletionQuizServiceImpl extends AbstractChatGameListenerService implements GenCompletionQuizService
{
    private static final String START_COMMAND = "!completionquiz ";

    private final DeploymentConfiguration config;

    private List<Pokemon> remainingPokemon;

    public GenCompletionQuizServiceImpl(Messages messages,
                                        DeploymentConfiguration config)
    {
        super(messages);

        this.config = config;
    }

    public String getGameId()
    {
        return START_COMMAND.substring(1).trim();
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

    protected ListenerResponse processRequest(String username, String command)
    {



//        final String ADMIN = config.getOwnerUsername().toLowerCase();
//
//        return username.toLowerCase().equals(ADMIN) &&
//                message != null &&
//                message.toLowerCase().startsWith(START_COMMAND);
        return null;
    }
}
