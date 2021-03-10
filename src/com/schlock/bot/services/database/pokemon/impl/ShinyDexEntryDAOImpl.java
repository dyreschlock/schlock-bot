package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.ShinyDexEntry;
import com.schlock.bot.services.database.pokemon.ShinyDexEntryDAO;
import com.schlock.bot.services.database.AbstractBaseDAO;
import org.hibernate.Session;

public class ShinyDexEntryDAOImpl extends AbstractBaseDAO<ShinyDexEntry> implements ShinyDexEntryDAO
{
    public  ShinyDexEntryDAOImpl(Session session)
    {
        super(ShinyDexEntry.class, session);
    }
}
