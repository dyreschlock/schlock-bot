package com.schlock.bot.pages.pokemon;

import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Index
{
    private static final String UPDATE_MESSAGE = "update";

    @Inject
    private Messages messages;

    public String getGeneratedMessage()
    {
        SimpleDateFormat format = new SimpleDateFormat("MMMMMM d, YYYY");

        String message = messages.format(UPDATE_MESSAGE, format.format(new Date()));
        return message;
    }
}
