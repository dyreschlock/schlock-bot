package setup;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.entities.pokemon.ShinyDexEntryHisui;

import java.util.List;

public class HisuiDexEntrySetup extends GoDexEntrySetup
{
    public void run()
    {
        try
        {
            setupDatabase();

            List<Pokemon> pokemon = pokemonManagement.getHisuiPokemonInNumberOrder();

            for (Pokemon poke : pokemon)
            {
                createEntryAndSave(poke);
            }
        }
        finally
        {
            teardownDatabase();
        }
    }

    private void createEntryAndSave(Pokemon poke)
    {
        ShinyDexEntryHisui entry = new ShinyDexEntryHisui();

        entry.setPokemonNumber(poke.getHisuiNumber());
        entry.setPokemonId(poke.getId());
        entry.setHaveShiny(false);

        session.save(entry);
    }

    public static void main(String[] args)
    {
//        new HisuiDexEntrySetup().run();
    }
}
