package com.schlock.bot.services.commands.pokemon.shiny.impl;

import com.schlock.bot.entities.TimeUtils;
import com.schlock.bot.entities.pokemon.ShinyGet;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.AbstractListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.pokemon.shiny.ShinyInfoService;
import com.schlock.bot.services.database.pokemon.ShinyGetDAO;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.ShinyGetFormatter;
import org.apache.tapestry5.ioc.Messages;

import java.text.DecimalFormat;

public class ShinyInfoServiceImpl extends AbstractListenerService implements ShinyInfoService
{
    protected static final String AVERAGE_TIME_KEY = "shiny-average-time";
    protected static final String AVERAGE_CHECKS_KEY = "shiny-average-checks";

    private static final String MOST_RECENT_COMMAND = "!recent";
    private static final String MOST_RECENT_COMMAND2 = "!last";

    public static final String AVERAGE_COMMAND = "!shinyaverage";
    private static final String AVERAGE_CHECKS_COMMAND = "!shinychecks";

    private final PokemonManagement pokemonManagement;
    private final ShinyGetFormatter shinyFormatter;

    private final ShinyGetDAO shinyGetDAO;

    private final DeploymentConfiguration config;

    public ShinyInfoServiceImpl(PokemonManagement pokemonManagement,
                                ShinyGetFormatter shinyFormatter,
                                ShinyGetDAO shinyGetDAO,
                                Messages messages,
                                DeploymentConfiguration config)
    {
        super(messages);

        this.pokemonManagement = pokemonManagement;
        this.shinyFormatter = shinyFormatter;
        this.shinyGetDAO = shinyGetDAO;
        this.config = config;
    }

    public boolean isAcceptRequest(String username, String in)
    {
        return in != null &&
                (in.toLowerCase().startsWith(MOST_RECENT_COMMAND) ||
                        in.toLowerCase().startsWith(MOST_RECENT_COMMAND2) ||
                        in.toLowerCase().startsWith(AVERAGE_COMMAND) ||
                        in.toLowerCase().startsWith(AVERAGE_CHECKS_COMMAND));
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    public ListenerResponse process(String username, String in)
    {
        String commandText = in.toLowerCase().trim();
        if (commandText.startsWith(MOST_RECENT_COMMAND) || commandText.startsWith(MOST_RECENT_COMMAND2))
        {
            ShinyGet mostRecent = shinyGetDAO.getMostRecent();

            String response = shinyFormatter.formatMostRecent(mostRecent);
            return ListenerResponse.relaySingle().addMessage(response);
        }

        if (commandText.startsWith(AVERAGE_COMMAND))
        {
            Double averageTime = shinyGetDAO.getCurrentAverageTimeToShiny();

            return formatSingleResponse(AVERAGE_TIME_KEY, TimeUtils.formatDoubleMinutesIntoTimeString(averageTime));
        }

        if (commandText.startsWith(AVERAGE_CHECKS_COMMAND))
        {
            Double averageChecks = shinyGetDAO.getCurrentAverageNumberOfRareChecks();

            return formatSingleResponse(AVERAGE_CHECKS_KEY, new DecimalFormat("#0.00").format(averageChecks));
        }

        return nullResponse();
    }
}

