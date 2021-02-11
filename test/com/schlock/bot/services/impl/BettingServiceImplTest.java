package com.schlock.bot.services.impl;

import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.PokemonService;
import org.junit.jupiter.api.BeforeEach;

class BettingServiceImplTest
{
    private BettingServiceImpl impl;




    @BeforeEach
    public void setup() throws Exception
    {
        DeploymentContext context = new DeploymentContextImpl(null)
        {
            @Override
            public String getDiscordToken()
            {
                return null;
            }
        };

        PokemonService pokemonService = new PokemonServiceImpl(context);

        impl = new BettingServiceImpl(pokemonService, context);
    }
}