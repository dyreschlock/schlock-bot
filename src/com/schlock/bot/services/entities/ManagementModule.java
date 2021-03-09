package com.schlock.bot.services.entities;

import com.schlock.bot.services.entities.base.UserManagement;
import com.schlock.bot.services.entities.base.impl.UserManagementImpl;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.PokemonUtils;
import com.schlock.bot.services.entities.pokemon.ShinyGetFormatter;
import com.schlock.bot.services.entities.pokemon.impl.PokemonManagementImpl;
import com.schlock.bot.services.entities.pokemon.impl.PokemonUtilsImpl;
import com.schlock.bot.services.entities.pokemon.impl.ShinyGetFormatterImpl;
import org.apache.tapestry5.ioc.ServiceBinder;

public class ManagementModule
{
    public static void bind(ServiceBinder binder)
    {
        binder.bind(UserManagement.class, UserManagementImpl.class);

        binder.bind(PokemonManagement.class, PokemonManagementImpl.class);
        binder.bind(PokemonUtils.class, PokemonUtilsImpl.class);
        binder.bind(ShinyGetFormatter.class, ShinyGetFormatterImpl.class);
    }
}
