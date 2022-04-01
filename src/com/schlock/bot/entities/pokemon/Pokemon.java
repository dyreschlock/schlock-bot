package com.schlock.bot.entities.pokemon;

public class Pokemon
{
    private Integer number;
    private String id;
    private String name;

    private String type1;
    private String type2;

    private Integer basestats = 0;

    private Integer hisuiNumber;

    private String hisuiType1;
    private String hisuiType2;

    private boolean shiny = false;
    private boolean shinyAlola = false;
    private boolean shinyGalar = false;
    private boolean shinyHisui = false;

    public Pokemon()
    {
    }

    public String getNumberString()
    {
        String num = Integer.toString(number);
        while (num.length() < 3)
        {
            num = "0" + num;
        }
        return num;
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

    public Integer getHisuiNumber()
    {
        return hisuiNumber;
    }

    public void setHisuiNumber(Integer hisuiNumber)
    {
        this.hisuiNumber = hisuiNumber;
    }

    public String getHisuiType1()
    {
        return hisuiType1;
    }

    public void setHisuiType1(String hisuiType1)
    {
        this.hisuiType1 = hisuiType1;
    }

    public String getHisuiType2()
    {
        return hisuiType2;
    }

    public void setHisuiType2(String hisuiType2)
    {
        this.hisuiType2 = hisuiType2;
    }

    public boolean isShiny()
    {
        return shiny;
    }

    public void setShiny(boolean shiny)
    {
        this.shiny = shiny;
    }

    public boolean isShinyAlola()
    {
        return shinyAlola;
    }

    public void setShinyAlola(boolean shinyAlola)
    {
        this.shinyAlola = shinyAlola;
    }

    public boolean isShinyGalar()
    {
        return shinyGalar;
    }

    public void setShinyGalar(boolean shinyGalar)
    {
        this.shinyGalar = shinyGalar;
    }

    public boolean isShinyHisui()
    {
        return shinyHisui;
    }

    public void setShinyHisui(boolean shinyHisui)
    {
        this.shinyHisui = shinyHisui;
    }
}
