package com.schlock.bot.services.commands.pokemon.bet.impl;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.entities.pokemon.ShinyGetType;
import com.schlock.bot.entities.pokemon.ShinyHisuiGet;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.AbstractListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.pokemon.bet.ShinyHisuiService;
import com.schlock.bot.services.database.adhoc.DatabaseManager;
import com.schlock.bot.services.database.pokemon.ShinyHisuiGetDAO;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.ShinyGetFormatter;
import org.apache.tapestry5.ioc.Messages;

public class ShinyHisuiServiceImpl extends AbstractListenerService implements ShinyHisuiService
{
    protected static final String BAD_FORMAT_MESSAGE_KEY = "hisui-wrong-format";

    private static final String SHINY_GET_COMMAND = "!hisuiget ";

    private final PokemonManagement pokemonManagement;
    private final ShinyGetFormatter shinyFormatter;

    private final DatabaseManager database;
    private final DeploymentConfiguration config;

    public ShinyHisuiServiceImpl(PokemonManagement pokemonManagement,
                                 ShinyGetFormatter shinyFormatter,
                                 DatabaseManager database,
                                 Messages messages,
                                 DeploymentConfiguration config)
    {
        super(messages);

        this.pokemonManagement = pokemonManagement;
        this.shinyFormatter = shinyFormatter;

        this.database = database;
        this.config = config;
    }

    public boolean isAcceptRequest(String username, String in)
    {
        String admin = config.getOwnerUsername();

        return username.equals(admin) &&
                in != null &&
                in.toLowerCase().startsWith(SHINY_GET_COMMAND);
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    protected ListenerResponse processRequest(String username, String in)
    {
        if (in.toLowerCase().trim().startsWith(SHINY_GET_COMMAND))
        {
            String params = in.substring(SHINY_GET_COMMAND.length()).trim();

            ShinyHisuiGet get = createGetFromParams(params);
            if (get == null)
            {
                return formatSingleResponse(BAD_FORMAT_MESSAGE_KEY);
            }

            database.save(get);

            ListenerResponse response = ListenerResponse.relayAll();
            response.addMessage(shinyFormatter.formatNewlyCaughtHisui(get));

            return response;
        }
        return nullResponse();
    }

    protected ShinyHisuiGet createGetFromParams(String params)
    {
        //[type] [pokemon] [resets]
        String[] p = params.split(" ");
        try
        {
            Pokemon pokemon = pokemonManagement.getPokemonFromText(p[1]);
            if (pokemon == null)
            {
                return null;
            }

            ShinyGetType type = ShinyGetType.valueOf(p[0].toUpperCase());
            if (type == null)
            {
                return null;
            }

            Integer resets = null;
            if (ShinyGetType.OUTBREAK.equals(type))
            {
                resets = Integer.parseInt(p[2]);
                if (resets == null)
                {
                    return null;
                }
            }

            Integer shinyNumber = database.get(ShinyHisuiGetDAO.class).getCurrentShinyNumber();

            ShinyHisuiGet get = new ShinyHisuiGet();
            get.setMethod(type);
            get.setPokemonId(pokemon.getId());
            get.setResets(resets);
            get.setShinyNumber(shinyNumber);

            return get;
        }
        catch(Exception e)
        {
        }
        return null;
    }
}
