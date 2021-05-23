package PlayersTests;

import Base.Cell;
import Base.Grid;
import Players.Human;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.Assert.*;


public class HumanTest {

    @Test
    public void ShootOneCellTest() {

        Cell cellExpected = new Cell('A', 21);
        String input = "A21\n";
        Human human = new Human("Test Human", new Grid('A', 1), new Scanner(input));

        Cell cellResult = human.shoot();

        assertEquals(cellExpected, cellResult);

    }

    @Test
    public void ShootInvalidCellTest() {

        String outputExpected = "\"A\" is not a valid coordinate for a cell";
        String input = "AA21\nA21";
        Human human = new Human("Test Human", new Grid('A', 1), new Scanner(input));

        var out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        human.shoot();

        String output = out.toString();

        assertTrue(output.contains(outputExpected));

    }

    @Test
    public void ShootTwoCellsTest() {

        String outputExpected = "Only one cell should be entered";
        String input = "A21A22\nA21";
        Human human = new Human("Test Human", new Grid('A', 1), new Scanner(input));

        var out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        human.shoot();

        String output = out.toString();

        assertTrue(output.contains(outputExpected));

    }


}
