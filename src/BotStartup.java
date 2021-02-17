import com.schlock.bot.Bot;
import com.schlock.bot.TwitchBot;
import com.schlock.bot.services.DatabaseModule;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.ListenerService;
import com.schlock.bot.services.apps.UserService;
import com.schlock.bot.services.apps.bet.ShinyBetService;
import com.schlock.bot.services.apps.bet.impl.ShinyBetServiceImpl;
import com.schlock.bot.services.apps.guess.GuessingService;
import com.schlock.bot.services.apps.guess.impl.GuessingServiceImpl;
import com.schlock.bot.services.apps.impl.UserServiceImpl;
import com.schlock.bot.services.apps.pokemon.PokemonService;
import com.schlock.bot.services.apps.pokemon.ShinyInfoService;
import com.schlock.bot.services.apps.pokemon.impl.PokemonServiceImpl;
import com.schlock.bot.services.apps.pokemon.impl.ShinyInfoServiceImpl;
import com.schlock.bot.services.impl.DeploymentContextImpl;

import java.util.HashSet;
import java.util.Set;

public class BotStartup
{
    private DatabaseModule database;

    private UserService userService;
    private PokemonService pokemonService;

    private ShinyBetService shinyBetService;
    private GuessingService guessingService;
    private ShinyInfoService shinyInfoService;

    private DeploymentContext deploymentContext;

    private Bot discordBot;
    private Bot twitchBot;


    public BotStartup()
    {
    }

    public void run(String contextFlag) throws Exception
    {
        initializeDeploymentContext(contextFlag);
        initializeDatabase();
        initializeServices();

        Set<ListenerService> listeners = new HashSet<>();
        listeners.add(userService);
        listeners.add(pokemonService);
        listeners.add(shinyBetService);
        listeners.add(guessingService);
        listeners.add(shinyInfoService);

//        discordBot = new DiscordBot(listeners, deploymentContext);
//        discordBot.startup();

        twitchBot = new TwitchBot(listeners, deploymentContext);
        twitchBot.startup();

        String temp = "";
    }

    private void initializeDeploymentContext(String contextFlag) throws Exception
    {
        deploymentContext = new DeploymentContextImpl(contextFlag);
        deploymentContext.loadProperties();
    }

    private void initializeDatabase() throws Exception
    {
        database = new DatabaseModule(deploymentContext);
        database.setup();
    }

    private void initializeServices()
    {
        userService = new UserServiceImpl(database, deploymentContext);
        pokemonService = new PokemonServiceImpl(deploymentContext);

        shinyBetService = new ShinyBetServiceImpl(pokemonService, userService, database, deploymentContext);
        guessingService = new GuessingServiceImpl(pokemonService, userService, database, deploymentContext);
        shinyInfoService = new ShinyInfoServiceImpl(pokemonService, database, deploymentContext);
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
