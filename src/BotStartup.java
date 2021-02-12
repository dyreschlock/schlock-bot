import com.schlock.bot.Bot;
import com.schlock.bot.TwitchBot;
import com.schlock.bot.services.DatabaseModule;
import com.schlock.bot.services.bet.BettingService;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.ListenerService;
import com.schlock.bot.services.database.bet.BetDAO;
import com.schlock.bot.services.database.bet.BettingUserDAO;
import com.schlock.bot.services.database.bet.impl.BetDAOImpl;
import com.schlock.bot.services.database.bet.impl.BettingUserDAOImpl;
import com.schlock.bot.services.pokemon.PokemonService;
import com.schlock.bot.services.bet.impl.BettingServiceImpl;
import com.schlock.bot.services.impl.DeploymentContextImpl;
import com.schlock.bot.services.pokemon.impl.PokemonServiceImpl;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class BotStartup
{
    private static final String CONFIG_PROPERTIES = "config.properties";

    private SessionFactory hibernateSessionFactory;

    private DatabaseModule database;

    private PokemonService pokemonService;
    private BettingService bettingService;

    private DeploymentContext deploymentContext;

    private Bot discordBot;
    private Bot twitchBot;


    public BotStartup()
    {
    }

    public void run(String contextFlag) throws Exception
    {
        initializeProperties(contextFlag);
        initializeDatabase();
        initializeServices();

        Set<ListenerService> listeners = new HashSet<>();
        listeners.add(pokemonService);
        listeners.add(bettingService);

//        discordBot = new DiscordBot(listeners, deploymentContext);
//        discordBot.startup();

        twitchBot = new TwitchBot(listeners, deploymentContext);
        twitchBot.startup();

        String temp = "";
    }

    private void initializeDatabase() throws Exception
    {
        database = new DatabaseModule(deploymentContext);
        database.setup();
    }

    private void initializeServices()
    {
        pokemonService = new PokemonServiceImpl(deploymentContext);
        bettingService = new BettingServiceImpl(pokemonService, database, deploymentContext);
    }

    private void initializeProperties(String contextFlag) throws Exception
    {
        Properties properties = new Properties();
        InputStream stream = getClass().getClassLoader().getResourceAsStream(CONFIG_PROPERTIES);
        if (stream != null)
        {
            try
            {
                properties.load(stream);
                deploymentContext = new DeploymentContextImpl(contextFlag, properties);
            }
            finally
            {
                stream.close();
            }
        }
        else
        {
            throw new FileNotFoundException("Property file missing: " + CONFIG_PROPERTIES);
        }
    }


    public static void main(String[] args) throws Exception
    {
        String contextFlag = getContextArgument(args);

        new BotStartup().run(contextFlag);
    }

    private static String getContextArgument(String[] args) throws Exception
    {
        if (args.length != 1)
        {
            throw new Exception("Missing Arguments");
        }

        String context = args[0];
        if (DeploymentContext.HOSTED.equals(context) ||
                DeploymentContext.LOCAL.equals(context))
        {
            return context;
        }

        throw new Exception("Argument is incorrect: " + context);
    }
}
