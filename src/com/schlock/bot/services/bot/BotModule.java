package com.schlock.bot.services.bot;

import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.bot.discord.DiscordBot;
import com.schlock.bot.services.bot.discord.impl.DiscordBotImpl;
import com.schlock.bot.services.bot.twitch.TwitchBot;
import com.schlock.bot.services.bot.twitch.impl.TwitchBotImpl;
import com.schlock.bot.services.commands.ListenerService;
import com.schlock.bot.services.commands.base.UserLeaderboardService;
import com.schlock.bot.services.commands.pokemon.bet.ShinyBetInfoService;
import com.schlock.bot.services.commands.pokemon.shiny.ShinyDexService;
import com.schlock.bot.services.commands.pokemon.shiny.ShinyInfoService;
import com.schlock.bot.services.commands.base.AnimationService;
import com.schlock.bot.services.commands.base.UserPointsService;
import com.schlock.bot.services.commands.pokemon.bet.ShinyBetService;
import com.schlock.bot.services.commands.pokemon.bet.ShinyPayoutService;
import com.schlock.bot.services.commands.pokemon.whodat.PokemonGuessingService;
import com.schlock.bot.services.commands.pokemon.whodat.PokemonInfoService;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.EagerLoad;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BotModule
{
    public static void bind(ServiceBinder binder)
    {
    }

    @EagerLoad
    public static TwitchBot build(UserPointsService userPointsService,
                                  ShinyBetService shinyBetService,
                                  ShinyPayoutService shinyPayoutService,
                                  PokemonGuessingService guessingService,
                                  PokemonInfoService pokemonInfoService,
                                  ShinyInfoService shinyInfoService,
                                  ShinyDexService shinyDexService,
                                  AnimationService animationService,
                                  DiscordBot discordBot,
                                  DeploymentConfiguration config)
    {
        Set<ListenerService> listeners =
                                    Stream.of(userPointsService,
                                                shinyBetService,
                                                shinyPayoutService,
                                                guessingService,
                                                pokemonInfoService,
                                                shinyInfoService,
                                                shinyDexService,
                                                animationService).collect(Collectors.toSet());

        TwitchBot bot = new TwitchBotImpl(listeners, discordBot, config);

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

    @EagerLoad
    public static DiscordBot build(UserLeaderboardService leaderboardService,
                                   PokemonInfoService pokemonInfoService,
                                   ShinyBetInfoService shinyBetInfoService,
                                   ShinyInfoService shinyInfoService,
                                   ShinyDexService shinyDexService,
                                   DeploymentConfiguration config)
    {
        Set<ListenerService> listeners =
                                    Stream.of(leaderboardService,
                                            pokemonInfoService,
                                            shinyBetInfoService,
                                            shinyInfoService,
                                            shinyDexService).collect(Collectors.toSet());

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
