package com.schlock.bot.entities.apps.alert;

import com.schlock.bot.entities.apps.User;

import javax.persistence.Entity;
import java.util.Date;

@Entity
public class AnimationAlert extends Alert
{
    public static AnimationAlert create(AlertType type, User user)
    {
        AnimationAlert alert = new AnimationAlert();
        alert.setRequestDate(new Date());
        alert.setType(type);
        alert.setUser(user);

        return alert;
    }
}
