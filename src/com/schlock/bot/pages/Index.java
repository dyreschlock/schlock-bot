package com.schlock.bot.pages;

import com.schlock.bot.services.database.apps.ShinyGetDAO;

import javax.inject.Inject;
import java.util.Date;

public class Index
{
    @Inject
    private ShinyGetDAO shinyGetDAO;

    public String getShinyNumber()
    {
        return shinyGetDAO.getCurrentShinyNumber().toString();
    }

    public String getDate()
    {
        return new Date().toString();
    }
}
