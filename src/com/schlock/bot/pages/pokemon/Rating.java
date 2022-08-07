package com.schlock.bot.pages.pokemon;

import com.schlock.bot.entities.pokemon.Pokemon;
import com.schlock.bot.services.commands.pokemon.quiz.RatePokemonService;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Rating
{
    @Inject
    private RatePokemonService ratingService;


    public boolean isStarted()
    {
        return ratingService.isStarted();
    }

    public String getImgSrc()
    {
        Pokemon currentPokemon = ratingService.getCurrentPokemon();
        String number = currentPokemon.getNumberString();

        return "/img/pokemon/" + number + ".png";
    }
}
