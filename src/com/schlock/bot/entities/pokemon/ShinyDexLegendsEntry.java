package com.schlock.bot.entities.pokemon;

import com.schlock.bot.entities.Persisted;

import javax.persistence.*;

@Entity
@Table(name = "dex_entry_legends")
public class ShinyDexLegendsEntry extends Persisted
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "pokemon")
    private String pokemon;

    @Column(name = "number")
    private Integer number;

    @Column(name = "resets")
    private Integer resets;

    @Column(name = "method")
    private String method;

    public String getNumberString()
    {
        String num = Integer.toString(number);
        while (num.length() < 3)
        {
            num = "0" + num;
        }
        return num;
    }


    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getPokemon()
    {
        return pokemon;
    }

    public void setPokemon(String pokemon)
    {
        this.pokemon = pokemon;
    }

    public Integer getNumber()
    {
        return number;
    }

    public void setNumber(Integer number)
    {
        this.number = number;
    }

    public Integer getResets()
    {
        return resets;
    }

    public void setResets(Integer resets)
    {
        this.resets = resets;
    }

    public String getMethod()
    {
        return method;
    }

    public void setMethod(String method)
    {
        this.method = method;
    }
}
