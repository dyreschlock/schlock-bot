package com.schlock.bot.services.bot;

import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.bot.discord.DiscordBot;
import com.schlock.bot.services.bot.discord.impl.DiscordBotImpl;
import com.schlock.bot.services.bot.twitch.TwitchChatBot;
import com.schlock.bot.services.bot.twitch.TwitchEventBot;
import com.schlock.bot.services.bot.twitch.impl.TwitchChatBotImpl;
import com.schlock.bot.services.bot.twitch.impl.TwitchEventBotImpl;
import com.schlock.bot.services.commands.ListenerService;
import com.schlock.bot.services.commands.base.*;
import com.schlock.bot.services.commands.pokemon.bet.ShinyBetInfoService;
import com.schlock.bot.services.commands.pokemon.bet.ShinyHisuiService;
import com.schlock.bot.services.commands.pokemon.quiz.GenCompletionQuizService;
import com.schlock.bot.services.commands.pokemon.shiny.ShinyDexService;
import com.schlock.bot.services.commands.pokemon.shiny.ShinyInfoService;
import com.schlock.bot.services.commands.pokemon.bet.ShinyBetService;
import com.schlock.bot.services.commands.pokemon.bet.ShinyPayoutService;
import com.schlock.bot.services.commands.pokemon.quiz.WhosThatPokemonService;
import com.schlock.bot.services.commands.pokemon.quiz.PokemonInfoService;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.EagerLoad;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BotModule
{
    public static void bind(ServiceBinder binder)
    {
    }

    @EagerLoad
    public static TwitchChatBot build(GameControllerService gameController,
                                      UserPointsService userPointsService,
                                      ShinyBetService shinyBetService,
                                      ShinyPayoutService shinyPayoutService,
                                      GenCompletionQuizService genCompletionQuizService,
                                      WhosThatPokemonService guessingService,
                                      PokemonInfoService pokemonInfoService,
                                      ShinyHisuiService shinyHisuiService,
                                      ShinyInfoService shinyInfoService,
                                      ShinyDexService shinyDexService,
                                      AnimationService animationService,
                                      DiscordBot discordBot,
                                      DeploymentConfiguration config)
    {
        Set<ListenerService> listeners =
                                    Stream.of(gameController,
                                                userPointsService,
                                                shinyBetService,
                                                shinyPayoutService,
                                                genCompletionQuizService,
                                                guessingService,
                                                pokemonInfoService,
                                                shinyHisuiService,
                                                shinyInfoService,
                                                shinyDexService,
                                                animationService).collect(Collectors.toSet());

        TwitchChatBot bot = new TwitchChatBotImpl(listeners, discordBot, config);

        Thread twitchChatBotThread = new Thread()
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
        twitchChatBotThread.start();

        return bot;
    }

    @EagerLoad
    public static TwitchEventBot build(TwitchChatBot twitchBot,
                                        DeploymentConfiguration config)
    {
        Set<ListenerService> listeners = new HashSet<>();

        TwitchEventBot bot = new TwitchEventBotImpl(listeners, twitchBot, config);

        Thread twitchEventBotThread = new Thread()
        {
            @Override
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
        //twitchEventBotThread.start();

        return bot;
    }

    @EagerLoad
    public static DiscordBot build(UserLeaderboardService leaderboardService,
                                   UserBalanceService userBalanceService,
                                   PokemonInfoService pokemonInfoService,
                                   ShinyBetInfoService shinyBetInfoService,
                                   ShinyInfoService shinyInfoService,
                                   ShinyDexService shinyDexService,
                                   TwitchChatBot twitchBot,
                                   DeploymentConfiguration config)
    {
        Set<ListenerService> listeners =
                                    Stream.of(leaderboardService,
                                            userBalanceService,
                                            pokemonInfoService,
                                            shinyBetInfoService,
                                            shinyInfoService,
                                            shinyDexService).collect(Collectors.toSet());

        DiscordBot bot = new DiscordBotImpl(listeners, twitchBot, config);

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
