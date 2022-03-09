package com.schlock.bot.entities.pokemon;

import com.schlock.bot.entities.Persisted;

import javax.persistence.*;

@Entity
@Table(name = "dex_entry_hisui")
public class ShinyDexEntryHisui extends Persisted
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "number")
    private Integer pokemonNumber;

    @Column(name = "name")
    private String pokemonId;

    @Column(name = "haveShiny")
    private Boolean haveShiny;

    public ShinyDexEntryHisui()
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

    public Integer getPokemonNumber()
    {
        return pokemonNumber;
    }

    public void setPokemonNumber(Integer pokemonNumber)
    {
        this.pokemonNumber = pokemonNumber;
    }

    public String getPokemonId()
    {
        return pokemonId;
    }

    public void setPokemonId(String pokemonId)
    {
        this.pokemonId = pokemonId;
    }

    public Boolean isHaveSHiny()
    {
        return haveShiny;
    }

    public void setHaveShiny(Boolean haveShiny)
    {
        this.haveShiny = haveShiny;
    }
}
