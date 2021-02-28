import com.schlock.bot.Bot;
import com.schlock.bot.TwitchBot;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.StandaloneDatabase;
import com.schlock.bot.services.bot.apps.UserService;
import com.schlock.bot.services.bot.apps.ListenerService;
import com.schlock.bot.services.bot.apps.bet.ShinyBetService;
import com.schlock.bot.services.bot.apps.bet.ShinyPayoutService;
import com.schlock.bot.services.bot.apps.guess.GuessingService;
import com.schlock.bot.services.bot.apps.pokemon.PokemonService;
import com.schlock.bot.services.bot.apps.pokemon.ShinyInfoService;
import com.schlock.bot.services.impl.DeploymentConfigurationImpl;

import java.util.HashSet;
import java.util.Set;

public class BotStartup
{
    private StandaloneDatabase database;

    private UserService userService;
    private PokemonService pokemonService;

    private ShinyBetService shinyBetService;
    private GuessingService guessingService;
    private ShinyInfoService shinyInfoService;
    private ShinyPayoutService shinyPayoutService;

    private DeploymentConfiguration configuration;

    private Bot discordBot;
    private Bot twitchBot;


    public BotStartup()
    {
    }

    public void run(String contextFlag) throws Exception
    {
        initializeDeploymentConfiguration(contextFlag);
        initializeDatabase();
        initializeServices();

        Set<ListenerService> listeners = new HashSet<>();
        listeners.add(userService);
        listeners.add(pokemonService);
        listeners.add(shinyBetService);
        listeners.add(guessingService);
        listeners.add(shinyInfoService);
        listeners.add(shinyPayoutService);

//        discordBot = new DiscordBot(listeners, configuration);
//        discordBot.startup();

        twitchBot = new TwitchBot(listeners, configuration);
        twitchBot.startup();

        String temp = "";
    }

    private void initializeDeploymentConfiguration(String contextFlag) throws Exception
    {
        configuration = DeploymentConfigurationImpl.createDeploymentConfiguration(contextFlag);
        configuration.loadProperties();
    }

    private void initializeDatabase() throws Exception
    {
        database = new StandaloneDatabase(configuration);
        database.setup();
    }

    private void initializeServices()
    {
//        userService = new UserServiceImpl(database, configuration);
//        pokemonService = new PokemonServiceImpl(configuration);
//
//        shinyBetService = new ShinyBetServiceImpl(pokemonService, userService, database, configuration);
//        guessingService = new GuessingServiceImpl(pokemonService, userService, database, configuration);
//        shinyInfoService = new ShinyInfoServiceImpl(pokemonService, database, configuration);
//
//        shinyPayoutService = new ShinyPayoutServiceImpl(pokemonService,  database, configuration);
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
        if (DeploymentConfigurationImpl.HOSTED.equals(context) ||
                DeploymentConfigurationImpl.LOCAL.equals(context))
        {
            return context;
        }

        throw new Exception("Argument is incorrect: " + context);
    }
}
