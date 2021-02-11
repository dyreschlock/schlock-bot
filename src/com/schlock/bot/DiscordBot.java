package com.schlock.bot;

import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.PokemonService;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.HashMap;

public class DiscordBot extends AbstractBot
{
    public DiscordBot(PokemonService pokemonService, DeploymentContext context)
    {
        super(pokemonService, context);
    }

    public void startup()
    {
        final String TOKEN = getContext().getDiscordToken();

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

        listenForPokemon(client);

        listenForCommands(client);

        client.onDisconnect().block();
    }


    public void listenForPokemon(GatewayDiscordClient client)
    {
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> getPokemonService().isPokemonCommand(message.getContent()))
                .subscribe(message -> {

                    String content = message.getContent();
                    String response = getPokemonService().process(content);

                    final MessageChannel channel = message.getChannel().block();
                    channel.createMessage(response).block();
                });
    }


    public void listenForCommands(GatewayDiscordClient client)
    {
        HashMap<String, String> commands = getContext().getListenerCommands();

        for (String command : commands.keySet())
        {
            String responseMessage = commands.get(command);

            client.getEventDispatcher().on(MessageCreateEvent.class)
                    .map(MessageCreateEvent::getMessage)
                    .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                    .filter(message -> message.getContent().equalsIgnoreCase(command))
                    .flatMap(Message::getChannel)
                    .flatMap(channel -> channel.createMessage(responseMessage))
                    .subscribe();
        }
    }

    public void listenForPing(GatewayDiscordClient client)
    {
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().equalsIgnoreCase(PING))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(PONG))
                .subscribe();
    }
}
