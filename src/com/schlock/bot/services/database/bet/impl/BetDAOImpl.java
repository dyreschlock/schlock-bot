package com.schlock.bot.services.database.bet.impl;

import com.schlock.bot.entities.bet.Bet;
import com.schlock.bot.services.database.BaseDAOImpl;
import com.schlock.bot.services.database.bet.BetDAO;
import org.hibernate.SessionFactory;

public class BetDAOImpl extends BaseDAOImpl<Bet> implements BetDAO
{
    public BetDAOImpl(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }
}
