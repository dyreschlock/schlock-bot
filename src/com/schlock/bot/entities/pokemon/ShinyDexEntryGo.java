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

    @Column(name = "shiny_go")
    private Boolean shinyGo;

    @Column(name = "shiny_home")
    private Boolean shinyHome;

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

    public Boolean isShinyGo()
    {
        return shinyGo;
    }

    public void setShinyGo(Boolean shinyGo)
    {
        this.shinyGo = shinyGo;
    }

    public Boolean isShinyHome()
    {
        return shinyHome;
    }

    public void setShinyHome(Boolean shinyHome)
    {
        this.shinyHome = shinyHome;
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
