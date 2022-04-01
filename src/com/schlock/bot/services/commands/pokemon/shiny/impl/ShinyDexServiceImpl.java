package com.schlock.bot.services.commands.pokemon.shiny.impl;

import com.schlock.bot.entities.pokemon.*;
import com.schlock.bot.services.commands.AbstractListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.pokemon.shiny.ShinyDexService;
import com.schlock.bot.services.database.adhoc.DatabaseManager;
import com.schlock.bot.services.database.pokemon.*;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import org.apache.tapestry5.ioc.Messages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShinyDexServiceImpl extends AbstractListenerService implements ShinyDexService
{
    protected static final String CURRENT_SHINY_DEX_KEY = "current-shiny-dex";

    private static final String SHINY_DEX_COMMAND = "!shinydex";

    private final PokemonManagement pokemonManagement;

    private DatabaseManager database;

    private Map<String, Pokemon> shinyChecklist;

    public ShinyDexServiceImpl(PokemonManagement pokemonManagement,
                               DatabaseManager database,
                               Messages messages)
    {
        super(messages);

        this.pokemonManagement = pokemonManagement;

        this.database = database;
    }

    public int getOverallShinyCount()
    {
        initialize();

        int count = 0;
        for(Pokemon pokemon : shinyChecklist.values())
        {
            if (pokemon.isShiny())
            {
                count++;
            }
        }
        return count;
    }

    public boolean isHaveShiny(String pokemonNumberCode)
    {
        initialize();

        Pokemon poke = shinyChecklist.get(pokemonNumberCode);
        return poke.isShiny();
    }

    private void initialize()
    {
        if (shinyChecklist == null)
        {
            shinyChecklist = new HashMap<>();

            List<Pokemon> pokemon = pokemonManagement.getAllPokemonInNumberOrder();

            for (Pokemon poke : pokemon)
            {
                shinyChecklist.put(poke.getNumberString(), poke);
            }

            checkLetsGoShinies();
            checkGoShinies();
            checkHisuiShinies();
            checkMainSeriesShinies();
        }
    }

    private void checkLetsGoShinies()
    {
        for(ShinyDexEntryLetsGo entry : getShinyDexLetsGoEntries())
        {
            String number = entry.getNumberCode();

            Pokemon poke = shinyChecklist.get(number);
            poke.setShiny(true);
        }
    }

    private void checkGoShinies()
    {
        for (ShinyDexEntryGo entry : getPokemonGoEntries())
        {
            if (entry.isShinyGo() || entry.isShinyHome())
            {
                String number = entry.getNumberCode();

                Pokemon poke = shinyChecklist.get(number);
                poke.setShiny(true);
            }
        }
    }

    private void checkHisuiShinies()
    {
        for (ShinyDexEntryHisui entry : getShinyDexHisuiEntries())
        {
            if (entry.isHaveShiny())
            {
                String number = entry.getNumberCode();

                Pokemon poke = shinyChecklist.get(number);
                if(poke != null)
                {
                    poke.setShiny(true);
                }
            }
        }
    }

    private void checkMainSeriesShinies()
    {
        for (ShinyDexEntryMain entry : getShinyDexMainEntries())
        {
            if (entry.isNormal())
            {
                String number = entry.getNumberCode();

                Pokemon poke = shinyChecklist.get(number);
                poke.setShiny(true);
            }
        }
    }

    private List<ShinyDexEntryMain> getShinyDexMainEntries()
    {
        List<ShinyDexEntryMain> mainEntries = database.get(ShinyDexEntryMainDAO.class).getAll();
        return mainEntries;
    }

    public List<ShinyDexEntryLetsGo> getShinyDexLetsGoEntries()
    {
        List<ShinyDexEntryLetsGo> entries = database.get(ShinyDexEntryLetsGoDAO.class).getAll();
        return entries;
    }

    public List<ShinyDexEntryHisui> getShinyDexHisuiEntries()
    {
        List<ShinyDexEntryHisui> entries = database.get(ShinyDexEntryHisuiDAO.class).getAll();
        return entries;
    }

    public List<ShinyHisuiGet> getShinyDexHisuiGets()
    {
        List<ShinyHisuiGet> gets = database.get(ShinyHisuiGetDAO.class).getAll();
        for (ShinyHisuiGet get : gets)
        {
            Pokemon pokemon = pokemonManagement.getPokemonFromText(get.getPokemonId());

            get.setHisuiNumber(pokemon.getHisuiNumber());
        }
        return gets;
    }

    public List<ShinyDexEntryGo> getPokemonGoEntries()
    {
        List<ShinyDexEntryGo> entries = database.get(ShinyDexEntryGoDAO.class).getAll();
        return entries;
    }


    public boolean isAcceptRequest(String username, String message)
    {
        return message != null &&
                (message.toLowerCase().startsWith(SHINY_DEX_COMMAND));
    }

    public boolean isTerminateAfterRequest()
    {
        return true;
    }

    protected ListenerResponse processRequest(String username, String in)
    {
        String commandText = in.toLowerCase().trim();
        if (commandText.startsWith(SHINY_DEX_COMMAND))
        {
            List<ShinyDexEntryLetsGo> entries = database.get(ShinyDexEntryLetsGoDAO.class).getAll();

            Integer count = entries.size();

            return formatSingleResponse(CURRENT_SHINY_DEX_KEY, count.toString());
        }
        return nullResponse();
    }


    private static final String REMAINING_COUNT = "hisui-remaining-counts";

    public String getHisuiRemainingCountsMessage()
    {
        Integer fieldCount = database.get(ShinyDexEntryHisuiDAO.class).getRemainingShinyCountInFieldlands();
        Integer mireCount = database.get(ShinyDexEntryHisuiDAO.class).getRemainingShinyCountInMirelands();
        Integer coastCount = database.get(ShinyDexEntryHisuiDAO.class).getRemainingShinyCountInCoastlands();
        Integer highCount = database.get(ShinyDexEntryHisuiDAO.class).getRemainingShinyCountInHighlands();
        Integer iceCount = database.get(ShinyDexEntryHisuiDAO.class).getRemainingShinyCountInIcelands();
        Integer rareCount = database.get(ShinyDexEntryHisuiDAO.class).getRemainingShinyCountNotInMassive();

        String message = messages.format(REMAINING_COUNT, fieldCount, mireCount, coastCount, highCount, iceCount, rareCount);
        return message;
    }
}
