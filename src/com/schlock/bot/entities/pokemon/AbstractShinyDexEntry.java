package com.schlock.bot.entities.pokemon;

import com.schlock.bot.entities.Persisted;

public abstract class AbstractShinyDexEntry extends Persisted
{
    public abstract String getNumberCode();

    public abstract void setNumberCode(String numberCode);

    public abstract String getPokemonId();

    public abstract void setPokemonId(String pokemonId);
}
