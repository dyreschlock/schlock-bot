package com.schlock.bot.services.commands;

import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.base.*;
import com.schlock.bot.services.commands.base.impl.*;
import com.schlock.bot.services.commands.pokemon.bet.ShinyBetInfoService;
import com.schlock.bot.services.commands.pokemon.bet.ShinyBetService;
import com.schlock.bot.services.commands.pokemon.bet.ShinyPayoutService;
import com.schlock.bot.services.commands.pokemon.bet.impl.ShinyBetInfoServiceImpl;
import com.schlock.bot.services.commands.pokemon.bet.impl.ShinyBetServiceImpl;
import com.schlock.bot.services.commands.pokemon.bet.impl.ShinyPayoutServiceImpl;
import com.schlock.bot.services.commands.pokemon.quiz.GenCompletionQuizService;
import com.schlock.bot.services.commands.pokemon.quiz.PokemonInfoService;
import com.schlock.bot.services.commands.pokemon.quiz.WhosThatPokemonService;
import com.schlock.bot.services.commands.pokemon.quiz.impl.GenCompletionQuizServiceImpl;
import com.schlock.bot.services.commands.pokemon.quiz.impl.PokemonInfoServiceImpl;
import com.schlock.bot.services.commands.pokemon.quiz.impl.WhosThatPokemonServiceImpl;
import com.schlock.bot.services.commands.pokemon.shiny.ShinyDexService;
import com.schlock.bot.services.commands.pokemon.shiny.ShinyInfoService;
import com.schlock.bot.services.commands.pokemon.shiny.impl.ShinyDexServiceImpl;
import com.schlock.bot.services.commands.pokemon.shiny.impl.ShinyInfoServiceImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.EagerLoad;

import java.util.HashMap;
import java.util.Map;

public class ListenerModule
{
    public static void bind(ServiceBinder binder)
    {
        //base
        binder.bind(UserPointsService.class, UserPointsServiceImpl.class);
        binder.bind(UserBalanceService.class, UserBalanceServiceImpl.class);
        binder.bind(AnimationService.class, AnimationServiceImpl.class);
        binder.bind(UserLeaderboardService.class, UserLeaderboardServiceImpl.class);

        //pokemon
        binder.bind(ShinyBetService.class, ShinyBetServiceImpl.class);
        binder.bind(ShinyBetInfoService.class, ShinyBetInfoServiceImpl.class);
        binder.bind(ShinyPayoutService.class, ShinyPayoutServiceImpl.class);

        binder.bind(ShinyDexService.class, ShinyDexServiceImpl.class);
        binder.bind(ShinyInfoService.class, ShinyInfoServiceImpl.class);

        binder.bind(PokemonInfoService.class, PokemonInfoServiceImpl.class);
        binder.bind(GenCompletionQuizService.class, GenCompletionQuizServiceImpl.class);
        binder.bind(WhosThatPokemonService.class, WhosThatPokemonServiceImpl.class);
    }

    @EagerLoad
    public static GameControllerService build(WhosThatPokemonService guessingService,
                                              GenCompletionQuizService genCompletionQuizService,
                                              Messages messages,
                                              DeploymentConfiguration config)
    {
        //Who's that Pokemon? is on by default.
        guessingService.turnOn();

        Map<String, ChatGameListenerService> chatGames = new HashMap<>();

        chatGames.put(guessingService.getGameId(), guessingService);
        chatGames.put(genCompletionQuizService.getGameId(), genCompletionQuizService);


        GameControllerService gameController = new GameControllerServiceImpl(chatGames, messages, config);

        return gameController;
    }
}
