package Players.AI;

import Base.Cell;
import Base.Effect;
import Base.Grid;

import java.util.HashSet;
import java.util.Set;

public class MediumAI extends AI {

    protected enum Direction {
        LEFT, RIGHT, UP, DOWN
    }

    protected final Set<Cell> shotCells;
    protected Direction direction;
    protected Cell lastShotCell;
    protected Cell firstTouchedCell;

    public MediumAI(String name, Grid grid) {
        super(name, grid);

        this.shotCells = new HashSet<>();
        this.direction = Direction.RIGHT;
        this.lastShotCell = null;
    }

    @Override
    public Cell shoot() {

        throwIfAllCellsShot();
        Cell cell = this.firstTouchedCell == null ? this.shootRandomly() : this.shootWithDirection();

        this.shotCells.add(cell);
        this.lastShotCell = cell;

        return cell;

    }

    private Cell shootRandomly() {

        Cell cell = this.grid.getRandomCell();
        while (this.shotCells.contains(cell)) cell = this.grid.getRandomCell();

        return cell;
    }

    public void notify(Effect effect) {

        if (effect == Effect.TOUCHED && this.firstTouchedCell == null) {
            // shot touched a new boat don't change strategy

            this.firstTouchedCell = this.lastShotCell;

        } else if (effect == Effect.SUNK) {
            // shot sunk the boat, reset values

            this.direction = Direction.RIGHT;
            this.firstTouchedCell = null;

        } else if (effect == Effect.NOTHING && this.firstTouchedCell != null) {
            // last shot missed but boat has not sunk yet, must change direction

            this.lastShotCell = this.firstTouchedCell;
            this.direction = calculateNewDirection();
        }

    }

    protected Direction calculateNewDirection() {

        var left = this.firstTouchedCell.leftNeighbour();
        var up = this.firstTouchedCell.upNeighbour();
        var down = this.firstTouchedCell.downNeighbour();

        Direction direction = Direction.RIGHT;

        if (this.direction != Direction.LEFT
                && left.isPresent()
                && !this.shotCells.contains(left.get())
                && this.grid.cellIsWithinGrid(left.get())) {

            direction = Direction.LEFT;

        } else if (this.direction != Direction.UP
                && up.isPresent()
                && !this.shotCells.contains(up.get())
                && this.grid.cellIsWithinGrid(up.get())) {

            direction = Direction.UP;

        } else if (this.direction != Direction.DOWN
                && down.isPresent()
                && !this.shotCells.contains(down.get())
                && this.grid.cellIsWithinGrid(down.get())) {

            direction = Direction.DOWN;
        }

        return direction;

    }

    protected Cell shootWithDirection() {

        var right = this.lastShotCell.rightNeighbour();
        var left = this.lastShotCell.leftNeighbour();
        var up = this.lastShotCell.upNeighbour();
        var down = this.lastShotCell.downNeighbour();
        Cell cell;


        if (this.direction == Direction.RIGHT
                && right.isPresent()
                && !this.shotCells.contains(right.get())
                && this.grid.cellIsWithinGrid(right.get())) {

            cell = right.get();

        } else if (this.direction == Direction.LEFT
                && left.isPresent()
                && !this.shotCells.contains(left.get())
                && this.grid.cellIsWithinGrid(left.get())) {

            cell = left.get();

        } else if (this.direction == Direction.UP
                && up.isPresent()
                && !this.shotCells.contains(up.get())
                && this.grid.cellIsWithinGrid(up.get())) {

            cell = up.get();

        } else if (this.direction == Direction.DOWN
                && down.isPresent()
                && !this.shotCells.contains(down.get())
                && this.grid.cellIsWithinGrid(down.get())) {

            cell = down.get();

        } else {

            this.lastShotCell = this.firstTouchedCell;
            this.direction = this.calculateNewDirection();
            cell = this.shootWithDirection();
        }

        return cell;
    }

    protected void throwIfAllCellsShot() {

        if (this.shotCells.size() >= this.grid.size())
            throw new RuntimeException("AI cannot find new cells to shoot. The " + this.grid.size() + " cells in the grid have been shot.");
    }
}
