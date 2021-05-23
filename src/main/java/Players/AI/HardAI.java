package Players.AI;

import Base.Cell;
import Base.Effect;
import Base.Grid;

import java.util.HashSet;

public class HardAI extends MediumAI {

    private HashSet<Cell> touchedCells;

    public HardAI(String name, Grid grid) {
        super(name, grid);

        this.touchedCells = new HashSet<>();
    }

    /**
     * The difference with Medium AI, this AI will also add the adjacent cells when a ship sunk.
     * Since it's not possible for another player to place items on adjacent cells.
     *
     * @param effect
     */
    @Override
    public void notify(Effect effect) {

        if (effect == Effect.TOUCHED) this.touchedCells.add(this.lastShotCell);

        if (effect == Effect.SUNK) {

            for (Cell cell : this.touchedCells) {

                for (Cell adjacentCell : cell.getAdjacentCells())

                    if (this.grid.cellIsWithinGrid(adjacentCell)) this.shotCells.add(adjacentCell);

            }

            this.touchedCells = new HashSet<>();
        }

        super.notify(effect);

    }

}
