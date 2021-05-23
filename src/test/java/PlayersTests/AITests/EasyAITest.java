package PlayersTests.AITests;

import Base.Cell;
import Base.Grid;
import Players.AI.EasyAI;
import org.junit.Test;

import static org.junit.Assert.*;

public class EasyAITest {

    @Test
    public void shootItemTest() {

        char length = 'C';
        int width = 10;

        var maxCell = new Cell(length, width);
        var easyAI = new EasyAI("Easy AI Test", new Grid(length, width));

        var shootedCell = easyAI.shoot();

        assertTrue(maxCell.compareTo(shootedCell) >= 0);

    }
}
