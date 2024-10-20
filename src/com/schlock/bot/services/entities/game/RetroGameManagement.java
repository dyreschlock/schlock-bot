package com.schlock.bot.services.entities.game;

import com.schlock.bot.entities.game.PocketGame;

import java.util.List;

public interface RetroGameManagement
{
    String getGenre(String text);

    List<PocketGame> getGames(String title);

    PocketGame getRandom();
}
