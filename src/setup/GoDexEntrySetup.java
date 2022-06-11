package setup;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.entities.pokemon.ShinyDexEntryGo;
import com.schlock.bot.services.DeploymentConfiguration;
import com.schlock.bot.services.database.DatabaseModule;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import com.schlock.bot.services.entities.pokemon.PokemonUtils;
import com.schlock.bot.services.entities.pokemon.impl.PokemonManagementImpl;
import com.schlock.bot.services.entities.pokemon.impl.PokemonUtilsImpl;
import com.schlock.bot.services.impl.DeploymentConfigurationImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class GoDexEntrySetup
{
    protected SessionFactory sessionFactory;
    protected Session session;

    protected PokemonManagement pokemonManagement;

    public void run()
    {
        try
        {
            setupDatabase();

            for(int i = 899; i < 905; i++)
            {
                createEntryAndSave(i + 1);
            }
        }
        finally
        {
            teardownDatabase();
        }
    }

    private void createEntryAndSave(Integer number)
    {
        Pokemon poke = pokemonManagement.getPokemonFromText(number.toString());

        ShinyDexEntryGo entry = new ShinyDexEntryGo();
        entry.setNumberCode(poke.getNumberString());
        entry.setPokemonId(poke.getId());
        entry.setHave(true);
        entry.setShinyGoFirst(false);
        entry.setShinyGoSecond(false);
        entry.setShinyHomeOwn(false);
        entry.setShinyHomeOther(false);

        session.save(entry);
    }

    protected void setupDatabase()
    {
        DeploymentConfiguration config = DeploymentConfigurationImpl.createDeploymentConfiguration(DeploymentConfiguration.LOCAL);

        PokemonUtils utils = new PokemonUtilsImpl(null);

        pokemonManagement = new PokemonManagementImpl(utils, config);


        final String username = config.getHibernateProperty(DatabaseModule.HIBERNATE_USERNAME);
        final String password = config.getHibernateProperty(DatabaseModule.HIBERNATE_PASSWORD);
        final String url = config.getHibernateProperty(DatabaseModule.HIBERNATE_URL);

        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder();


        Configuration dbconfig = new Configuration();

        dbconfig.configure(StandardServiceRegistryBuilder.DEFAULT_CFG_RESOURCE_NAME);

        dbconfig.setProperty(DatabaseModule.HIBERNATE_USERNAME, username);
        dbconfig.setProperty(DatabaseModule.HIBERNATE_PASSWORD, password);
        dbconfig.setProperty(DatabaseModule.HIBERNATE_URL, url);

        dbconfig.setProperty(DatabaseModule.HIBERNATE_HIKARI_USERNAME, username);
        dbconfig.setProperty(DatabaseModule.HIBERNATE_HIKARI_PASSWORD, password);
        dbconfig.setProperty(DatabaseModule.HIBERNATE_HIKARI_URL, url);

        sessionFactory = dbconfig.buildSessionFactory();
        session = sessionFactory.openSession();
        session.beginTransaction();

    }

    protected void teardownDatabase()
    {
        session.getTransaction().commit();
        session.close();
        sessionFactory.close();
    }

    public static void main(String[] args)
    {
//        new GoDexEntrySetup().run();
    }
}
