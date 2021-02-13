package com.schlock.bot.services.database.bet;

import com.schlock.bot.entities.bet.BettingUser;
import com.schlock.bot.services.database.BaseDAO;

public interface BettingUserDAO extends BaseDAO<BettingUser>
{
    BettingUser getByUsername(String username);
}
