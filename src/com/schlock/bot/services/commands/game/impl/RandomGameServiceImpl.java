package com.schlock.bot.services.commands.game.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.entities.game.PocketGame;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.AbstractListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.game.RandomGameService;
import com.schlock.bot.services.database.adhoc.DatabaseManager;
import com.schlock.bot.services.entities.base.UserManagement;
import com.schlock.bot.services.entities.game.RetroGameManagement;
import com.schlock.bot.services.entities.game.impl.RetroGameManagementImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ioc.Messages;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class RandomGameServiceImpl extends AbstractListenerService implements RandomGameService
{
    protected static final String GAME_COMMAND = "!game ";

    protected static final String SELECTION_RANDOM = "Random";

    protected static final String GAME_LAUNCH_TOKEN_PREFIX = "/media/fat/_@Favorites/_@All/";
    protected static final String GENRE_LAUNCH_TOKEN_PREFIX = "**launch.random:/media/fat/";

    protected static final String COMMAND_NOT_RECOGNIZED_KEY = "command-not-recognized";
    protected static final String PLEASE_WAIT_KEY = "random-game-cooldown";
    protected static final String GENRE_SUCCESS_KEY = "launching-genre-success";
    protected static final String GAME_SUCCESS_KEY = "launching-game-success";
    protected static final String FAILURE_KEY = "launching-game-failure";
    protected static final String TOO_MANY_GAMES_FAILURE_KEY = "too-many-games-failure";

    private final DeploymentConfiguration config;

    private final RetroGameManagement gameManagement;
    private final UserManagement userManagment;

    private final DatabaseManager database;

    public RandomGameServiceImpl(RetroGameManagement gameManagement,
                                 UserManagement userManagemet,
                                 DeploymentConfiguration config,
                                 DatabaseManager database,
                                 Messages messages)
    {
        super(messages);

        this.gameManagement = gameManagement;
        this.userManagment = userManagemet;

        this.config = config;
        this.database = database;
    }

    public boolean isAcceptRequest(String username, String in)
    {
        return in != null &&
                in.toLowerCase().startsWith(GAME_COMMAND);
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    private static final long ONE_MINUTE = 60000;
    private static final long ONE_SECOND = 1000;

    protected ListenerResponse processRequest(String username, String in)
    {
        String command = in.toLowerCase();
        String param = command.substring(GAME_COMMAND.length()).trim();

        User user = userManagment.getUser(username);

        Date date = user.getRandomGameLastExecutionTime();
        if (date != null && !config.getOwnerUsername().equals(username))
        {
            long lastTimeMillis = date.getTime();
            long currentTimeMillis = new Date().getTime();

            long cooldownMillis = config.getRandomGameCooldownMinutes() * ONE_MINUTE;

            if (lastTimeMillis + cooldownMillis > currentTimeMillis)
            {
                String cooldown = Integer.toString(config.getRandomGameCooldownMinutes());

                long waitInSeconds = (cooldownMillis - (currentTimeMillis - lastTimeMillis)) / ONE_SECOND;
                int seconds = (int) (waitInSeconds +1);

                String message = messages.format(PLEASE_WAIT_KEY, cooldown, seconds);
                return ListenerResponse.relaySingle().addMessage(message);
            }
        }


        String launchToken = validateEntry(param);
        if (launchToken == null)
        {
            String message = messages.format(COMMAND_NOT_RECOGNIZED_KEY, param);
            return ListenerResponse.relaySingle().addMessage(message);
        }

        if (StringUtils.equalsIgnoreCase(TOO_MANY_GAMES_FAILURE_KEY, launchToken))
        {
            String message = messages.get(TOO_MANY_GAMES_FAILURE_KEY);
            return ListenerResponse.relaySingle().addMessage(message);
        }


        boolean success = launch(launchToken);
        if (!success)
        {
            String message = messages.get(FAILURE_KEY);
            return ListenerResponse.relaySingle().addMessage(message);
        }

        user.setRandomGameLastExecutionTime(new Date());
        database.save(user);


        if (StringUtils.equalsIgnoreCase(SELECTION_RANDOM, param))
        {
            String message = messages.format(GENRE_SUCCESS_KEY, SELECTION_RANDOM);
            return ListenerResponse.relaySingle().addMessage(message);
        }

        String token = launchToken.substring(launchToken.lastIndexOf("/") + 2);

        if(StringUtils.startsWith(launchToken, GENRE_LAUNCH_TOKEN_PREFIX))
        {
            String message = messages.format(GENRE_SUCCESS_KEY, token);
            return ListenerResponse.relaySingle().addMessage(message);
        }

        token = token.substring(0, token.lastIndexOf(".mgl"));

        String message = messages.format(GAME_SUCCESS_KEY, token);
        return ListenerResponse.relaySingle().addMessage(message);
    }

    private String validateEntry(String param)
    {
        if (StringUtils.equalsIgnoreCase(SELECTION_RANDOM, param))
        {
            PocketGame game = gameManagement.getRandom();
            return getLaunchToken(game);
        }

        String genre = gameManagement.getGenre(param);
        if (genre != null)
        {
            return getLaunchToken(genre);
        }

        List<PocketGame> games = gameManagement.getGames(param);
        if (games.size() == 1)
        {
            return getLaunchToken(games.get(0));
        }
        if (games.size() > 1)
        {
            return TOO_MANY_GAMES_FAILURE_KEY;
        }
        return null;
    }

    public String getLaunchToken(PocketGame game)
    {
        String launchToken = GAME_LAUNCH_TOKEN_PREFIX + "_%s/%s (%s).mgl";

        String genre = game.getGenre();
        String title = game.getGameName();
        String coreName = game.getCoreName();

        return String.format(launchToken, genre, title, coreName);
    }

    public String getLaunchToken(String genre)
    {
        String launchToken = GENRE_LAUNCH_TOKEN_PREFIX;
        if (StringUtils.equalsIgnoreCase(RetroGameManagementImpl.ARCADE, genre))
        {
            launchToken += "_Arcade";
        }
        else
        {
            launchToken += "_@Favorites/_@All/_" + genre;
        }
        return launchToken;
    }


    private boolean launch(String launchToken)
    {
        String urlCall = getMisterUrlCall(launchToken);

        try
        {
            URL request = new URL(urlCall);

            HttpURLConnection connection = (HttpURLConnection) request.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setConnectTimeout(5000);

            int code = connection.getResponseCode();
            if (code != 200)
            {
                System.out.println("Response code: " + code);
                return false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String getMisterUrlCall(String token)
    {
        final String API_CALL = "http://%s:7497/api/v1/launch/%s";

        String ip = config.getMisterIp();
        String launchToken = URLEncoder.encode(token, StandardCharsets.UTF_8);

        return String.format(API_CALL, ip, launchToken);
    }
}
