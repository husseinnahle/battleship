package PlayersTests.AITests;

import Base.Grid;
import Exceptions.InvalidPlacementException;
import Placeables.Boat;
import Placeables.BoatType;
import Players.AI.HardAI;
import Players.AI.MediumAI;
import org.junit.Ignore;
import org.junit.Test;


import static org.junit.Assert.*;

public class HardAITest {

    @Test
    @Ignore
    public void HardIsFasterThanMediumAI() throws InvalidPlacementException {

        char length = 'J';
        int width = 10;

        var mediumAI = new MediumAI("Medium AI Test", new Grid(length, width));
        var hardAI = new HardAI("Hard AI Test", new Grid(length, width));

        mediumAI.subscribe(hardAI);
        hardAI.subscribe(mediumAI);

        mediumAI.placeItem(new Boat(BoatType.BATTLESHIP));
        mediumAI.placeItem(new Boat(BoatType.CRUISER));
        mediumAI.placeItem(new Boat(BoatType.CARRIER));
        mediumAI.placeItem(new Boat(BoatType.DESTROYER));

        hardAI.placeItem(new Boat(BoatType.BATTLESHIP));
        hardAI.placeItem(new Boat(BoatType.CRUISER));
        hardAI.placeItem(new Boat(BoatType.CARRIER));
        hardAI.placeItem(new Boat(BoatType.DESTROYER));


        int shotsNeededByHardAI = 0;
        while (mediumAI.getNumberOfFloatingBoats() > 0) {
            mediumAI.receiveShot(hardAI.shoot());
            shotsNeededByHardAI++;
        }

        int shotsNeededByMediumAI = 0;
        while (hardAI.getNumberOfFloatingBoats() > 0) {
            hardAI.receiveShot(mediumAI.shoot());
            shotsNeededByMediumAI++;
        }

        assertTrue(shotsNeededByHardAI < shotsNeededByMediumAI);

    }

}
