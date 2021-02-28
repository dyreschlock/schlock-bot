package com.schlock.bot.services.database.apps.impl;

import com.schlock.bot.entities.apps.pokemon.ShinyDexEntry;
import com.schlock.bot.services.database.apps.ShinyDexEntryDAO;
import com.schlock.bot.services.database.impl.BaseDAOImpl;
import org.hibernate.Session;

public class ShinyDexEntryDAOImpl extends BaseDAOImpl<ShinyDexEntry> implements ShinyDexEntryDAO
{
    public  ShinyDexEntryDAOImpl(Session session)
    {
        super(ShinyDexEntry.class, session);
    }
}
