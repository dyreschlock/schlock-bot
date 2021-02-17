package com.schlock.bot.services.apps.bet.impl;

import com.schlock.bot.services.DatabaseModule;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.apps.UserService;
import com.schlock.bot.services.apps.impl.UserServiceImpl;
import com.schlock.bot.services.apps.pokemon.PokemonService;
import com.schlock.bot.services.apps.pokemon.impl.PokemonServiceImpl;
import com.schlock.bot.services.database.DatabaseTest;
import org.junit.jupiter.api.BeforeEach;

class ShinyBetServiceImplTest extends DatabaseTest
{
    private ShinyBetServiceImpl impl;




    @BeforeEach
    public void setup() throws Exception
    {
        setupDatabase();

        DeploymentContext context = getDeploymentContext();
        DatabaseModule database = getDatabase();

        PokemonService pokemonService = new PokemonServiceImpl(context);
        UserService userService = new UserServiceImpl(database, context);

        impl = new ShinyBetServiceImpl(pokemonService, userService, database, context);
    }
}