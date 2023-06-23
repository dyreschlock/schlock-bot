package com.schlock.bot.entities.pokemon;

import org.apache.tapestry5.json.JSONObject;

public class Pokemon
{
    private static final String NUMBER = "number";
    private static final String ID = "id";
    private static final String NAME = "name";

    private static final String TYPE1 = "type1";
    private static final String TYPE2 = "type2";

    private static final String BASE_STATS = "base_stats";

    private static final String HISUI_NUMBER = "hisui_number";

    private static final String HISUI_TYPE1 = "hisui_type1";
    private static final String HISUI_TYPE2 = "hisui_type2";

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
    private boolean shinyPaldea = false;

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

    public JSONObject createJSON()
    {
        JSONObject object = new JSONObject();

        object.put(NUMBER, number)
                .put(NAME, name)
                .put(ID, id)
                .put(TYPE1, type1)
                .put(TYPE2, type2)
                .put(BASE_STATS, basestats)
                .put(HISUI_NUMBER, hisuiNumber)
                .put(HISUI_TYPE1, hisuiType1)
                .put(HISUI_TYPE2, hisuiType2)
        ;

        return object;
    }

    public static Pokemon createFromJSON(JSONObject object)
    {
        Pokemon pokemon = new Pokemon();

        pokemon.number = object.getInt(NUMBER);
        pokemon.name = object.getString(NAME);
        pokemon.id = object.getString(ID);
        pokemon.basestats = object.getInt(BASE_STATS);

        pokemon.type1 = object.getString(TYPE1);
        if (object.containsKey(TYPE2))
        {
            pokemon.type2 = object.getString(TYPE2);
        }

        if (object.containsKey(HISUI_NUMBER))
        {
            pokemon.hisuiNumber = object.getInt(HISUI_NUMBER);

        }
        if (object.containsKey(HISUI_TYPE1))
        {
            pokemon.hisuiType1 = object.getString(HISUI_TYPE1);
        }
        if (object.containsKey(HISUI_TYPE2))
        {
            pokemon.hisuiType2 = object.getString(HISUI_TYPE2);
        }

        return pokemon;
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

    public boolean isShinyPaldea()
    {
        return shinyPaldea;
    }

    public void setShinyPaldea(boolean shinyPaldea)
    {
        this.shinyPaldea = shinyPaldea;
    }
}
