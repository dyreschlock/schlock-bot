package com.schlock.bot.twitch;

import com.schlock.bot.Bot;
import com.schlock.bot.services.PokemonService;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

public class Commands extends ListenerAdapter
{
    private final PokemonService pokemonService;

    public Commands(PokemonService pokemonService)
    {
        this.pokemonService = pokemonService;
    }

    public void onMessage(MessageEvent event) throws Exception
    {
        String message = event.getMessage().toLowerCase();

        if (message.startsWith(Bot.PING))
        {
            event.getChannel().send().message(Bot.PONG);
            return;
        }

        if (message.startsWith(PokemonService.POKEMON_COMMAND))
        {
            String response = pokemonService.process(message);
            event.getChannel().send().message(response);
            return;
        }
    }
}
