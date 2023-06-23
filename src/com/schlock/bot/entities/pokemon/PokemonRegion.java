package com.schlock.bot.entities.pokemon;

import java.util.Arrays;
import java.util.List;

public enum PokemonRegion
{
    ALOLA("a", Arrays.asList(19, 20, 26, 27, 28, 37, 38, 50, 51, 52, 53, 74, 75, 76, 88, 89, 103, 105)),
    GALAR("g", Arrays.asList(52, 77, 78, 83, 110, 122, 222, 263, 264, 554, 555, 562, 618, 79, 80, 199, 144, 145, 146)),
    HISUI("h", Arrays.asList(157, 503, 724, 58, 59, 100, 101, 211, 215, 549, 550, 570, 571, 628, 705, 706, 713)),
    PALDEA("p", Arrays.asList(128, 194));

    private static final String DELIM = "_";

    private final String mark;

    private final List<Integer> pokemonNumbers;

    PokemonRegion(String mark, List<Integer> pokemonNumbers)
    {
        this.mark = mark;
        this.pokemonNumbers = pokemonNumbers;
    }

    public List<Integer> pokemonNumbers()
    {
        return pokemonNumbers;
    }

    public String prefix()
    {
        return this.mark + DELIM;
    }

    public static boolean isNormalNumberCode(String numberCode)
    {
        String[] parts = numberCode.split(DELIM);
        return parts.length == 1;
    }

    public static boolean isRegionalNumberCode(String numberCode, PokemonRegion region)
    {
        String[] parts = numberCode.split(DELIM);
        return parts.length == 2 && region.mark.equals(parts[0]);
    }
}
