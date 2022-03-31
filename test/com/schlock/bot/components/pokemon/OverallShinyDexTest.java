package com.schlock.bot.components.pokemon;

import com.schlock.bot.AppTestCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OverallShinyDexTest extends AppTestCase
{
    private OverallShinyDex component;

    @Test
    public void testPokemonNumberFromCell()
    {
        component = new OverallShinyDex();

        assertCorrectPoke(1, 1);
        assertCorrectPoke(2, 2);
        assertCorrectPoke(6, 6);
        assertCorrectPoke(7, 31);
        assertCorrectPoke(13, 61);
        assertCorrectPoke(19, 91);

        assertCorrectPoke(37, 7);
        assertCorrectPoke(38, 8);
        assertCorrectPoke(42, 12);
        assertCorrectPoke(43, 37);


        assertCorrectPoke(181, 181);
        assertCorrectPoke(182, 182);
        assertCorrectPoke(186, 186);
        assertCorrectPoke(187, 211);
    }

    private void assertCorrectPoke(int cellNumber, int pokemonNumber)
    {
        int pNum = component.getPokemonNumber(cellNumber);

        String message = "Cell Number " + cellNumber + " should equal " + pokemonNumber;

        assertEquals(pokemonNumber, pNum, message);
    }

    @Test
    public void testBoxNumber()
    {
        component = new OverallShinyDex();

        assertCorrectBox(1, 1);
        assertCorrectBox(2, 2);
        assertCorrectBox(6, 6);

        assertCorrectBox(7, 1);
        assertCorrectBox(12, 6);

        assertCorrectBox(31, 7);
        assertCorrectBox(32, 8);
        assertCorrectBox(36, 12);

        assertCorrectBox(37, 7);
        assertCorrectBox(42, 12);

        assertCorrectBox(61, 13);
    }

    private void assertCorrectBox(int set, int boxNumber)
    {
        int set_index = set -1;

        int b_index = component.getBoxNumber(set_index);
        int bNum = b_index +1;

        String message = "Set Number " + set + " should equal " + boxNumber;

        assertEquals(boxNumber, bNum, message);
    }
}
