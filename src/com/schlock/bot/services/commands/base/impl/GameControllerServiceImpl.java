package com.schlock.bot.services.commands.base.impl;

import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.AbstractListenerService;
import com.schlock.bot.services.commands.ChatGameListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.base.GameControllerService;
import org.apache.tapestry5.ioc.Messages;

import java.util.Map;

public class GameControllerServiceImpl extends AbstractListenerService implements GameControllerService
{
    private final static String TURN_ON_COMMAND = "!turnon ";
    private final static String TURN_OFF_COMMAND = "!turnoff ";

    private final static String NOT_ADMIN_KEY = "not-admin-response";
    private final static String GAME_UNKNOWN_KEY = "game-unknown";

    private final static String CANT_TURN_OFF_KEY = "cant-turn-off";
    private final static String CANT_TURN_ON_KEY = "cant-turn-on";
    private final static String ALREADY_ON_KEY = "game-already-on";
    private final static String ALREADY_OFF_KEY = "game-already-off";
    private final static String TURNED_ON_KEY = "game-turned-on";
    private final static String TURNED_OFF_KEY = "game-turned-off";

    private final Map<String, ChatGameListenerService> chatGames;

    private final DeploymentConfiguration config;

    public GameControllerServiceImpl(Map<String, ChatGameListenerService> chatGames,
                                     Messages messages,
                                     DeploymentConfiguration config)
    {
        super(messages);

        this.chatGames = chatGames;
        this.config = config;
    }

    public boolean isAcceptRequest(String username, String message)
    {
        final String ADMIN = config.getOwnerUsername();

        return ADMIN.toLowerCase().equals(username.toLowerCase()) &&
                (message.toLowerCase().startsWith(TURN_ON_COMMAND) ||
                        message.toLowerCase().startsWith(TURN_OFF_COMMAND));
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    protected ListenerResponse processRequest(String username, String command)
    {
        final String ADMIN = config.getOwnerUsername();
        if (!ADMIN.toLowerCase().equals(username.toLowerCase()))
        {
            return formatSingleResponse(NOT_ADMIN_KEY);
        }

        String gameId = command.substring(TURN_ON_COMMAND.length()).trim().toLowerCase();

        ChatGameListenerService game = chatGames.get(gameId);
        if (game == null)
        {
            return formatSingleResponse(GAME_UNKNOWN_KEY, gameId);
        }


        if (command.startsWith(TURN_ON_COMMAND))
        {
            if (game.isOn())
            {
                return formatSingleResponse(ALREADY_ON_KEY, gameId);
            }

            String startedGameId = getStartedGameId();
            if (startedGameId != null)
            {
                return formatSingleResponse(CANT_TURN_ON_KEY, gameId, startedGameId);
            }

            turnOffAllGames();
            game.turnOn();

            return formatSingleResponse(TURNED_ON_KEY, gameId);
        }

        if (command.startsWith(TURN_OFF_COMMAND))
        {
            if (game.isStarted())
            {
                return formatSingleResponse(CANT_TURN_OFF_KEY, gameId);
            }

            if (!game.isOn())
            {
                return formatSingleResponse(ALREADY_OFF_KEY, gameId);
            }

            game.turnOff();

            return formatSingleResponse(TURNED_OFF_KEY, gameId);
        }
        return nullResponse();
    }

    private String getStartedGameId()
    {
        for (ChatGameListenerService game : chatGames.values())
        {
            if (game.isStarted())
            {
                return game.getGameId();
            }
        }
        return null;
    }

    private void turnOffAllGames()
    {
        for (ChatGameListenerService game : chatGames.values())
        {
            game.turnOff();
        }
    }
}
