package com.schlock.bot.entities.pokemon;

import com.schlock.bot.entities.Persisted;

import javax.persistence.*;

@Entity
@Table(name = "rate_shiny_pokemon")
public class PokemonShinyRating extends Persisted
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String pokemonId;

    @Column(name = "numCode")
    private String numberCode;

    @Column(name = "rating")
    private Double rating;


    public PokemonShinyRating()
    {

    }

    public PokemonShinyRating(Pokemon pokemon, Double rating)
    {
        this.pokemonId = pokemon.getId();
        this.numberCode = pokemon.getNumberString();
        this.rating = rating;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public String getPokemonId()
    {
        return pokemonId;
    }

    public void setPokemonId(String pokemonId)
    {
        this.pokemonId = pokemonId;
    }

    public String getNumberCode()
    {
        return numberCode;
    }

    public void setNumberCode(String numberCode)
    {
        this.numberCode = numberCode;
    }

    public Double getRating()
    {
        return rating;
    }

    public void setRating(Double rating)
    {
        this.rating = rating;
    }
}
