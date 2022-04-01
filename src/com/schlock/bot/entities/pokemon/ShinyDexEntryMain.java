package com.schlock.bot.entities.pokemon;

import javax.persistence.*;

@Entity
@Table(name = "dex_entry_main")
public class ShinyDexEntryMain extends AbstractShinyDexEntry
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "numCode")
    private String numberCode;

    @Column(name = "name")
    private String pokemonId;

    @Column(name = "caught")
    private Boolean caught;

    public ShinyDexEntryMain()
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

    public Boolean isCaught()
    {
        return caught;
    }

    public void setCaught(Boolean caught)
    {
        this.caught = caught;
    }
}
