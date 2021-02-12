package com.schlock.bot.services.database.bet;

import com.schlock.bot.entities.bet.BettingUser;

public interface BettingUserDAO
{
    BettingUser getByUsername(String username);
}
