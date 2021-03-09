package com.schlock.bot.services.entities.pokemon.impl;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.bet.ShinyBet;
import com.schlock.bot.entities.apps.pokemon.Pokemon;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.ShinyBetFormatter;
import org.apache.tapestry5.ioc.Messages;

import java.util.List;

public class ShinyBetFormatterImpl implements ShinyBetFormatter
{
    protected static final String CURRENT_BET_KEY = "current-bet";

    private final PokemonManagement pokemonManagement;
    private final Messages messages;
    private final DeploymentConfiguration config;

    public ShinyBetFormatterImpl(PokemonManagement pokemonManagement,
                                 Messages messages,
                                 DeploymentConfiguration config)
    {
        this.pokemonManagement = pokemonManagement;
        this.messages = messages;
        this.config = config;
    }

    public ListenerResponse formatAllBets(ListenerResponse responses, List<ShinyBet> bets)
    {
        for(ShinyBet bet : bets)
        {
            Pokemon pokemon = pokemonManagement.getPokemonFromText(bet.getPokemonId());
            User user = bet.getUser();

            String response = messages.format(CURRENT_BET_KEY,
                    user.getUsername(),
                    pokemon.getName(),
                    bet.getTimeMinutes().toString(),
                    bet.getBetAmount().toString(),
                    config.getCurrencyMark());

            responses.addMessage(response);
        }
        return responses;
    }
}
