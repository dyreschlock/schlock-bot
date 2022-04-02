package com.schlock.bot.entities.pokemon;

import com.schlock.bot.entities.base.User;

import javax.persistence.*;

@Entity
@Table(name = "shiny_bet_hisui")
public class ShinyBetHisui
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
    private ShinyGetHisui shiny;

    @Column(name = "numChecks")
    private Integer numberOfChecks;

    @Column(name = "betAmount")
    private Integer betAmount;

    @Column(name = "pokemonId")
    private String pokemonId;

    public ShinyBetHisui()
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

    public ShinyGetHisui getShiny()
    {
        return shiny;
    }

    public void setShiny(ShinyGetHisui shiny)
    {
        this.shiny = shiny;
    }

    public Integer getNumberOfChecks()
    {
        return numberOfChecks;
    }

    public void setNumberOfChecks(Integer numberOfChecks)
    {
        this.numberOfChecks = numberOfChecks;
    }

    public Integer getBetAmount()
    {
        return betAmount;
    }

    public void setBetAmount(Integer betAmount)
    {
        this.betAmount = betAmount;
    }

    public String getPokemonId()
    {
        return pokemonId;
    }

    public void setPokemonId(String pokemonId)
    {
        this.pokemonId = pokemonId;
    }
}
