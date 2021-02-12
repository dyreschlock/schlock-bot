package com.schlock.bot.services.database.bet.impl;

import com.schlock.bot.entities.bet.BettingUser;
import com.schlock.bot.services.database.BaseDAOImpl;
import com.schlock.bot.services.database.bet.BettingUserDAO;
import org.hibernate.SessionFactory;

public class BettingUserDAOImpl extends BaseDAOImpl<BettingUser> implements BettingUserDAO
{
    public BettingUserDAOImpl(SessionFactory sessionFactory)
    {
        super(sessionFactory);
    }

    public BettingUser getByUsername(String username)
    {
        return null;
    }
}
