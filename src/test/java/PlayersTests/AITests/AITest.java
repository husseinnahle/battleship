package PlayersTests.AITests;

import Base.Cell;
import Base.Grid;
import Exceptions.InvalidPlacementException;
import Placeables.Boat;
import Placeables.BoatType;
import Players.AI.AI;
import Players.AI.EasyAI;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

public class AITest {

    @Test
    public void PlaceItemTest() throws RuntimeException {

        AI ai = new EasyAI("Test AI", new Grid('J', 10));

        ai.placeItem(new Boat(BoatType.CRUISER));
    }


    @Test
    public void FindItemPlacementInEmptyGridTest() throws InvalidPlacementException {

        int qtyOfBoatsExpected = 1;

        Grid grid = new Grid('J', 10);
        AI ai = new EasyAI("Test AI", grid);

        ai.placeItem(new Boat(BoatType.CARRIER));

        int qtyOfBoatsResult = ai.getNumberOfFloatingBoats();
        assertEquals(qtyOfBoatsExpected, qtyOfBoatsResult);

    }

    @Test
    public void FindItemPlacementInGridTest() throws InvalidPlacementException {

        int qtyOfBoatsExpected = 5;

        Grid grid = new Grid('J', 10);
        AI ai = new EasyAI("Test AI", grid);

        ai.placeItem(new Boat(BoatType.DESTROYER));
        ai.placeItem(new Boat(BoatType.CRUISER));
        ai.placeItem(new Boat(BoatType.CRUISER));
        ai.placeItem(new Boat(BoatType.BATTLESHIP));
        ai.placeItem(new Boat(BoatType.CARRIER));

        int qtyOfBoatsResult = ai.getNumberOfFloatingBoats();
        assertEquals(qtyOfBoatsExpected, qtyOfBoatsResult);
    }
}
