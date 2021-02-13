package com.schlock.bot.services.bet.impl;

import com.schlock.bot.services.DatabaseModule;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.bet.impl.BettingServiceImpl;
import com.schlock.bot.services.database.BaseDAO;
import com.schlock.bot.services.database.bet.BetDAO;
import com.schlock.bot.services.database.bet.BettingUserDAO;
import com.schlock.bot.services.database.bet.impl.BetDAOImpl;
import com.schlock.bot.services.database.bet.impl.BettingUserDAOImpl;
import com.schlock.bot.services.impl.DeploymentContextImpl;
import com.schlock.bot.services.pokemon.PokemonService;
import com.schlock.bot.services.pokemon.impl.PokemonServiceImpl;
import org.junit.jupiter.api.BeforeEach;

import java.util.HashMap;
import java.util.Map;

class BettingServiceImplTest
{
    private BettingServiceImpl impl;




    @BeforeEach
    public void setup() throws Exception
    {
        DeploymentContext context = new DeploymentContextImpl(DeploymentContext.TEST);

        DatabaseModule database = new DatabaseModule(null)
        {
            private BettingUserDAO userDAO = new BettingUserDAOImpl(null)
            {

            };

            private BetDAO betDAO = new BetDAOImpl(null)
            {

            };

            public <T> T get(Class<T> dao)
            {
                Map<Class, BaseDAO> daos = new HashMap<>();
                daos.put(BettingUserDAO.class, userDAO);
                daos.put(BetDAO.class, betDAO);

                return (T) daos.get(dao);
            }
        };


        PokemonService pokemonService = new PokemonServiceImpl(context);

        impl = new BettingServiceImpl(pokemonService, database, context);
    }
}