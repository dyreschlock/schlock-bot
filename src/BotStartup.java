import com.schlock.bot.DiscordBot;
import com.schlock.bot.services.PokemonService;
import com.schlock.bot.services.impl.PokemonServiceImpl;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

public class BotStartup
{
    private static final String CONFIG_PROPERTIES = "config.properties";
    private static final String DISCORD_TOKEN = "discord.token";

    private static final PokemonService pokemonService = new PokemonServiceImpl();

    private DiscordBot dbot;

    private Properties properties = new Properties();

    public BotStartup()
    {
    }

    public void run() throws Exception
    {
        initializeProperties();

        startDiscordBot();



    }

    private void startDiscordBot()
    {
        final String discordToken = properties.getProperty(DISCORD_TOKEN);

        dbot = new DiscordBot(pokemonService, discordToken);
        dbot.startup();

        String temp = "";
    }

    private void initializeProperties() throws Exception
    {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(CONFIG_PROPERTIES);
        if (stream != null)
        {
            try
            {
                properties.load(stream);
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
