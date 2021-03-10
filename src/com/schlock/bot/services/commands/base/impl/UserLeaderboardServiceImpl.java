package com.schlock.bot.services.commands.base.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.AbstractListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.base.UserLeaderboardService;
import com.schlock.bot.services.database.base.UserDAO;
import org.apache.tapestry5.ioc.Messages;

import java.util.List;

public class UserLeaderboardServiceImpl extends AbstractListenerService implements UserLeaderboardService
{
    private static final Integer RETURN_COUNT = 10;

    protected static final String USER_LEADERBOARD_KEY = "user-leaderboard";

    protected static final String LEADERBOARD = "!leaderboard";

    private final UserDAO userDAO;

    private final DeploymentConfiguration config;

    public UserLeaderboardServiceImpl(UserDAO userDAO,
                                      Messages messages,
                                      DeploymentConfiguration config)
    {
        super(messages);

        this.userDAO = userDAO;
        this.config = config;
    }

    public boolean isAcceptRequest(String username, String message)
    {
        return message != null &&
                message.toLowerCase().startsWith(LEADERBOARD);
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    public ListenerResponse process(String username, String command)
    {
        ListenerResponse response = ListenerResponse.relaySingle();

        List<User> users = userDAO.getOrderByPoints(RETURN_COUNT);
        for(int i = 0; i < users.size(); i++)
        {
            int number = i+ 1;
            User user = users.get(i);

            String message = messages.format(USER_LEADERBOARD_KEY,
                                                number,
                                                user.getUsername(),
                                                user.getBalance(),
                                                config.getCurrencyMark());

            response.addMessage(message);
        }
        return response;
    }
}
