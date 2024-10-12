package com.schlock.bot.services.commands;

import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.commands.base.*;
import com.schlock.bot.services.commands.base.impl.*;
import com.schlock.bot.services.commands.game.RandomGameService;
import com.schlock.bot.services.commands.game.impl.RandomGameServiceImpl;
import com.schlock.bot.services.commands.pokemon.bet.ShinyBetInfoService;
import com.schlock.bot.services.commands.pokemon.bet.ShinyBetService;
import com.schlock.bot.services.commands.pokemon.bet.ShinyPayoutHisuiService;
import com.schlock.bot.services.commands.pokemon.bet.ShinyPayoutLetsGoService;
import com.schlock.bot.services.commands.pokemon.bet.impl.ShinyBetInfoServiceImpl;
import com.schlock.bot.services.commands.pokemon.bet.impl.ShinyBetServiceImpl;
import com.schlock.bot.services.commands.pokemon.bet.impl.ShinyPayoutHisuiServiceImpl;
import com.schlock.bot.services.commands.pokemon.bet.impl.ShinyPayoutLetsGoServiceImpl;
import com.schlock.bot.services.commands.pokemon.quiz.GenCompletionQuizService;
import com.schlock.bot.services.commands.pokemon.quiz.PokemonInfoService;
import com.schlock.bot.services.commands.pokemon.quiz.RatePokemonService;
import com.schlock.bot.services.commands.pokemon.quiz.WhosThatPokemonService;
import com.schlock.bot.services.commands.pokemon.quiz.impl.GenCompletionQuizServiceImpl;
import com.schlock.bot.services.commands.pokemon.quiz.impl.PokemonInfoServiceImpl;
import com.schlock.bot.services.commands.pokemon.quiz.impl.RatePokemonServiceImpl;
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

        //game
        binder.bind(RandomGameService.class, RandomGameServiceImpl.class);

        //pokemon
        binder.bind(ShinyBetInfoService.class, ShinyBetInfoServiceImpl.class);
        binder.bind(ShinyBetService.class, ShinyBetServiceImpl.class);
        binder.bind(ShinyPayoutHisuiService.class, ShinyPayoutHisuiServiceImpl.class);
        binder.bind(ShinyPayoutLetsGoService.class, ShinyPayoutLetsGoServiceImpl.class);

        binder.bind(ShinyDexService.class, ShinyDexServiceImpl.class);
        binder.bind(ShinyInfoService.class, ShinyInfoServiceImpl.class);

        binder.bind(PokemonInfoService.class, PokemonInfoServiceImpl.class);
        binder.bind(GenCompletionQuizService.class, GenCompletionQuizServiceImpl.class);
        binder.bind(WhosThatPokemonService.class, WhosThatPokemonServiceImpl.class);
        binder.bind(RatePokemonService.class, RatePokemonServiceImpl.class);
    }

    @EagerLoad
    public static GameControllerService build(WhosThatPokemonService guessingService,
                                              GenCompletionQuizService genCompletionQuizService,
                                              RatePokemonService rateService,
                                              Messages messages,
                                              DeploymentConfiguration config)
    {
        //Who's that Pokemon? is on by default.
        guessingService.turnOn();

        //Rate Pokemon is on by default.
        rateService.turnOn();

        Map<String, ChatGameListenerService> chatGames = new HashMap<>();

        chatGames.put(guessingService.getGameId(), guessingService);
        chatGames.put(genCompletionQuizService.getGameId(), genCompletionQuizService);
        chatGames.put(rateService.getGameId(), rateService);

        GameControllerService gameController = new GameControllerServiceImpl(chatGames, messages, config);

        return gameController;
    }
}
