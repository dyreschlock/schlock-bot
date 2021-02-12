package com.schlock.bot.entities;

import javax.persistence.Id;

public class Persisted
{
    @Id
    private Long id;


    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

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
