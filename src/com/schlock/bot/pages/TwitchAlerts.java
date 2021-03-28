package com.schlock.bot.pages;

import com.schlock.bot.services.database.adhoc.DatabaseManager;
import com.schlock.bot.services.database.base.UserDAO;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

public class TwitchAlerts
{
    @Inject
    private DatabaseManager database;

    @Inject
    private Messages messages;

    public String getTwitchAlertHTML()
    {
        String mostRecentUsername = database.get(UserDAO.class).getMostRecentUser().getUsername();

        String message = messages.format("new-follower", mostRecentUsername);
        message = message.toUpperCase();

        String html = "";
        for(int i = 0; i < message.length(); i++)
        {
            html += "<span>";
            html += message.charAt(i);
            html += "</span>";
        }
        return html;
    }
}
