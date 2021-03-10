package com.schlock.bot.pages.base;

import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.base.UserLeaderboardService;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Leaderboard
{
    private static final Integer RESULTS_COUNT = 10;

    @Inject
    private UserLeaderboardService leaderboardService;

    @Inject
    private DeploymentConfiguration config;

    public String getUsersHTML()
    {
        ListenerResponse response = leaderboardService.process(config.getOwnerUsername(), "!leaderboard");

        String html = "";
        for (String message : response.getMessages())
        {
            html += "<span>" + message + "</span><br />";
        }
        return html;
    }
}
