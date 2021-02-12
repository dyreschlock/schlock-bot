package com.schlock.bot.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public abstract class Persisted
{
    public abstract Long getId();

    public abstract void setId(Long id);


    public boolean equals(Object o)
    {
        if (o != null && Persisted.class.isAssignableFrom(o.getClass()))
        {
            Persisted that = (Persisted) o;
            if (this.getId() != null && that.getId() != null)
            {
                return this.getId().equals(that.getId());
            }
        }
        return false;
    }
}
