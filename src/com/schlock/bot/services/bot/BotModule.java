package com.schlock.bot.services.bot;

import com.schlock.bot.services.bot.apps.ShinyDexEntryService;
import com.schlock.bot.services.bot.apps.UserService;
import com.schlock.bot.services.bot.apps.bet.ShinyBetService;
import com.schlock.bot.services.bot.apps.bet.ShinyPayoutService;
import com.schlock.bot.services.bot.apps.bet.impl.ShinyBetServiceImpl;
import com.schlock.bot.services.bot.apps.bet.impl.ShinyPayoutServiceImpl;
import com.schlock.bot.services.bot.apps.guess.GuessingService;
import com.schlock.bot.services.bot.apps.guess.impl.GuessingServiceImpl;
import com.schlock.bot.services.bot.apps.impl.ShinyDexEntryServiceImpl;
import com.schlock.bot.services.bot.apps.impl.UserServiceImpl;
import com.schlock.bot.services.bot.apps.pokemon.PokemonService;
import com.schlock.bot.services.bot.apps.pokemon.ShinyInfoService;
import com.schlock.bot.services.bot.apps.pokemon.impl.PokemonServiceImpl;
import com.schlock.bot.services.bot.apps.pokemon.impl.ShinyInfoServiceImpl;
import org.apache.tapestry5.ioc.ServiceBinder;

public class BotModule
{
    public static void bind(ServiceBinder binder)
    {
        binder.bind(ShinyDexEntryService.class, ShinyDexEntryServiceImpl.class);

        //app listeners
        binder.bind(UserService.class, UserServiceImpl.class);

        binder.bind(ShinyBetService.class, ShinyBetServiceImpl.class);
        binder.bind(ShinyPayoutService.class, ShinyPayoutServiceImpl.class);

        binder.bind(GuessingService.class, GuessingServiceImpl.class);

        binder.bind(PokemonService.class, PokemonServiceImpl.class);
        binder.bind(ShinyInfoService.class, ShinyInfoServiceImpl.class);
    }
}
