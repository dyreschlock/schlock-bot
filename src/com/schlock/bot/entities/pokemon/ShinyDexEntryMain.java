package com.schlock.bot.entities.pokemon;

import com.schlock.bot.entities.Persisted;

import javax.persistence.Entity;

import javax.persistence.*;

@Entity
@Table(name = "dex_entry_main")
public class ShinyDexEntryMain extends Persisted
{
    private static final String ALOLA_MARK = "a";
    private static final String GALAR_MARK = "g";
    private static final String HISUI_MARK = "h";

    private static final String DELIM = "_";

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "numberCode")
    private String numberCode;

    @Column(name = "name")
    private String pokemonId;

    @Column(name = "caught")
    private Boolean caught;

    public ShinyDexEntryMain()
    {
    }

    public Integer getNumber()
    {
        String[] parts = numberCode.split(DELIM);

        String number = parts[0];
        if (parts.length == 2)
        {
            number = parts[1];
        }
        return Integer.parseInt(number);
    }

    public boolean isAlola()
    {
        return ALOLA_MARK.equals(getMark());
    }

    public boolean isGalar()
    {
        return GALAR_MARK.equals(getMark());
    }

    public boolean isHisui()
    {
        return HISUI_MARK.equals(getMark());
    }

    public boolean isNormal()
    {
        return getMark() == null;
    }

    private String getMark()
    {
        String[] parts = numberCode.split(DELIM);
        if (parts.length == 1)
        {
            return null;
        }
        return parts[0];
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
