import com.schlock.bot.Bot;
import com.schlock.bot.DiscordBot;
import com.schlock.bot.TwitchBot;
import com.schlock.bot.services.BettingService;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.ListenerService;
import com.schlock.bot.services.PokemonService;
import com.schlock.bot.services.impl.BettingServiceImpl;
import com.schlock.bot.services.impl.DeploymentContextImpl;
import com.schlock.bot.services.impl.PokemonServiceImpl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class BotStartup
{
    private static final String CONFIG_PROPERTIES = "config.properties";

    private PokemonService pokemonService;
    private BettingService bettingService;

    private DeploymentContext deploymentContext;

    private Bot discordBot;
    private Bot twitchBot;


    public BotStartup()
    {
    }

    public void run() throws Exception
    {
        initializeProperties();
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

    private void initializeServices()
    {
        pokemonService = new PokemonServiceImpl(deploymentContext);
        bettingService = new BettingServiceImpl(pokemonService, deploymentContext);
    }

    private void initializeProperties() throws Exception
    {
        Properties properties = new Properties();
        InputStream stream = getClass().getClassLoader().getResourceAsStream(CONFIG_PROPERTIES);
        if (stream != null)
        {
            try
            {
                properties.load(stream);
                deploymentContext = new DeploymentContextImpl(properties);
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
        new BotStartup().run();
    }
}
