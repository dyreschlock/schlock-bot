package com.schlock.bot.entities.pokemon;

import javax.persistence.*;

@Entity
@Table(name = "dex_entry_go")
public class ShinyDexEntryGo extends AbstractShinyDexEntry
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "numCode")
    private String numberCode;

    @Column(name = "name")
    private String pokemonId;

    @Column(name = "have")
    private Boolean have;

    @Column(name = "s_go_first")
    private Boolean shinyGoFirst;

    @Column(name = "s_go_second")
    private Boolean shinyGoSecond;

    @Column(name = "s_home_own")
    private Boolean shinyHomeOwn;

    @Column(name = "s_home_other")
    private Boolean shinyHomeOther;

    @Column(name = "google_id")
    private String googlePhotoId;

    public ShinyDexEntryGo()
    {
    }

    public String getGoogleImageLink()
    {
        String link = "";
        if(googlePhotoId != null)
        {
            link = "https://drive.google.com/thumbnail?id=" + googlePhotoId;
        }
        return link;
    }

    public boolean isShinyAtAll()
    {
        return isShinyGoFirst() || isShinyGoSecond() || isShinyHomeOwn() || isShinyHomeOther();
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

    public Boolean isHave()
    {
        return have;
    }

    public void setHave(Boolean have)
    {
        this.have = have;
    }

    public Boolean isShinyGoFirst()
    {
        return shinyGoFirst;
    }

    public void setShinyGoFirst(Boolean shinyGoFirst)
    {
        this.shinyGoFirst = shinyGoFirst;
    }

    public Boolean isShinyGoSecond()
    {
        return shinyGoSecond;
    }

    public void setShinyGoSecond(Boolean shinyGoSecond)
    {
        this.shinyGoSecond = shinyGoSecond;
    }

    public Boolean isShinyHomeOwn()
    {
        return shinyHomeOwn;
    }

    public void setShinyHomeOwn(Boolean shinyHomeOwn)
    {
        this.shinyHomeOwn = shinyHomeOwn;
    }

    public Boolean isShinyHomeOther()
    {
        return shinyHomeOther;
    }

    public void setShinyHomeOther(Boolean shinyHomeOther)
    {
        this.shinyHomeOther = shinyHomeOther;
    }

    public String getGooglePhotoId()
    {
        return googlePhotoId;
    }

    public void setGooglePhotoId(String googlePhotoId)
    {
        this.googlePhotoId = googlePhotoId;
    }
}
