package PlayersTests.AITests;

import Base.Cell;
import Base.Effect;
import Base.Grid;
import Exceptions.InvalidPlacementException;
import Placeables.Boat;
import Placeables.BoatType;
import Players.AI.EasyAI;
import Players.AI.MediumAI;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.Assert.*;

public class MediumAITest {

    @Test
    public void MediumAISinksBoatTest() throws InvalidPlacementException {

        char length = 'C';
        int width = 5;
        var grid = new Grid(length, width);

        grid.receiveItem(
                new ArrayList<>() {{
                    add(new Cell('C', 2));
                    add(new Cell('C', 3));
                    add(new Cell('C', 4));
                }},
                new Boat(BoatType.CRUISER)
        );


        var mediumAI = new MediumAI("Medium AI Test", new Grid(length, width));

        var effects = new ArrayList<Effect>();

        while (grid.getNumberOfFloatingBoats() > 0) {
            var cell = mediumAI.shoot();
            var effect = grid.receiveShot(cell);
            mediumAI.notify(effect);

            effects.add(effect);
        }

        assertTrue(effects.contains(Effect.SUNK));
    }

    @Test
    public void MediumAINotShootSameCellTwiceTest() throws InvalidPlacementException {

        char length = 'C';
        int width = 5;
        var grid = new Grid(length, width);

        grid.receiveItem(
                new ArrayList<>() {{
                    add(new Cell('C', 2));
                    add(new Cell('C', 3));
                    add(new Cell('C', 4));
                }},
                new Boat(BoatType.CRUISER)
        );

        var mediumAI = new MediumAI("Medium AI Test", new Grid(length, width));

        var shotCells = new ArrayList<Cell>();

        while (grid.getNumberOfFloatingBoats() > 0) {
            var cell = mediumAI.shoot();
            var effect = grid.receiveShot(cell);
            mediumAI.notify(effect);
            shotCells.add(cell);
        }

        var uniqueCells = new HashSet<>(shotCells);
        assertEquals(uniqueCells.size(), shotCells.size());

    }


    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void MediumAIThrowIfNoMoreCellTest() throws InvalidPlacementException {

        char length = 'B';
        int width = 10;
        var grid = new Grid(length, width);

        grid.receiveItem(
                new ArrayList<>() {{
                    add(new Cell('B', 2));
                    add(new Cell('B', 3));
                    add(new Cell('B', 4));
                    add(new Cell('B', 5));
                }},
                new Boat(BoatType.BATTLESHIP)
        );

        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("cannot find new cells");

        var mediumAI = new MediumAI("Medium AI Test", new Grid(length, width));

        var shotCells = new ArrayList<Cell>();
        var duplicates = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            var cell = mediumAI.shoot();
            var effect = grid.receiveShot(cell);
            mediumAI.notify(effect);

            if (shotCells.contains(cell)) duplicates.add(cell);
            shotCells.add(cell);
        }

        System.out.println(shotCells.size());

    }

    @Test
    public void MediumIsFasterThanEasyAI() throws InvalidPlacementException {

        char length = 'J';
        int width = 10;

        var mediumAI = new MediumAI("Medium AI Test", new Grid(length, width));
        var easyAI = new EasyAI("Easy AI Test", new Grid(length, width));

        mediumAI.subscribe(easyAI);
        easyAI.subscribe(mediumAI);

        mediumAI.placeItem(new Boat(BoatType.BATTLESHIP));
        mediumAI.placeItem(new Boat(BoatType.CRUISER));
        mediumAI.placeItem(new Boat(BoatType.CARRIER));
        mediumAI.placeItem(new Boat(BoatType.DESTROYER));

        easyAI.placeItem(new Boat(BoatType.BATTLESHIP));
        easyAI.placeItem(new Boat(BoatType.CRUISER));
        easyAI.placeItem(new Boat(BoatType.CARRIER));
        easyAI.placeItem(new Boat(BoatType.DESTROYER));


        int shotsNeededByEasyAI = 0;
        while (mediumAI.getNumberOfFloatingBoats() > 0) {
            mediumAI.receiveShot(easyAI.shoot());
            shotsNeededByEasyAI++;
        }

        int shotsNeededByMediumAI = 0;
        while (easyAI.getNumberOfFloatingBoats() > 0) {
            easyAI.receiveShot(mediumAI.shoot());
            shotsNeededByMediumAI++;
        }

        assertTrue(shotsNeededByEasyAI > shotsNeededByMediumAI);

    }

}
