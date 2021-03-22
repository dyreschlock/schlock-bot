package com.schlock.bot.entities.pokemon;

import com.schlock.bot.entities.Persisted;

import javax.persistence.*;

@Entity
@Table(name = "go_shiny_dex")
public class PokemonGoDexEntry extends Persisted
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "number")
    private Integer pokemonNumber;

    @Column(name = "name")
    private String pokemonName;

    @Column(name = "have")
    private Boolean have;

    @Column(name = "shiny_go")
    private Boolean shinyGo;

    @Column(name = "shiny_home")
    private Boolean shinyHome;

    @Column(name = "google_id")
    private String googlePhotoId;

    public PokemonGoDexEntry()
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

    public String getPokemonNumberText()
    {
        String number = getPokemonNumber().toString();
        if (number.length() == 1)
        {
            number = "00" + number;
        }
        if (number.length() == 2)
        {
            number = "0" + number;
        }
        return number;
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

    public String getPokemonName()
    {
        return pokemonName;
    }

    public void setPokemonName(String pokemonName)
    {
        this.pokemonName = pokemonName;
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
