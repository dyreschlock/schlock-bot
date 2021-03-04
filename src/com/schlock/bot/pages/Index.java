package com.schlock.bot.pages;

import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.bot.apps.ListenerResponse;
import com.schlock.bot.services.bot.apps.UserService;
import com.schlock.bot.services.bot.apps.bet.ShinyBetService;
import com.schlock.bot.services.database.apps.ShinyGetDAO;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class Index
{
    @Inject
    private ShinyGetDAO shinyGetDAO;

    @Inject
    private ShinyBetService shinyBetService;

    @Inject
    private UserService userService;

    @Inject
    private DeploymentConfiguration configuration;

    public String getShinyNumber()
    {
        return shinyGetDAO.getCurrentShinyNumber().toString();
    }

    public String getDate()
    {
        return new Date().toString();
    }

    public String getOpenBets()
    {
        String command = "!openbets";
        String owner = configuration.getOwnerUsername();

        ListenerResponse response = shinyBetService.process(owner, command);

        return response.getFirstMessage();
    }

    @CommitAfter
    public String getBalance()
    {
        String command = "!balance";
        String username = "asdfasdf";

        ListenerResponse response = userService.process(username, command);

        return response.getFirstMessage();
    }

}
