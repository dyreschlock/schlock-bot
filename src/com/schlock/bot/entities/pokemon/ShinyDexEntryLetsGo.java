package com.schlock.bot.entities.pokemon;

import javax.persistence.*;

@Entity
@Table(name = "dex_entry_letsgo")
public class ShinyDexEntryLetsGo extends AbstractShinyDexEntry
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "numCode")
    private String numberCode;

    @Column(name = "name")
    private String pokemonId;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getNumberCode()
    {
        return numberCode;
    }

    public void setNumberCode(String numberCode)
    {
        this.numberCode = numberCode;
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
