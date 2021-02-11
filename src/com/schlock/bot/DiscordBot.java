package com.schlock.bot;

import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.ListenerService;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;

import java.util.HashMap;
import java.util.Set;


public class DiscordBot extends AbstractBot
{
    public DiscordBot(Set<ListenerService> listeners,
                      DeploymentContext context)
    {
        super(listeners, context);
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

        initializeListeners(client);

        listenForCommands(client);

        client.onDisconnect().block();
    }


    public void initializeListeners(GatewayDiscordClient client)
    {
        for (ListenerService service : getListeners())
        {
            client.getEventDispatcher().on(MessageCreateEvent.class)
                    .map(MessageCreateEvent::getMessage)
                    .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                    .filter(message -> service.isCommand(message.getContent()))
                    .subscribe(message -> {

                        String username = message.getAuthor().toString();
                        String content = message.getContent();

                        String response = service.process(username, content);

                        final MessageChannel channel = message.getChannel().block();
                        channel.createMessage(response).block();
                    });
        }
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
