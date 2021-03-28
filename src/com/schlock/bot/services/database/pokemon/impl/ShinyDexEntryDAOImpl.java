package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.ShinyDexEntry;
import com.schlock.bot.services.database.AbstractBaseDAO;
import com.schlock.bot.services.database.pokemon.ShinyDexEntryDAO;
import org.hibernate.SessionFactory;

public class ShinyDexEntryDAOImpl extends AbstractBaseDAO<ShinyDexEntry> implements ShinyDexEntryDAO
{
    public  ShinyDexEntryDAOImpl(SessionFactory sessionFactory)
    {
        super(ShinyDexEntry.class, sessionFactory);
    }
}
