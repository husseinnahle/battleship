package Players.AI;

import Base.Cell;
import Base.Grid;
import Exceptions.InvalidPlacementException;
import Placeables.Placeable;
import Players.Player;

import java.util.*;

public abstract class AI extends Player {

    public AI(String name, Grid grid) {
        super(name, grid);
    }

    @Override
    public void placeItem(Placeable item) {

        try {

            var cells = findItemPlacement(item);
            this.grid.receiveItem(cells, item);

        } catch (InvalidPlacementException e) {

            throw new RuntimeException("AI: Unable to place " + item.toString());

        }

    }

    private List<Cell> findItemPlacement(Placeable item) {

        boolean isValid = false;

        Set<Cell> availableCells = this.grid.getAvailableCells();

        List<Cell> cells = new ArrayList<>();

        while (!isValid) {

            boolean lookInX = true;
            Optional<Cell> nextCell;
            Optional<Cell> randomCell = this.grid
                    .getAvailableCells()
                    .stream()
                    .skip(new Random().nextInt(availableCells.size()))
                    .findFirst();

            Cell cell = randomCell.get();

            for (int i = 0; i < item.getCapacity(); i++) {

                isValid = true;

                if (lookInX) {

                    nextCell = cell.rightNeighbour();

                    if (nextCell.isPresent() && availableCells.contains(nextCell.get())) {

                        cells.add(cell);
                        cell = nextCell.get();

                    } else {

                        i = -1;
                        lookInX = false;
                        cells.clear();
                        cell = randomCell.get();
                    }

                } else {

                    nextCell = cell.upNeighbour();

                    if (nextCell.isPresent() && availableCells.contains(nextCell.get())) {

                        cells.add(cell);
                        cell = nextCell.get();

                    } else {

                        cells.clear();
                        isValid = false;
                        break;

                    }

                }

            }
        }

        return cells;
    }

}
