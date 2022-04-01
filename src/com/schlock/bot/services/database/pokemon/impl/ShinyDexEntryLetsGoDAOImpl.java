package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.ShinyDexEntryLetsGo;
import com.schlock.bot.services.database.AbstractBaseDAO;
import com.schlock.bot.services.database.pokemon.ShinyDexEntryLetsGoDAO;
import org.hibernate.SessionFactory;

public class ShinyDexEntryLetsGoDAOImpl extends AbstractBaseDAO<ShinyDexEntryLetsGo> implements ShinyDexEntryLetsGoDAO
{
    public ShinyDexEntryLetsGoDAOImpl(SessionFactory sessionFactory)
    {
        super(ShinyDexEntryLetsGo.class, sessionFactory);
    }
}
