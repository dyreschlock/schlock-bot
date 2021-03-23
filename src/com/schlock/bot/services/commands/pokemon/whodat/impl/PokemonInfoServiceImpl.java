package com.schlock.bot.services.commands.pokemon.whodat.impl;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.services.commands.AbstractListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.pokemon.whodat.PokemonInfoService;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.PokemonUtils;
import org.apache.tapestry5.ioc.Messages;

public class PokemonInfoServiceImpl extends AbstractListenerService implements PokemonInfoService
{
    private static final String POKEMON_COMMAND = "!pokemon";
    private static final String POKEMON_E_COMMAND = "!pokémon";

    private static final String ARGUMENT_TYPE = "type";
    private static final String ARGUMENT_STATS = "basestats";

    private static final String RANDOM = "random";

    private final PokemonUtils pokemonUtils;
    private final PokemonManagement pokemonManagement;

    public PokemonInfoServiceImpl(PokemonUtils pokemonUtils,
                                   PokemonManagement pokemonManagement,
                                   Messages messages)
    {
        super(messages);

        this.pokemonUtils = pokemonUtils;
        this.pokemonManagement = pokemonManagement;
    }

    public boolean isAcceptRequest(String username, String in)
    {
        return in != null &&
                (in.toLowerCase().startsWith(POKEMON_COMMAND) ||
                        in.toLowerCase().startsWith(POKEMON_E_COMMAND));
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    /**
     * Always returns a single pokemon, or nothing if bad syntax
     *
     * @return String "No. 25 Pikachu" (example)
     */
    protected ListenerResponse processRequest(String username, String in)
    {
        String commandText = in.toLowerCase();
        if (commandText.startsWith(POKEMON_COMMAND) || commandText.startsWith(POKEMON_E_COMMAND))
        {
            boolean hasTypeArg = hasArg(ARGUMENT_TYPE, commandText);
            if (hasTypeArg)
            {
                commandText = removeArg(ARGUMENT_TYPE, commandText);
            }

            boolean hasStatsArg = hasArg(ARGUMENT_STATS, commandText);
            if (hasStatsArg)
            {
                commandText = removeArg(ARGUMENT_STATS, commandText);
            }

            Pokemon pokemon = getPokemonFromParams(commandText);
            if (pokemon != null)
            {
                String response = pokemonUtils.formatOutput(pokemon, hasTypeArg, hasStatsArg);
                return ListenerResponse.relaySingle().addMessage(response);
            }
        }
        return nullResponse();
    }

    private boolean hasArg(String arg, String in)
    {
        return in.toLowerCase().contains(" " + arg + " ") ||
                in.toLowerCase().endsWith(" " + arg);
    }

    private String removeArg(String arg, String in)
    {
        String target = " " + arg;
        if (in.toLowerCase().endsWith(target))
        {
            int endpoint = in.length() - target.length();
            return in.substring(0, endpoint);
        }

        return in.replace(target, "");
    }

    private Pokemon getPokemonFromParams(String params)
    {
        String commandText = params.substring(POKEMON_COMMAND.length());

        if (pokemonManagement.isRangeSearch(commandText))
        {
            return pokemonManagement.getRandomPokemonInRange(commandText);
        }

        if (pokemonManagement.isGenSearch(commandText))
        {
            return pokemonManagement.getRandomPokemonInGen(commandText);
        }

        if (commandText.contains(RANDOM))
        {
            return pokemonManagement.getRandomPokemon();
        }

        return pokemonManagement.getPokemonFromText(commandText);
    }
}
