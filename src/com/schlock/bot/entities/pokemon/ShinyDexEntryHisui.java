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

    @Column(name = "inMassiveFieldlands")
    private Boolean inMassiveFieldlands;

    @Column(name = "isMassiveMirelands")
    private Boolean isMassiveMirelands;

    @Column(name = "isMassiveCoastlands")
    private Boolean isMassiveCoastlands;

    @Column(name = "isMassiveHighlands")
    private Boolean isMassiveHighlands;

    @Column(name = "isMassiveIcelands")
    private Boolean isMassiveIcelands;


    @Column(name = "inOutbreakFieldlands")
    private Boolean inOutbreakFieldlands;

    @Column(name = "isOutbreakMirelands")
    private Boolean isOutbreakMirelands;

    @Column(name = "isOutbreakCoastlands")
    private Boolean isOutbreakCoastlands;

    @Column(name = "isOutbreakHighlands")
    private Boolean isOutbreakHighlands;

    @Column(name = "isOutbreakIcelands")
    private Boolean isOutbreakIcelands;


    public ShinyDexEntryHisui()
    {
    }

    public String getNumberString()
    {
        String num = Integer.toString(pokemonNumber);
        while (num.length() < 3)
        {
            num = "0" + num;
        }
        return num;
    }

    public boolean isInSomeOutbreak()
    {
        return this.inMassiveFieldlands ||
                this.isMassiveMirelands ||
                this.isMassiveCoastlands ||
                this.isMassiveHighlands ||
                this.isMassiveIcelands ||
                this.inOutbreakFieldlands ||
                this.isOutbreakMirelands ||
                this.isOutbreakCoastlands ||
                this.isOutbreakHighlands ||
                this.isOutbreakIcelands;
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

    public Boolean isHaveShiny()
    {
        return haveShiny;
    }

    public void setHaveShiny(Boolean haveShiny)
    {
        this.haveShiny = haveShiny;
    }

    public Boolean getInMassiveFieldlands()
    {
        return inMassiveFieldlands;
    }

    public void setInMassiveFieldlands(Boolean inMassiveFieldlands)
    {
        this.inMassiveFieldlands = inMassiveFieldlands;
    }

    public Boolean getMassiveMirelands()
    {
        return isMassiveMirelands;
    }

    public void setMassiveMirelands(Boolean massiveMirelands)
    {
        isMassiveMirelands = massiveMirelands;
    }

    public Boolean getMassiveCoastlands()
    {
        return isMassiveCoastlands;
    }

    public void setMassiveCoastlands(Boolean massiveCoastlands)
    {
        isMassiveCoastlands = massiveCoastlands;
    }

    public Boolean getMassiveHighlands()
    {
        return isMassiveHighlands;
    }

    public void setMassiveHighlands(Boolean massiveHighlands)
    {
        isMassiveHighlands = massiveHighlands;
    }

    public Boolean getMassiveIcelands()
    {
        return isMassiveIcelands;
    }

    public void setMassiveIcelands(Boolean massiveIcelands)
    {
        isMassiveIcelands = massiveIcelands;
    }

    public Boolean getInOutbreakFieldlands()
    {
        return inOutbreakFieldlands;
    }

    public void setInOutbreakFieldlands(Boolean inOutbreakFieldlands)
    {
        this.inOutbreakFieldlands = inOutbreakFieldlands;
    }

    public Boolean getOutbreakMirelands()
    {
        return isOutbreakMirelands;
    }

    public void setOutbreakMirelands(Boolean outbreakMirelands)
    {
        isOutbreakMirelands = outbreakMirelands;
    }

    public Boolean getOutbreakCoastlands()
    {
        return isOutbreakCoastlands;
    }

    public void setOutbreakCoastlands(Boolean outbreakCoastlands)
    {
        isOutbreakCoastlands = outbreakCoastlands;
    }

    public Boolean getOutbreakHighlands()
    {
        return isOutbreakHighlands;
    }

    public void setOutbreakHighlands(Boolean outbreakHighlands)
    {
        isOutbreakHighlands = outbreakHighlands;
    }

    public Boolean getOutbreakIcelands()
    {
        return isOutbreakIcelands;
    }

    public void setOutbreakIcelands(Boolean outbreakIcelands)
    {
        isOutbreakIcelands = outbreakIcelands;
    }
}
