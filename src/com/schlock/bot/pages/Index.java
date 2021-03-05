package com.schlock.bot.pages;

import com.schlock.bot.services.DeploymentConfiguration;
import org.apache.tapestry5.ioc.annotations.Inject;

public class Index
{
    @Inject
    private DeploymentConfiguration config;


    public String getTitle()
    {
        return config.getBotName();
    }

    public String getSourceMD()
    {
        return config.getReadmeFileContents();
    }
}
