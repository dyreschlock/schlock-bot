package com.schlock.bot.services.commands.pokemon.shiny.impl;

import com.schlock.bot.entities.pokemon.*;
import com.schlock.bot.services.commands.AbstractListenerService;
import com.schlock.bot.services.commands.ListenerResponse;
import com.schlock.bot.services.commands.pokemon.shiny.ShinyDexService;
import com.schlock.bot.services.database.adhoc.DatabaseManager;
import com.schlock.bot.services.database.pokemon.ShinyDexEntryDAO;
import com.schlock.bot.services.database.pokemon.ShinyDexEntryGoDAO;
import com.schlock.bot.services.database.pokemon.ShinyDexEntryHisuiDAO;
import com.schlock.bot.services.database.pokemon.ShinyHisuiGetDAO;
import com.schlock.bot.services.entities.pokemon.PokemonManagement;
import org.apache.tapestry5.ioc.Messages;

import java.util.ArrayList;
import java.util.List;

public class ShinyDexServiceImpl extends AbstractListenerService implements ShinyDexService
{
    protected static final String CURRENT_SHINY_DEX_KEY = "current-shiny-dex";

    private static final String SHINY_DEX_COMMAND = "!shinydex";

    private final PokemonManagement pokemonManagement;

    private DatabaseManager database;

    public ShinyDexServiceImpl(PokemonManagement pokemonManagement,
                               DatabaseManager database,
                               Messages messages)
    {
        super(messages);

        this.pokemonManagement = pokemonManagement;

        this.database = database;
    }

    public List<Pokemon> getShinyDexEntries()
    {
        List<ShinyDexEntry> entries = database.get(ShinyDexEntryDAO.class).getAll();

        List<Pokemon> pokemon = new ArrayList<>();
        for (ShinyDexEntry entry : entries)
        {
            Pokemon p = pokemonManagement.getPokemonFromText(entry.getPokemon());
            if (p != null)
            {
                pokemon.add(p);
            }
        }
        return pokemon;
    }

    public List<ShinyDexEntryHisui> getShinyDexHisuiEntries()
    {
        List<ShinyDexEntryHisui> entries = database.get(ShinyDexEntryHisuiDAO.class).getAll();
        return entries;
    }

    public List<Pokemon> getShinyDexHisuiGets()
    {
        List<ShinyHisuiGet> entries = database.get(ShinyHisuiGetDAO.class).getAll();

        List<Pokemon> pokemon = new ArrayList<>();
        for (ShinyHisuiGet entry : entries)
        {
            Pokemon p = pokemonManagement.getPokemonFromText(entry.getPokemonId());
            if (p != null)
            {
                pokemon.add(p);
            }
        }
        return pokemon;
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
            List<ShinyDexEntry> entries = database.get(ShinyDexEntryDAO.class).getAll();

            Integer count = entries.size();

            return formatSingleResponse(CURRENT_SHINY_DEX_KEY, count.toString());
        }
        return nullResponse();
    }


    private static final String FIELD_MSG = "hisui-remaining-field";
    private static final String MIRE_MSG = "hisui-remaining-mire";
    private static final String COAST_MSG = "hisui-remaining-coast";
    private static final String HIGH_MSG = "hisui-remaining-high";
    private static final String ICE_MSG = "hisui-remaining-ice";
    private static final String RARE_MSG = "hisui-remaining-rare";

    public String getHisuiRemainingCountsMessage()
    {
        Integer fieldCount = database.get(ShinyDexEntryHisuiDAO.class).getRemainingShinyCountInFieldlands();
        String fieldMessage = messages.format(FIELD_MSG, fieldCount);

        Integer mireCount = database.get(ShinyDexEntryHisuiDAO.class).getRemainingShinyCountInMirelands();
        String mireMessage = messages.format(MIRE_MSG, mireCount);

        Integer coastCount = database.get(ShinyDexEntryHisuiDAO.class).getRemainingShinyCountInCoastlands();
        String coastMessage = messages.format(COAST_MSG, coastCount);

        Integer highCount = database.get(ShinyDexEntryHisuiDAO.class).getRemainingShinyCountInHighlands();
        String highMessage = messages.format(HIGH_MSG, highCount);

        Integer iceCount = database.get(ShinyDexEntryHisuiDAO.class).getRemainingShinyCountInIcelands();
        String iceMessage = messages.format(ICE_MSG, iceCount);

        Integer rareCount = database.get(ShinyDexEntryHisuiDAO.class).getRemainingShinyCountNotInMassive();
        String rareMessage = messages.format(RARE_MSG, rareCount);


        return fieldMessage + " / " +
                mireMessage + " / " +
                coastMessage + " / " +
                highMessage + " / " +
                iceMessage + " / " +
                rareMessage;
    }
}
