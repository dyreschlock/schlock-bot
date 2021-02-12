package com.schlock.bot.entities.pokemon;

public class Pokemon
{
    private Integer number;
    private String id;
    private String name;

    private String type1;
    private String type2;

    private Integer basestats = 0;

    public Pokemon()
    {
    }

    public void incrementBasestats(int i)
    {
        basestats = basestats +i;
    }


    public Integer getNumber()
    {
        return number;
    }

    public void setNumber(Integer number)
    {
        this.number = number;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType1()
    {
        return type1;
    }

    public void setType1(String type1)
    {
        this.type1 = type1;
    }

    public String getType2()
    {
        return type2;
    }

    public void setType2(String type2)
    {
        this.type2 = type2;
    }

    public Integer getBasestats()
    {
        return basestats;
    }

    public void setBasestats(Integer basestats)
    {
        this.basestats = basestats;
    }
}
