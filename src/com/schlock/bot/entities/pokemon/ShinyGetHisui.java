package com.schlock.bot.entities.pokemon;

import com.schlock.bot.entities.Persisted;

import javax.persistence.*;

@Entity
@Table(name = "shiny_get_hisui")
public class ShinyGetHisui extends Persisted
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

    @Column(name = "alphaNumber")
    private Integer alphaNumber;

    @Column(name = "resets")
    private Integer resets;

    @Column(name = "method")
    @Enumerated(EnumType.STRING)
    private ShinyGetType method;

    private Integer hisuiNumber;


    public Integer getHisuiNumber()
    {
        return hisuiNumber;
    }

    public void setHisuiNumber(Integer hisuiNumber)
    {
        this.hisuiNumber = hisuiNumber;
    }

    public String getHisuiNumberString()
    {
        String num = Integer.toString(hisuiNumber);
        while (num.length() < 3)
        {
            num = "0" + num;
        }
        return num;
    }


    public boolean isAlpha()
    {
        return alphaNumber != null;
    }

    public boolean isWild()
    {
        return ShinyGetType.WILD.equals(this.method);
    }

    public boolean isOutbreak()
    {
        return ShinyGetType.OUTBREAK.equals(this.method);
    }

    public boolean isMassiveOutbreak()
    {
        return ShinyGetType.MASSIVE.equals(this.method);
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

    public Integer getAlphaNumber()
    {
        return alphaNumber;
    }

    public void setAlphaNumber(Integer alphaNumber)
    {
        this.alphaNumber = alphaNumber;
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
