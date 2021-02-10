import com.schlock.bot.Bot;
import com.schlock.bot.DiscordBot;
import com.schlock.bot.TwitchBot;
import com.schlock.bot.entities.Pokemon;
import com.schlock.bot.services.DeploymentContext;
import com.schlock.bot.services.PokemonService;
import com.schlock.bot.services.impl.DeploymentContextImpl;
import com.schlock.bot.services.impl.PokemonServiceImpl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class BotStartup
{
    private static final String CONFIG_PROPERTIES = "config.properties";

    private DeploymentContext deploymentContext;
    private PokemonService pokemonService;

    private Bot discordBot;
    private Bot twitchBot;


    public BotStartup()
    {
    }

    public void run() throws Exception
    {
        initializeProperties();
        initializeServices();

        discordBot = new DiscordBot(pokemonService, deploymentContext);
        discordBot.startup();

        twitchBot = new TwitchBot(pokemonService, deploymentContext);
        twitchBot.startup();

        String temp = "";
    }

    private void initializeServices()
    {
        pokemonService = new PokemonServiceImpl(deploymentContext);
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
