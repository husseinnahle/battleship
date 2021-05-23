package BaseTests;

import Base.Cell;
import Base.Effect;
import Base.Grid;
import Exceptions.InvalidPlacementException;
import Placeables.Boat;
import Placeables.BoatType;
import Placeables.Bomb;
import Placeables.Placeable;
import org.hamcrest.core.StringStartsWith;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class GridTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void CanReceiveItemTest1() throws InvalidPlacementException {

        Grid grid = new Grid('J', 10);

        var firstCells = new ArrayList<Cell>() {{
            add(new Cell('D', 3));
        }};

        grid.receiveItem(firstCells, new Bomb());

        var secondCells = new ArrayList<Cell>() {{
            add(new Cell('F', 4));
            add(new Cell('G', 4));
            add(new Cell('H', 4));
        }};

        grid.receiveItem(secondCells, new Boat(BoatType.CRUISER));
    }

    @Test
    public void CanReceiveItemTest2() throws InvalidPlacementException {

        Grid grid = new Grid('J', 10);

        var cells = new ArrayList<Cell>() {{
            add(new Cell('F', 4));
        }};

        grid.receiveItem(cells, new Bomb());
    }

    @Test
    public void CanReceiveItemTest3() throws InvalidPlacementException {

        Grid grid = new Grid('J', 10);

        var cells = new ArrayList<Cell>() {{
            add(new Cell('C', 2));
            add(new Cell('C', 3));
            add(new Cell('C', 4));
            add(new Cell('C', 5));
            add(new Cell('C', 6));
        }};

        grid.receiveItem(cells, new Boat(BoatType.CARRIER));
    }


    @Test
    public void CannotReceiveItemCellsAreNotLinearTest() throws InvalidPlacementException {

        Grid grid = new Grid('J', 10);

        var cells = new ArrayList<Cell>() {{
            add(new Cell('F', 4));
            add(new Cell('G', 5));
            add(new Cell('H', 4));
        }};

        String expectedMessage = "Invalid placement!";
        thrown.expect(InvalidPlacementException.class);
        thrown.expectMessage(new StringStartsWith(expectedMessage));

        grid.receiveItem(cells, new Boat(BoatType.CRUISER));
    }

    @Test
    public void CannotReceiveItemAdjacentCellsAreOccupiedTest() throws InvalidPlacementException {

        Grid grid = new Grid('J', 10);

        var firstCells = new ArrayList<Cell>() {{
            add(new Cell('E', 3));
        }};

        grid.receiveItem(firstCells, new Bomb());

        var secondCells = new ArrayList<Cell>() {{
            add(new Cell('F', 4));
            add(new Cell('G', 4));
            add(new Cell('H', 4));
        }};

        String expectedMessage = String.format("Invalid placement! Adjacent cell at (%s) is occupied.", firstCells.get(0).toString());
        thrown.expect(InvalidPlacementException.class);
        thrown.expectMessage(expectedMessage);

        grid.receiveItem(secondCells, new Boat(BoatType.BATTLESHIP));
    }

    @Test
    public void CannotReceiveItemCellsAreOccupiedTest() throws InvalidPlacementException {

        Grid grid = new Grid('J', 10);

        var firstCells = new ArrayList<Cell>() {{
            add(new Cell('F', 4));
        }};

        grid.receiveItem(firstCells, new Bomb());


        var secondCells = new ArrayList<Cell>() {{
            add(new Cell('F', 4));
            add(new Cell('G', 4));
            add(new Cell('H', 4));
        }};

        String expectedMessage = String.format("Invalid placement! Cell at (%s) is occupied.", firstCells.get(0).toString());
        thrown.expect(InvalidPlacementException.class);
        thrown.expectMessage(expectedMessage);

        grid.receiveItem(secondCells, new Boat(BoatType.CRUISER));
    }

    @Test
    public void CannotReceiveItemCapacityIsNotValidTest() throws InvalidPlacementException {

        Grid grid = new Grid('J', 10);

        Placeable boat = new Boat(BoatType.BATTLESHIP);

        var cells = new ArrayList<Cell>() {{
            add(new Cell('F', 4));
            add(new Cell('G', 4));
            add(new Cell('H', 4));
        }};

        String expectedMessage = String.format("Invalid placement! Number of cells (%d) does not match %s's size (%d).",
                cells.size(), boat.toString(), boat.getCapacity());

        thrown.expect(InvalidPlacementException.class);
        thrown.expectMessage(expectedMessage);

        grid.receiveItem(cells, boat);
    }

    @Test
    public void CannotReceiveItemDuplicateCellsTest() throws InvalidPlacementException {

        Grid grid = new Grid('J', 10);

        Placeable boat = new Boat(BoatType.BATTLESHIP);
        Cell duplicateCell = new Cell('H', 4);

        var cells = new ArrayList<Cell>() {{
            add(new Cell('F', 4));
            add(new Cell('G', 4));
            add(duplicateCell);
            add(duplicateCell);
        }};

        String expectedMessage = String.format("Cell (%s) is present more than once. Duplicates are not allowed.",
                duplicateCell.toString());

        thrown.expect(InvalidPlacementException.class);
        thrown.expectMessage(expectedMessage);

        grid.receiveItem(cells, boat);
    }

    @Test
    public void CannotReceiveShotInvalidCell() throws InvalidPlacementException {

        Grid grid = new Grid('J', 10);

        Cell invalidCell = new Cell('A', 11);

        thrown.expect(InvalidPlacementException.class);
        thrown.expectMessage("Invalid cell!");

        grid.receiveShot(invalidCell);
    }

    @Test
    public void ShotTouchedABombTest() throws InvalidPlacementException {

        Effect effectExpected = Effect.SKIP_TURN;

        Grid grid = new Grid('A', 10);

        var cells = new ArrayList<Cell>() {{
            add(new Cell('A', 10));
        }};

        grid.receiveItem(cells, new Bomb());

        Effect effectResult = grid.receiveShot(cells.get(0));

        assertEquals(effectExpected, effectResult);
    }

    @Test
    public void ShotTouchedABoatTest() throws InvalidPlacementException {

        Effect effectExpected = Effect.TOUCHED;

        Grid grid = new Grid('J', 10);

        var cells = new ArrayList<Cell>() {{
            add(new Cell('F', 4));
            add(new Cell('G', 4));
            add(new Cell('H', 4));
        }};

        grid.receiveItem(cells, new Boat(BoatType.CRUISER));

        Effect effectResult = grid.receiveShot(new Cell('F', 4));

        assertEquals(effectExpected, effectResult);
    }


    @Test
    public void ShotTouchedNothingTest() throws InvalidPlacementException {

        Effect effectExpected = Effect.NOTHING;

        Grid grid = new Grid('A', 10);

        Effect effectResult = grid.receiveShot(new Cell('A', 10));

        assertEquals(effectExpected, effectResult);
    }

    @Test
    public void BoatTouchedAndSunkTest() throws InvalidPlacementException {

        Effect[] effectsExpected = {Effect.TOUCHED, Effect.SUNK};

        Effect[] effectsResult = new Effect[2];

        Grid grid = new Grid('A', 10);
        Boat destroyer = new Boat(BoatType.DESTROYER);
        var cells = new ArrayList<Cell>() {{
            add(new Cell('A', 2));
            add(new Cell('A', 3));
        }};

        grid.receiveItem(cells, destroyer);
        effectsResult[0] = grid.receiveShot(new Cell('A', 2));
        effectsResult[1] = grid.receiveShot(new Cell('A', 3));

        assertArrayEquals(effectsExpected, effectsResult);
    }

    @Test
    public void CellCannotBeShotTwiceTest() throws InvalidPlacementException {

        Effect effectExpected = Effect.NOTHING;

        Grid grid = new Grid('J', 10);

        var cells = new ArrayList<Cell>() {{
            add(new Cell('F', 4));
        }};

        grid.receiveItem(cells, new Bomb());

        grid.receiveShot(new Cell('F', 4));

        Effect effectResult = grid.receiveShot(new Cell('F', 4));

        assertEquals(effectExpected, effectResult);
    }

    @Test
    public void NumberOfFloatingBoatsTest1() throws InvalidPlacementException {

        int numberOfFloatingBoatsExpected = 1;

        Grid grid = new Grid('J', 10);

        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('A', 2));
            add(new Cell('A', 3));
        }}, new Boat(BoatType.DESTROYER));

        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('H', 2));
            add(new Cell('H', 3));
            add(new Cell('H', 4));
        }}, new Boat(BoatType.CRUISER));

        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('A', 7));
            add(new Cell('A', 8));
            add(new Cell('A', 9));
            add(new Cell('A', 10));
        }}, new Boat(BoatType.BATTLESHIP));

        grid.receiveShot(new Cell('A', 2));
        grid.receiveShot(new Cell('A', 3));

        grid.receiveShot(new Cell('H', 2));
        grid.receiveShot(new Cell('H', 3));
        grid.receiveShot(new Cell('H', 4));

        grid.receiveShot(new Cell('A', 7));
        grid.receiveShot(new Cell('A', 10));

        int numberOfFloatingBoatsResult = grid.getNumberOfFloatingBoats();

        assertEquals(numberOfFloatingBoatsExpected, numberOfFloatingBoatsResult);
    }

    @Test
    public void NumberOfFloatingBoatsTest2() throws InvalidPlacementException {

        int numberOfFloatingBoatsExpected = 0;

        Grid grid = new Grid('J', 10);

        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('A', 2));
            add(new Cell('A', 3));
        }}, new Boat(BoatType.DESTROYER));

        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('A', 6));
            add(new Cell('A', 7));
            add(new Cell('A', 8));
            add(new Cell('A', 9));
            add(new Cell('A', 10));
        }}, new Boat(BoatType.CARRIER));

        grid.receiveShot(new Cell('A', 2));
        grid.receiveShot(new Cell('A', 3));

        grid.receiveShot(new Cell('A', 6));
        grid.receiveShot(new Cell('A', 7));
        grid.receiveShot(new Cell('A', 8));
        grid.receiveShot(new Cell('A', 9));
        grid.receiveShot(new Cell('A', 10));


        int numberOfFloatingBoatsResult = grid.getNumberOfFloatingBoats();

        assertEquals(numberOfFloatingBoatsExpected, numberOfFloatingBoatsResult);
    }

    @Test
    public void NumberOfFloatingBoatsTest3() throws InvalidPlacementException {

        int numberOfFloatingBoatsExpected = 4;

        Grid grid = new Grid('J', 10);

        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('A', 2));
            add(new Cell('A', 3));
        }}, new Boat(BoatType.DESTROYER));

        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('H', 2));
            add(new Cell('H', 3));
            add(new Cell('H', 4));
        }}, new Boat(BoatType.CRUISER));

        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('J', 2));
            add(new Cell('J', 3));
            add(new Cell('J', 4));
        }}, new Boat(BoatType.CRUISER));

        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('C', 4));
            add(new Cell('D', 4));
            add(new Cell('E', 4));
        }}, new Boat(BoatType.CRUISER));

        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('A', 7));
            add(new Cell('A', 8));
            add(new Cell('A', 9));
            add(new Cell('A', 10));
        }}, new Boat(BoatType.BATTLESHIP));

        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('D', 7));
            add(new Cell('E', 7));
            add(new Cell('F', 7));
            add(new Cell('G', 7));
            add(new Cell('H', 7));
        }}, new Boat(BoatType.CARRIER));


        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('F', 1));
        }}, new Bomb());
        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('D', 2));
        }}, new Bomb());
        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('A', 5));
        }}, new Bomb());
        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('D', 9));
        }}, new Bomb());

        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('J', 6));
        }}, new Bomb());

        // Sink destroyer
        grid.receiveShot(new Cell('A', 2));
        grid.receiveShot(new Cell('A', 3));

        // Touch a bomb
        grid.receiveShot(new Cell('D', 2));

        // Sink cruiser
        grid.receiveShot(new Cell('H', 2));
        grid.receiveShot(new Cell('H', 3));
        grid.receiveShot(new Cell('H', 4));

        // Touch an empty cell
        grid.receiveShot(new Cell('E', 5));

        // Touch battleship
        grid.receiveShot(new Cell('A', 7));
        grid.receiveShot(new Cell('A', 10));

        int numberOfFloatingBoatsResult = grid.getNumberOfFloatingBoats();

        assertEquals(numberOfFloatingBoatsExpected, numberOfFloatingBoatsResult);
    }

    @Test
    public void NumberOfAvailableCellsTest1() throws InvalidPlacementException {

        int numberOfAvailableCellsExpected = 54;

        Grid grid = new Grid('J', 10);

        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('B', 2));
            add(new Cell('B', 3));
        }}, new Boat(BoatType.DESTROYER));

        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('H', 2));
            add(new Cell('H', 3));
            add(new Cell('H', 4));
        }}, new Boat(BoatType.CRUISER));

        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('D', 2));
            add(new Cell('E', 2));
            add(new Cell('F', 2));
        }}, new Boat(BoatType.CRUISER));

        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('A', 7));
            add(new Cell('A', 8));
            add(new Cell('A', 9));
            add(new Cell('A', 10));
        }}, new Boat(BoatType.BATTLESHIP));

        int numberOfAvailableCellsActual = grid.getAvailableCells().size();

        assertEquals(numberOfAvailableCellsExpected, numberOfAvailableCellsActual);
    }

    @Test
    public void NumberOfAvailableCellsTest2() throws InvalidPlacementException {

        int numberOfAvailableCellsExpected = 72;

        Grid grid = new Grid('J', 10);

        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('A', 2));
            add(new Cell('A', 3));
        }}, new Boat(BoatType.DESTROYER));

        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('H', 2));
            add(new Cell('H', 3));
            add(new Cell('H', 4));
        }}, new Boat(BoatType.CRUISER));

        grid.receiveItem(new ArrayList<>() {{
            add(new Cell('J', 2));
            add(new Cell('J', 3));
            add(new Cell('J', 4));
        }}, new Boat(BoatType.CRUISER));

        int numberOfAvailableCellsActual = grid.getAvailableCells().size();

        assertEquals(numberOfAvailableCellsExpected, numberOfAvailableCellsActual);
    }
}
