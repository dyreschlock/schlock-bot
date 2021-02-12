package com.schlock.bot.entities.bet;

import com.schlock.bot.entities.Persisted;

import javax.persistence.*;

@Entity(name = "bet")
public class Bet extends Persisted
{
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",
                    foreignKey = @ForeignKey(name = "BETTING_USER_ID_FK"))
    private BettingUser user;

    @Column(name = "pokemonId")
    private String pokemonId;

    @Column(name = "timeMinutes")
    private Integer timeMinutes;

    public Bet()
    {
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
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
