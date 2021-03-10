package com.schlock.bot.services.database.base;

import com.schlock.bot.entities.base.User;
import com.schlock.bot.entities.base.alert.Alert;
import com.schlock.bot.entities.base.alert.AnimationAlert;
import com.schlock.bot.entities.base.alert.TwitchAlert;
import com.schlock.bot.services.database.BaseDAO;

import java.util.List;

public interface AlertDAO extends BaseDAO<Alert>
{
    public List<Alert> getByUser(User user);

    public TwitchAlert getEarliestUnfinishedTwitchAlert();

    public AnimationAlert getEarliestUnfinishedAnimationAlert();
}
