package com.schlock.bot.services.database.pokemon.impl;

import com.schlock.bot.entities.pokemon.ShinyBetHisui;
import com.schlock.bot.services.database.AbstractBaseDAO;
import com.schlock.bot.services.database.pokemon.ShinyBetHisuiDAO;
import org.hibernate.SessionFactory;

public class ShinyBetHisuiDAOImpl extends AbstractBaseDAO<ShinyBetHisui> implements ShinyBetHisuiDAO
{
    public ShinyBetHisuiDAOImpl(SessionFactory sessionFactory)
    {
        super(ShinyBetHisui.class, sessionFactory);
    }
}
