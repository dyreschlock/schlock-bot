package com.schlock.bot.pages.pokemon;

import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.pokemon.bet.ShinyBetInfoService;
import com.schlock.bot.services.commands.pokemon.bet.impl.ShinyBetInfoServiceImpl;

import javax.inject.Inject;

public class CurrentBets
{
    @Inject
    private ShinyBetInfoService betInfoService;

    @Inject
    private DeploymentConfiguration config;

    public String getBetsHTML()
    {
        final String COMMAND = ShinyBetInfoServiceImpl.ALL_CURRENT_BETS;

        ListenerResponse response = betInfoService.process(config.getOwnerUsername(), COMMAND);

        String html = "";
        for (String message : response.getMessages())
        {
            html += "<span>" + message + "&nbsp;</span><br />";
        }
        return html;
    }
}
