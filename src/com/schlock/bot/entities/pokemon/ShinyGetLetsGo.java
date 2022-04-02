package com.schlock.bot.entities.pokemon;

import com.schlock.bot.entities.Persisted;

import javax.persistence.*;

@Entity
@Table(name = "shiny_get_letsgo")
public class ShinyGetLetsGo extends Persisted
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ShinyGetType type;

    @Column(name = "shinyNumber")
    private Integer shinyNumber;

    @Column(name = "pokemonId")
    private String pokemonId;

    @Column(name = "timeInMinutes")
    private Integer timeInMinutes;

    @Column(name = "numOfRareChecks")
    private Integer numOfRareChecks;

    public ShinyGetLetsGo()
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

    public ShinyGetType getType()
    {
        return type;
    }

    public void setType(ShinyGetType type)
    {
        this.type = type;
    }

    public Integer getShinyNumber()
    {
        return shinyNumber;
    }

    public void setShinyNumber(Integer shinyNumber)
    {
        this.shinyNumber = shinyNumber;
    }

    public String getPokemonId()
    {
        return pokemonId;
    }

    public void setPokemonId(String pokemonId)
    {
        this.pokemonId = pokemonId;
    }

    public Integer getTimeInMinutes()
    {
        return timeInMinutes;
    }

    public void setTimeInMinutes(Integer timeInMinutes)
    {
        this.timeInMinutes = timeInMinutes;
    }

    public Integer getNumOfRareChecks()
    {
        return numOfRareChecks;
    }

    public void setNumOfRareChecks(Integer numOfRareChecks)
    {
        this.numOfRareChecks = numOfRareChecks;
    }
}
