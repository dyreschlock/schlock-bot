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

        int normal = getNormalShinyCount();
        int alola = getRegionShinyCount(PokemonRegion.ALOLA);
        int galar = getRegionShinyCount(PokemonRegion.GALAR);
        int hisui = getRegionShinyCount(PokemonRegion.HISUI);

        int total = normal + alola + galar + hisui;
        return total;
    }

    public int getNormalShinyCount()
    {
        initialize();

        int count = 0;
        for (String key : shinyChecklist.keySet())
        {
            Pokemon poke = shinyChecklist.get(key);
            if(PokemonRegion.isNormalNumberCode(key) && poke.isShiny())
            {
                count++;
            }
        }
        return count;
    }

    public int getRegionShinyCount(PokemonRegion region)
    {
        int count = 0;
        for (Pokemon poke : pokemonManagement.getAllPokemonInRegion(region))
        {
            if (PokemonRegion.ALOLA.equals(region) && poke.isShinyAlola())
            {
                count++;
            }
            if (PokemonRegion.GALAR.equals(region) && poke.isShinyGalar())
            {
                count++;
            }
            if (PokemonRegion.HISUI.equals(region) && poke.isShinyHisui())
            {
                count++;
            }
        }
        return count;
    }

    public int getOverallTotalCount()
    {
        initialize();

        int normal = getNormalTotalCount();
        int alola = getRegionTotalCount(PokemonRegion.ALOLA);
        int galar = getRegionTotalCount(PokemonRegion.GALAR);
        int hisui = getRegionTotalCount(PokemonRegion.HISUI);

        int total = normal + alola + galar + hisui;
        return total;
    }

    public int getNormalTotalCount()
    {
        return pokemonManagement.getAllPokemonInNumberOrder().size();
    }

    public int getRegionTotalCount(PokemonRegion region)
    {
        return region.pokemonNumbers().size();
    }

    public boolean isHaveShiny(String pokemonNumberCode)
    {
        initialize();

        Pokemon poke = shinyChecklist.get(pokemonNumberCode);
        if (PokemonRegion.isRegionalNumberCode(pokemonNumberCode, PokemonRegion.ALOLA))
        {
            return poke.isShinyAlola();
        }
        if (PokemonRegion.isRegionalNumberCode(pokemonNumberCode, PokemonRegion.GALAR))
        {
            return poke.isShinyGalar();
        }
        if (PokemonRegion.isRegionalNumberCode(pokemonNumberCode, PokemonRegion.HISUI))
        {
            return poke.isShinyHisui();
        }
        if (PokemonRegion.isRegionalNumberCode(pokemonNumberCode, PokemonRegion.PALDEA))
        {
            return poke.isShinyPaldea();
        }
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

            addAllPokemonInRegion(PokemonRegion.ALOLA);
            addAllPokemonInRegion(PokemonRegion.GALAR);
            addAllPokemonInRegion(PokemonRegion.HISUI);
            addAllPokemonInRegion(PokemonRegion.PALDEA);

            checkLetsGoShinies();
            checkGoShinies();
            checkHisuiShinies();
            checkMainSeriesShinies();
        }
    }

    private void addAllPokemonInRegion(PokemonRegion region)
    {
        List<Pokemon> pokemon = pokemonManagement.getAllPokemonInRegion(region);
        for (Pokemon poke : pokemon)
        {
            String numberCode = region.prefix() + poke.getNumberString();
            shinyChecklist.put(numberCode, poke);
        }
    }

    private void checkLetsGoShinies()
    {
        for(ShinyDexEntryLetsGo entry : getShinyDexLetsGoEntries())
        {
            updateShinyChecklist(entry);
        }
    }

    private void checkGoShinies()
    {
        for (ShinyDexEntryGo entry : getPokemonGoEntries())
        {
            if (entry.isShinyAtAll())
            {
                updateShinyChecklist(entry);
            }
        }
    }

    private void checkHisuiShinies()
    {
        for (ShinyDexEntryHisui entry : getShinyDexHisuiEntries())
        {
            if (entry.isHaveShiny())
            {
                updateShinyChecklist(entry);
            }
        }
    }

    private void checkMainSeriesShinies()
    {
        for (ShinyDexEntryMain entry : getShinyDexMainEntries())
        {
            updateShinyChecklist(entry);
        }
    }

    private void updateShinyChecklist(AbstractShinyDexEntry entry)
    {
        String number = entry.getNumberCode();
        Pokemon poke = shinyChecklist.get(number);

        if (PokemonRegion.isNormalNumberCode(number))
        {
            poke.setShiny(true);
        }
        else if (PokemonRegion.isRegionalNumberCode(number, PokemonRegion.ALOLA))
        {
            poke.setShinyAlola(true);
        }
        else if (PokemonRegion.isRegionalNumberCode(number, PokemonRegion.GALAR))
        {
            poke.setShinyGalar(true);
        }
        else if (PokemonRegion.isRegionalNumberCode(number, PokemonRegion.HISUI))
        {
            poke.setShinyHisui(true);
        }
        else if (PokemonRegion.isRegionalNumberCode(number, PokemonRegion.PALDEA))
        {
            poke.setShinyPaldea(true);
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

    public ShinyDexEntryHisui getShinyDexHisuiEntryByPokemonId(String pokemonId)
    {
        ShinyDexEntryHisui entry = database.get(ShinyDexEntryHisuiDAO.class).getEntryByPokemonId(pokemonId);
        return entry;
    }

    public List<ShinyGetHisui> getShinyDexHisuiGets()
    {
        List<ShinyGetHisui> gets = database.get(ShinyGetHisuiDAO.class).getAll();
        for (ShinyGetHisui get : gets)
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
