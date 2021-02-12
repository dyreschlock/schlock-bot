package com.schlock.bot.entities.bet;

public class BettingUser
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
