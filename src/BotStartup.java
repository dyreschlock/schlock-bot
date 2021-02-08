import com.schlock.bot.DiscordBot;
import com.schlock.services.PokemonService;
import com.schlock.services.impl.PokemonServiceImpl;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Function;

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
        InputStream stream = null;
        try
        {
            stream = getClass().getClassLoader().getResourceAsStream(CONFIG_PROPERTIES);
            if (stream != null)
            {
                properties.load(stream);
            }
            else
            {
                throw new FileNotFoundException("Property file missing: " + CONFIG_PROPERTIES);
            }
        }
        finally
        {
            stream.close();
        }
    }


    public static void main(String[] args) throws Exception
    {
        new BotStartup().run();
    }
}
