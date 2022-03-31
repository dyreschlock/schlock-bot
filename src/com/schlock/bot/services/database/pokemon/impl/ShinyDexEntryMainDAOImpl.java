package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.ShinyDexEntryMain;
import com.schlock.bot.services.database.AbstractBaseDAO;
import com.schlock.bot.services.database.pokemon.ShinyDexEntryMainDAO;
import org.hibernate.SessionFactory;

public class ShinyDexEntryMainDAOImpl extends AbstractBaseDAO<ShinyDexEntryMain> implements ShinyDexEntryMainDAO
{
    public ShinyDexEntryMainDAOImpl(SessionFactory sessionFactory)
    {
        super(ShinyDexEntryMain.class, sessionFactory);
    }
}
