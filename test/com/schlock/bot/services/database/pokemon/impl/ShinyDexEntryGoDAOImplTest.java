package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.ShinyDexEntryGo;
import com.schlock.bot.services.database.DatabaseTest;
import com.schlock.bot.services.database.pokemon.ShinyDexEntryGoDAO;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShinyDexEntryGoDAOImplTest extends DatabaseTest
{
    private static final String NAME1 = "BULBASAUR";
    private static final String NAME2 = "IVYSAUR";
    private static final String NAME3 = "VENUSAUR";

    private ShinyDexEntryGoDAO entryDao;

    private ShinyDexEntryGo entry1;
    private ShinyDexEntryGo entry2;
    private ShinyDexEntryGo entry3;

    @Test
    public void testGetInOrder()
    {
        List<ShinyDexEntryGo> entries = entryDao.getInPokemonOrder();

        assertEquals(3, entries.size());

        assertEquals(NAME1, entries.get(0).getPokemonName());
        assertEquals(NAME2, entries.get(1).getPokemonName());
        assertEquals(NAME3, entries.get(2).getPokemonName());
    }

    @Override
    protected void before() throws Exception
    {
        entryDao = database().get(ShinyDexEntryGoDAO.class);

        createTestObjects();
    }

    @Override
    protected void after() throws Exception
    {
        removeTestObjects();
    }

    public void createTestObjects()
    {
        entry1 = new ShinyDexEntryGo();
        entry1.setPokemonNumber(1);
        entry1.setPokemonName(NAME1);
        entry1.setHave(true);
        entry1.setShinyGo(true);
        entry1.setShinyHome(true);

        entry2 = new ShinyDexEntryGo();
        entry2.setPokemonNumber(2);
        entry2.setPokemonName(NAME2);
        entry2.setHave(true);
        entry2.setShinyGo(true);
        entry2.setShinyHome(true);

        entry3 = new ShinyDexEntryGo();
        entry3.setPokemonNumber(3);
        entry3.setPokemonName(NAME3);
        entry3.setHave(true);
        entry3.setShinyGo(true);
        entry3.setShinyHome(true);

        database().save(entry1, entry2, entry3);
    }

    public void removeTestObjects()
    {
        database().delete(entry1, entry2, entry3);
    }
}