package com.schlock.bot.twitch;

import com.schlock.bot.Bot;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.PokemonService;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.HashMap;

public class Commands extends ListenerAdapter
{
    private final PokemonService pokemonService;

    private final DeploymentContext context;

    public Commands(PokemonService pokemonService, DeploymentContext context)
    {
        this.pokemonService = pokemonService;
        this.context = context;
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

        HashMap<String, String> commands = context.getListenerCommands();
        for (String command : commands.keySet())
        {
            if (message.startsWith(command))
            {
                String response = commands.get(command);
                event.getChannel().send().message(response);
                return;
            }
        }
    }
}
