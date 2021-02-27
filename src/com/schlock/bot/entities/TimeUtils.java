package com.schlock.bot.entities;

public class TimeUtils
{
    public static final String formatDoubleMinutesIntoTimeString(Double minutes)
    {
        Integer intMinutes = minutes.intValue();

        Integer hrs = intMinutes /60;
        Integer min = intMinutes %60;
        Integer sec = Double.valueOf((minutes - intMinutes) * 60).intValue();

        String minutesString = addZeroIfLessThanTen(min.toString());
        String secondsString = addZeroIfLessThanTen(sec.toString());

        return String.format("%s:%s:%s", hrs.toString(), minutesString, secondsString);
    }

    private static final String addZeroIfLessThanTen(String number)
    {
        if (number.length() == 1)
        {
            return "0" + number;
        }
        return number;
    }
}
