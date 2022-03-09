package com.schlock.bot.pages.pokemon;

import com.schlock.bot.components.pokemon.HisuiShinyDex;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;

public class ShinyDex
{
    private static final String PARAM_GO = "go";
    private static final String PARAM_LEGENDS = "hisui";

    @InjectComponent
    private HisuiShinyDex hisuiShinyComponent;

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

    Object onActivate(String p1, String p2)
    {
        onActivate(p1);

        if (isLegendsPage())
        {
            hisuiShinyComponent.setListInDexFormat(p2);
        }

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
