package com.schlock.bot.services.apps.pokemon.impl;

import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.entities.apps.pokemon.ShinyGet;
import com.schlock.bot.entities.apps.pokemon.ShinyGetUtils;
import com.schlock.bot.services.DatabaseModule;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.apps.pokemon.PokemonService;
import com.schlock.bot.services.apps.pokemon.ShinyInfoService;
import com.schlock.bot.services.database.apps.ShinyGetDAO;

public class ShinyInfoServiceImpl implements ShinyInfoService
{
    private static final String MOST_RECENT_COMMAND = "!recent";
    private static final String MOST_RECENT_COMMAND2 = "!last";

    private static final String SHINY_GET_COMMAND = "!shinyget ";

    private final DeploymentContext context;
    private final DatabaseModule database;

    private final PokemonService pokemonService;

    public ShinyInfoServiceImpl(PokemonService pokemonService,
                                DatabaseModule database,
                                DeploymentContext context)
    {
        this.pokemonService = pokemonService;
        this.database = database;
        this.context = context;
    }

    public boolean isAcceptRequest(String in)
    {
        return in != null &&
                (in.toLowerCase().startsWith(MOST_RECENT_COMMAND) ||
                        in.toLowerCase().startsWith(MOST_RECENT_COMMAND2) ||
                        in.toLowerCase().startsWith(SHINY_GET_COMMAND));
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    public String process(String username, String in)
    {
        String commandText = in.toLowerCase().trim();
        if (commandText.startsWith(MOST_RECENT_COMMAND) || commandText.startsWith(MOST_RECENT_COMMAND2))
        {
            ShinyGet mostRecent = database.get(ShinyGetDAO.class).getMostRecent();
            Pokemon pokemon = pokemonService.getPokemonFromText(mostRecent.getPokemonId());

            return ShinyGetUtils.format(mostRecent, pokemon);
        }

        return NULL_RESPONSE;
    }
}

