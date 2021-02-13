package com.schlock.bot.services.database.apps.impl;

import com.schlock.bot.entities.apps.bet.Bet;
import com.schlock.bot.services.database.impl.BaseDAOImpl;
import com.schlock.bot.services.database.apps.BetDAO;
import org.hibernate.SessionFactory;

public class BetDAOImpl extends BaseDAOImpl<Bet> implements BetDAO
{
    public BetDAOImpl(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }
}
