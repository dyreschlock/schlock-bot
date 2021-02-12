package com.schlock.bot.services.bet.impl;

import com.schlock.bot.services.DatabaseModule;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.bet.impl.BettingServiceImpl;
import com.schlock.bot.services.database.bet.BetDAO;
import com.schlock.bot.services.database.bet.BettingUserDAO;
import com.schlock.bot.services.database.bet.impl.BetDAOImpl;
import com.schlock.bot.services.database.bet.impl.BettingUserDAOImpl;
import com.schlock.bot.services.impl.DeploymentContextImpl;
import com.schlock.bot.services.pokemon.PokemonService;
import com.schlock.bot.services.pokemon.impl.PokemonServiceImpl;
import org.junit.jupiter.api.BeforeEach;

class BettingServiceImplTest
{
    private BettingServiceImpl impl;




    @BeforeEach
    public void setup() throws Exception
    {
        DeploymentContext context = new DeploymentContextImpl(DeploymentContext.TEST)
        {
            @Override
            public String getDiscordToken()
            {
                return null;
            }
        };

        DatabaseModule database = new DatabaseModule(null)
        {

        };

        BettingUserDAO userDAO = new BettingUserDAOImpl(null)
        {

        };

        BetDAO betDAO = new BetDAOImpl(null)
        {

        };

        PokemonService pokemonService = new PokemonServiceImpl(context);

        impl = new BettingServiceImpl(pokemonService, database, context);
    }
}