package com.schlock.bot.services.database.apps;

import com.schlock.bot.entities.apps.alert.Alert;
import com.schlock.bot.entities.apps.alert.AnimationAlert;
import com.schlock.bot.entities.apps.alert.TwitchAlert;
import com.schlock.bot.services.database.BaseDAO;

public interface AlertDAO extends BaseDAO<Alert>
{
    public TwitchAlert getEarliestUnfinishedTwitchAlert();

    public AnimationAlert getEarliestUnfinishedAnimationAlert();
}
