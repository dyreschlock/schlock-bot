package com.schlock.bot.entities.bet;

import com.schlock.bot.entities.Persisted;

import javax.persistence.Entity;

@Entity(name = "betting_user")
public class BettingUser extends Persisted
{
    private String username;

    private Integer balance;

    public BettingUser()
    {
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public Integer getBalance()
    {
        return balance;
    }

    public void setBalance(Integer balance)
    {
        this.balance = balance;
    }
}
