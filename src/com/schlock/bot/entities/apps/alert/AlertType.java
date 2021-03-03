package com.schlock.bot.entities.apps.alert;

public enum AlertType
{
    FOLLOWER(20),
    SUBSCRIBER(20),

    MONSTER_FRISBEE(5);

    public final int duration;

    AlertType(int duration)
    {
        this.duration = duration;
    }

}
