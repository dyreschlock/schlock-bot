package com.schlock.bot.entities.pokemon;

import com.schlock.bot.entities.Persisted;
import com.schlock.bot.entities.base.User;

import javax.persistence.*;

@Entity
@Table(name = "shiny_bet_letsgo")
public class ShinyBetLetsGo extends Persisted
{
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",
                    foreignKey = @ForeignKey(name = "USER_ID_FK"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "shiny",
                    foreignKey = @ForeignKey(name = "SHINY_GET_ID_FK"))
    private ShinyGetLetsGo shiny;

    @Column(name = "pokemonId")
    private String pokemonId;

    @Column(name = "timeMinutes")
    private Integer timeMinutes;

    @Column(name = "betAmount")
    private Integer betAmount;

    public ShinyBetLetsGo()
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

    public ShinyGetLetsGo getShiny()
    {
        return shiny;
    }

    public void setShiny(ShinyGetLetsGo shiny)
    {
        this.shiny = shiny;
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

    public Integer getBetAmount()
    {
        return betAmount;
    }

    public void setBetAmount(Integer betAmount)
    {
        this.betAmount = betAmount;
    }
}
