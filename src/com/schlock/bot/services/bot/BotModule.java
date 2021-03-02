package com.schlock.bot.services.bot;

import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.bot.apps.ListenerService;
import com.schlock.bot.services.bot.apps.pokemon.ShinyDexEntryService;
import com.schlock.bot.services.bot.apps.UserService;
import com.schlock.bot.services.bot.apps.bet.ShinyBetService;
import com.schlock.bot.services.bot.apps.bet.ShinyPayoutService;
import com.schlock.bot.services.bot.apps.bet.impl.ShinyBetServiceImpl;
import com.schlock.bot.services.bot.apps.bet.impl.ShinyPayoutServiceImpl;
import com.schlock.bot.services.bot.apps.guess.GuessingService;
import com.schlock.bot.services.bot.apps.guess.impl.GuessingServiceImpl;
import com.schlock.bot.services.bot.apps.pokemon.impl.ShinyDexEntryServiceImpl;
import com.schlock.bot.services.bot.apps.impl.UserServiceImpl;
import com.schlock.bot.services.bot.apps.pokemon.PokemonService;
import com.schlock.bot.services.bot.apps.pokemon.PokemonUtils;
import com.schlock.bot.services.bot.apps.pokemon.ShinyInfoService;
import com.schlock.bot.services.bot.apps.pokemon.impl.PokemonServiceImpl;
import com.schlock.bot.services.bot.apps.pokemon.impl.PokemonUtilsImpl;
import com.schlock.bot.services.bot.apps.pokemon.impl.ShinyInfoServiceImpl;
import com.schlock.bot.services.bot.discord.DiscordBot;
import com.schlock.bot.services.bot.discord.impl.DiscordBotImpl;
import com.schlock.bot.services.bot.twitch.TwitchBot;
import com.schlock.bot.services.bot.twitch.impl.TwitchBotImpl;
import com.schlock.bot.services.database.apps.UserDAO;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.EagerLoad;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BotModule
{
    public static void bind(ServiceBinder binder)
    {
        binder.bind(ShinyDexEntryService.class, ShinyDexEntryServiceImpl.class);

        binder.bind(PokemonUtils.class, PokemonUtilsImpl.class);

        //app listeners
        binder.bind(UserService.class, UserServiceImpl.class);

        binder.bind(ShinyBetService.class, ShinyBetServiceImpl.class);
        binder.bind(ShinyPayoutService.class, ShinyPayoutServiceImpl.class);

        binder.bind(GuessingService.class, GuessingServiceImpl.class);

        binder.bind(PokemonService.class, PokemonServiceImpl.class);
        binder.bind(ShinyInfoService.class, ShinyInfoServiceImpl.class);
    }

    @EagerLoad
    public static TwitchBot build(UserService userService,
                                  ShinyBetService shinyBetService,
                                  ShinyPayoutService shinyPayoutService,
                                  GuessingService guessingService,
                                  PokemonService pokemonService,
                                  ShinyInfoService shinyInfoService,
                                  DeploymentConfiguration config)
    {
        Set<ListenerService> listeners =
                                    Stream.of(userService,
                                                shinyBetService,
                                                shinyPayoutService,
                                                guessingService,
                                                pokemonService,
                                                shinyInfoService).collect(Collectors.toSet());

        TwitchBot bot = new TwitchBotImpl(listeners, config);

        Thread twitchBotThread = new Thread()
        {
            public void run()
            {
                try
                {
                    bot.startup();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        twitchBotThread.start();

        return bot;
    }

    public static DiscordBot build(UserService userService,
                                   ShinyBetService shinyBetService,
                                   ShinyPayoutService shinyPayoutService,
                                   GuessingService guessingService,
                                   PokemonService pokemonService,
                                   ShinyInfoService shinyInfoService,
                                   UserDAO userDAO,
                                   DeploymentConfiguration config)
    {
        Set<ListenerService> listeners =
                                    Stream.of(userService,
                                            shinyBetService,
                                            shinyPayoutService,
                                            guessingService,
                                            pokemonService,
                                            shinyInfoService).collect(Collectors.toSet());

        DiscordBot bot = new DiscordBotImpl(listeners, config);

        Thread discordBotThread = new Thread()
        {
            public void run()
            {
                try
                {
                    bot.startup();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        discordBotThread.start();

        return bot;
    }
}
