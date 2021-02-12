package com.schlock.bot.entities.bet;

import com.schlock.bot.entities.Persisted;

import javax.persistence.Entity;

@Entity(name = "bet")
public class Bet extends Persisted
{
    private BettingUser user;

    private String pokemonId;

    private Integer timeMinutes;

    public Bet()
    {
    }

    public BettingUser getUser()
    {
        return user;
    }

    public void setUser(BettingUser user)
    {
        this.user = user;
    }

    public String getPokemonId()
    {
        return pokemonId;
    }

    public void setPokemonId(String pokemonId)
    {
        this.pokemonId = pokemonId;
    }

    public Integer getTimeMinutes()
    {
        return timeMinutes;
    }

    public void setTimeMinutes(Integer timeMinutes)
    {
        this.timeMinutes = timeMinutes;
    }
}
