package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.ShinyDexLegendsEntry;
import com.schlock.bot.services.database.AbstractBaseDAO;
import com.schlock.bot.services.database.pokemon.ShinyDexLegendsEntryDAO;
import org.hibernate.SessionFactory;

public class ShinyDexLegendsEntryDAOImpl extends AbstractBaseDAO<ShinyDexLegendsEntry> implements ShinyDexLegendsEntryDAO
{
    public ShinyDexLegendsEntryDAOImpl(SessionFactory sessionFactory)
    {
        super(ShinyDexLegendsEntry.class, sessionFactory);
    }
}
