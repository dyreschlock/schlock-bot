package com.schlock.bot.entities.pokemon;

import com.schlock.bot.entities.Persisted;

import javax.persistence.*;

@Entity
@Table(name = "shiny_hisui_get")
public class ShinyHisuiGet extends Persisted
{
    public static final String WILD = "wild";
    public static final String OUTBREAK = "outbreak";
    public static final String GUARANTEED = "guaranteed";

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "pokemonId")
    private String pokemonId;

    @Column(name = "shinyNumber")
    private Integer shinyNumber;

    @Column(name = "resets")
    private Integer resets;

    @Column(name = "method")
    @Enumerated(EnumType.STRING)
    private ShinyGetType method;


    public boolean isWild()
    {
        return ShinyGetType.WILD.equals(this.method);
    }

    public boolean isOutbreak()
    {
        return ShinyGetType.OUTBREAK.equals(this.method);
    }


    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getPokemonId()
    {
        return pokemonId;
    }

    public void setPokemonId(String pokemonId)
    {
        this.pokemonId = pokemonId;
    }

    public Integer getShinyNumber()
    {
        return shinyNumber;
    }

    public void setShinyNumber(Integer shinyNumber)
    {
        this.shinyNumber = shinyNumber;
    }

    public Integer getResets()
    {
        return resets;
    }

    public void setResets(Integer resets)
    {
        this.resets = resets;
    }

    public ShinyGetType getMethod()
    {
        return method;
    }

    public void setMethod(ShinyGetType method)
    {
        this.method = method;
    }
}
