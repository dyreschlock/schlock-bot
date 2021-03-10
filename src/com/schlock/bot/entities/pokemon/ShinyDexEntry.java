package com.schlock.bot.entities.pokemon;

import com.schlock.bot.entities.Persisted;

import javax.persistence.*;

@Entity
@Table(name = "dex_entry")
public class ShinyDexEntry extends Persisted
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "pokemon")
    private String pokemon;

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
}
