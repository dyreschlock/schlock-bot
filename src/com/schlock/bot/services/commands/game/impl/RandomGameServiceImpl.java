package com.schlock.bot.services.commands.game.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.AbstractListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.game.RandomGameService;
import com.schlock.bot.services.database.adhoc.DatabaseManager;
import com.schlock.bot.services.entities.base.UserManagement;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.tapestry5.ioc.Messages;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RandomGameServiceImpl extends AbstractListenerService implements RandomGameService
{
    protected static final String GAME_COMMAND = "!game ";

    protected static final String SELECTION_RANDOM = "random";
    protected static final String SELECTION_FIGHTER = "Fighter";
    protected static final String SELECTION_SHOOTER = "Shooter";
    protected static final String SELECTION_PUZZLE = "Puzzle";
    protected static final String SELECTION_ARCADE = "arcade";

    protected static final List<String> PARAMS = Stream.of(SELECTION_RANDOM,
                                                            SELECTION_FIGHTER.toLowerCase(),
                                                            SELECTION_SHOOTER.toLowerCase(),
                                                            SELECTION_PUZZLE.toLowerCase(),
                                                            SELECTION_ARCADE).collect(Collectors.toList());

    protected static final String COMMAND_NOT_RECOGNIZED_KEY = "command-not-recognized";
    protected static final String PLEASE_WAIT_KEY = "random-game-cooldown";
    protected static final String SUCCESS_KEY = "launching-game-success";
    protected static final String FAILURE_KEY = "launching-game-failure";

    private final DeploymentConfiguration config;
    private final UserManagement userManagment;

    private final DatabaseManager database;

    public RandomGameServiceImpl(UserManagement userManagemet,
                                 DeploymentConfiguration config,
                                 DatabaseManager database,
                                 Messages messages)
    {
        super(messages);

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

        if (!PARAMS.contains(param))
        {
            String message = messages.format(COMMAND_NOT_RECOGNIZED_KEY, param);
            return ListenerResponse.relaySingle().addMessage(message);
        }

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

        boolean success = launchGame(param);
        if (!success)
        {
            String message = messages.get(FAILURE_KEY);
            return ListenerResponse.relaySingle().addMessage(message);
        }

        user.setRandomGameLastExecutionTime(new Date());
        database.save(user);

        String message = messages.format(SUCCESS_KEY, param);
        return ListenerResponse.relaySingle().addMessage(message);
    }

    private boolean launchGame(String param)
    {
        String urlCall = getMisterUrlCall(param);

        try
        {
            URL request = new URL(urlCall);

            HttpURLConnection connection = (HttpURLConnection) request.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

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

    private String getMisterUrlCall(String param)
    {
        final String API_CALL = "http://%s:7497/api/v1/launch/%s";
        String ip = config.getMisterIp();

        if (SELECTION_RANDOM.equals(param))
        {
            int random = new Random().nextInt(PARAMS.size() - 2) +1;
            return getMisterUrlCall(PARAMS.get(random));
        }

        String launchToken = "**launch.random:/media/fat/";
        if (SELECTION_ARCADE.equals(param))
        {
            launchToken += "_Arcade";
        }
        else
        {
            String s1 = param.substring(0, 1).toUpperCase();
            String category = s1 + param.substring(1);

            launchToken += "_@Favorites/_TapTo/_" + category;
        }

        launchToken = URLEncoder.encode(launchToken, StandardCharsets.UTF_8);

        return String.format(API_CALL, ip, launchToken);
    }
}
