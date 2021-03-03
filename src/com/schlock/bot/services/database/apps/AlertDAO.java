package com.schlock.bot.services.database.apps;

import com.schlock.bot.entities.apps.User;
import com.schlock.bot.entities.apps.alert.Alert;
import com.schlock.bot.entities.apps.alert.AnimationAlert;
import com.schlock.bot.entities.apps.alert.TwitchAlert;
import com.schlock.bot.services.database.BaseDAO;

import java.util.List;

public interface AlertDAO extends BaseDAO<Alert>
{
    public List<Alert> getByUser(User user);

    public TwitchAlert getEarliestUnfinishedTwitchAlert();

    public AnimationAlert getEarliestUnfinishedAnimationAlert();
}
