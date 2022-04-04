package com.schlock.bot.services.commands.pokemon.bet.impl;

import com.schlock.bot.entities.pokemon.ShinyBetLetsGo;
import com.schlock.bot.services.commands.AbstractListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.pokemon.bet.ShinyBetInfoService;
import com.schlock.bot.services.database.adhoc.DatabaseManager;
import com.schlock.bot.services.database.pokemon.ShinyBetLetsGoDAO;
import com.schlock.bot.services.entities.pokemon.ShinyBetFormatter;
import org.apache.tapestry5.ioc.Messages;

import java.util.List;

public class ShinyBetInfoServiceImpl extends AbstractListenerService implements ShinyBetInfoService
{
    protected static final String NO_CURRENT_BETS_KEY = "no-current-bets";

    public static final String ALL_CURRENT_BETS = "!allcurrentbets";


    private final ShinyBetFormatter shinyBetFormatter;
    private final DatabaseManager database;

    public ShinyBetInfoServiceImpl(ShinyBetFormatter shinyBetFormatter,
                                   DatabaseManager database,
                                   Messages messages)
    {
        super(messages);

        this.shinyBetFormatter = shinyBetFormatter;
        this.database = database;
    }


    public boolean isAcceptRequest(String username, String message)
    {
        return message != null &&
                message.toLowerCase().startsWith(ALL_CURRENT_BETS);
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    protected ListenerResponse processRequest(String username, String command)
    {
        List<ShinyBetLetsGo> currentBets = database.get(ShinyBetLetsGoDAO.class).getAllCurrent();
        if (currentBets.size() == 0)
        {
            return formatSingleResponse(NO_CURRENT_BETS_KEY);
        }

        ListenerResponse response = ListenerResponse.relaySingle();
        return shinyBetFormatter.formatAllBetsLetsGo(response, currentBets);
    }
}
