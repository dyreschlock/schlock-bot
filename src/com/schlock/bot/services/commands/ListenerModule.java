package com.schlock.bot.services.commands;

import com.schlock.bot.services.commands.base.AnimationService;
import com.schlock.bot.services.commands.base.UserBalanceService;
import com.schlock.bot.services.commands.base.UserLeaderboardService;
import com.schlock.bot.services.commands.base.UserPointsService;
import com.schlock.bot.services.commands.base.impl.AnimationServiceImpl;
import com.schlock.bot.services.commands.base.impl.UserBalanceServiceImpl;
import com.schlock.bot.services.commands.base.impl.UserLeaderboardServiceImpl;
import com.schlock.bot.services.commands.base.impl.UserPointsServiceImpl;
import com.schlock.bot.services.commands.pokemon.bet.ShinyBetInfoService;
import com.schlock.bot.services.commands.pokemon.bet.ShinyBetService;
import com.schlock.bot.services.commands.pokemon.bet.ShinyPayoutService;
import com.schlock.bot.services.commands.pokemon.bet.impl.ShinyBetInfoServiceImpl;
import com.schlock.bot.services.commands.pokemon.bet.impl.ShinyBetServiceImpl;
import com.schlock.bot.services.commands.pokemon.bet.impl.ShinyPayoutServiceImpl;
import com.schlock.bot.services.commands.pokemon.shiny.ShinyDexService;
import com.schlock.bot.services.commands.pokemon.shiny.ShinyInfoService;
import com.schlock.bot.services.commands.pokemon.shiny.impl.ShinyDexServiceImpl;
import com.schlock.bot.services.commands.pokemon.shiny.impl.ShinyInfoServiceImpl;
import com.schlock.bot.services.commands.pokemon.whodat.PokemonGuessingService;
import com.schlock.bot.services.commands.pokemon.whodat.PokemonInfoService;
import com.schlock.bot.services.commands.pokemon.whodat.impl.PokemonGuessingServiceImpl;
import com.schlock.bot.services.commands.pokemon.whodat.impl.PokemonInfoServiceImpl;
import org.apache.tapestry5.ioc.ServiceBinder;

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

        binder.bind(PokemonGuessingService.class, PokemonGuessingServiceImpl.class);
        binder.bind(PokemonInfoService.class, PokemonInfoServiceImpl.class);
    }
}
