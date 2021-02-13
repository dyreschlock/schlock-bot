package com.schlock.bot.services.apps.bet.impl;

import com.schlock.bot.services.DatabaseModule;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.database.BaseDAO;
import com.schlock.bot.services.database.apps.BetDAO;
import com.schlock.bot.services.database.apps.UserDAO;
import com.schlock.bot.services.database.apps.impl.BetDAOImpl;
import com.schlock.bot.services.database.apps.impl.UserDAOImpl;
import com.schlock.bot.services.impl.DeploymentContextImpl;
import com.schlock.bot.services.apps.pokemon.PokemonService;
import com.schlock.bot.services.apps.pokemon.impl.PokemonServiceImpl;
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
            private UserDAO userDAO = new UserDAOImpl(null)
            {

            };

            private BetDAO betDAO = new BetDAOImpl(null)
            {

            };

            public <T> T get(Class<T> dao)
            {
                Map<Class, BaseDAO> daos = new HashMap<>();
                daos.put(UserDAO.class, userDAO);
                daos.put(BetDAO.class, betDAO);

                return (T) daos.get(dao);
            }
        };


        PokemonService pokemonService = new PokemonServiceImpl(context);

        impl = new BettingServiceImpl(pokemonService, database, context);
    }
}