package com.schlock.bot.entities.apps.bet;

import com.schlock.bot.entities.Persisted;
import com.schlock.bot.entities.apps.User;

import javax.persistence.*;

@Entity
@Table(name = "shiny_bet")
public class ShinyBet extends Persisted
{
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",
                    foreignKey = @ForeignKey(name = "USER_ID_FK"))
    private User user;

    @Column(name = "pokemonId")
    private String pokemonId;

    @Column(name = "timeMinutes")
    private Integer timeMinutes;

    public ShinyBet()
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

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
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
