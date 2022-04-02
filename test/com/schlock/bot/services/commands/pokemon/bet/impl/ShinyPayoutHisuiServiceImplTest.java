package com.schlock.bot.services.commands.pokemon.bet.impl;

import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.entities.base.UserManagement;
import com.schlock.bot.services.entities.base.impl.UserManagementImpl;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.PokemonUtils;
import com.schlock.bot.services.entities.pokemon.ShinyGetFormatter;
import com.schlock.bot.services.entities.pokemon.impl.PokemonManagementImpl;
import com.schlock.bot.services.entities.pokemon.impl.PokemonUtilsImpl;
import com.schlock.bot.services.entities.pokemon.impl.ShinyGetFormatterImpl;

public class ShinyPayoutHisuiServiceImplTest extends DatabaseTest
{
    private UserManagement userManagement;
    private ShinyGetFormatter shinyFormatter;

    private ShinyPayoutHisuiServiceImpl impl;

    @Override
    protected void before() throws Exception
    {
        userManagement = new UserManagementImpl(database(), config());

        PokemonUtils pokemonUtils = new PokemonUtilsImpl(messages());

        PokemonManagement pokemonManagement = new PokemonManagementImpl(pokemonUtils, config());

        shinyFormatter = new ShinyGetFormatterImpl(pokemonManagement, messages());

        impl = new ShinyPayoutHisuiServiceImpl(pokemonManagement, shinyFormatter, database(), messages(), config());


        createTestObjects();
    }

    @Override
    protected void after() throws Exception
    {
        removeTestObjects();
    }

    private void createTestObjects()
    {

    }

    private void removeTestObjects()
    {

    }
}
