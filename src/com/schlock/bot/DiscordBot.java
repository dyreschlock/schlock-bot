package com.schlock.bot;

import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.PokemonService;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;

import java.util.Properties;

public class DiscordBot
{
    private final static String POKEMON_COMMAND = "!pokemon";

    private final PokemonService pokemonService;
    private final DeploymentContext context;


    public DiscordBot(PokemonService pokemonService, DeploymentContext context)
    {
        this.pokemonService = pokemonService;
        this.context = context;
    }

    public void startup()
    {
        final String TOKEN = context.getDiscordToken();

        GatewayDiscordClient client = DiscordClientBuilder.create(TOKEN)
                .build()
                .login()
                .block();

        client.getEventDispatcher().on(ReadyEvent.class)
                .subscribe(event -> {
                    final User self = event.getSelf();
                    System.out.println(String.format(
                            "Logged in as %s#%s", self.getUsername(), self.getDiscriminator()
                    ));
                });

        listenForPing(client);
//        listenForPong(client);

        listenForPokemon(client);

        client.onDisconnect().block();
    }


    public void listenForPokemon(GatewayDiscordClient client)
    {
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().toLowerCase().startsWith(POKEMON_COMMAND))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Pong!"))
                .subscribe();
    }


    public void listenForPing(GatewayDiscordClient client)
    {
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase("!ping"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Pong!"))
                .subscribe();
    }

    public void listenForPong(GatewayDiscordClient client)
    {
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase("!pong"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Ping!"))
                .subscribe();
    }
}
