package com.schlock.bot.pages.pokemon;

import org.apache.tapestry5.annotations.Persist;

public class ShinyDex
{
    private static final String PARAM_GO = "go";
    private static final String PARAM_LEGENDS = "hisui";

    @Persist
    private String pageParam;

    Object onActivate()
    {
        return onActivate(null);
    }

    Object onActivate(String parameter)
    {
        this.pageParam = parameter;

        return true;
    }

    public boolean isGoPage()
    {
        return PARAM_GO.equalsIgnoreCase(pageParam);
    }

    public boolean isLegendsPage()
    {
        return PARAM_LEGENDS.equalsIgnoreCase(pageParam);
    }

    public boolean isLetsGoPage()
    {
        return !isGoPage() && !isLegendsPage();
    }
}
