package com.schlock.bot.services.entities.pokemon.impl;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.entities.pokemon.ShinyBetHisui;
import com.schlock.bot.entities.pokemon.ShinyBetLetsGo;
import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.ShinyBetFormatter;
import org.apache.tapestry5.ioc.Messages;

import java.util.List;

public class ShinyBetFormatterImpl implements ShinyBetFormatter
{
    public static final String CURRENT_BET_LETSGO_KEY = "current-bet-letsgo";
    public static final String CURRENT_BET_HISUI_KEY = "current-bet-hisui";
    public static final String BET_HISUI_POKEMON_KEY = "bet-hisui-pokemon";

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

    public ListenerResponse formatAllBetsLetsGo(ListenerResponse responses, List<ShinyBetLetsGo> bets)
    {
        for(ShinyBetLetsGo bet : bets)
        {
            Pokemon pokemon = pokemonManagement.getPokemonFromText(bet.getPokemonId());
            User user = bet.getUser();

            String response = messages.format(CURRENT_BET_LETSGO_KEY,
                    user.getUsername(),
                    pokemon.getName(),
                    bet.getTimeMinutes().toString(),
                    bet.getBetAmount().toString(),
                    config.getCurrencyMark());

            responses.addMessage(response);
        }
        return responses;
    }

    public ListenerResponse formatAllBetsHisui(ListenerResponse responses, List<ShinyBetHisui> bets)
    {
        for(ShinyBetHisui bet : bets)
        {
            User user = bet.getUser();

            String response = messages.format(CURRENT_BET_HISUI_KEY,
                    user.getUsername(),
                    bet.getBetAmount(),
                    config.getCurrencyMark(),
                    bet.getNumberOfChecks());

            if (bet.getPokemonId() != null)
            {
                Pokemon pokemon = pokemonManagement.getPokemonFromText(bet.getPokemonId());

                response += " " + messages.format(BET_HISUI_POKEMON_KEY, pokemon.getName());
            }

            responses.addMessage(response);
        }
        return responses;
    }
}
