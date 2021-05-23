package Base;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.lang.Math.abs;

public class Cell implements Comparable<Cell> {
    private final char x;
    private final int y;
    private boolean isHit;

    public Cell(char x, int y) {

        if (!Character.isLetter(x))
            throw new IllegalArgumentException("X coordinate must be a char representing a letter. \"" + x + "\" provided.");
        if (y < 1)
            throw new IllegalArgumentException("Y coordinate must be an integer greater than 0. \"" + y + "\" provided.");

        this.x = Character.toUpperCase(x);
        this.y = y;
        this.isHit = false;
    }

    public boolean isHit() {
        return this.isHit;
    }

    public void setIsHit() {
        this.isHit = true;
    }

    public boolean isLinearTo(Cell that) {
        return ((this.x - that.x == 0) || (this.y - that.y) == 0) && isAdjacentTo(that);
    }

    public List<Cell> getAdjacentCells() {
        List<Cell> list = new ArrayList<>();
        for (int dx = -1; dx <= 1; ++dx) {
            for (int dy = -1; dy <= 1; ++dy) {

                char coordinateX = (char) (x + dx);
                int coordinateY = (y + dy);

                if ((dx != 0 || dy != 0) && Character.isLetter(coordinateX) && coordinateY > 1)
                    list.add(new Cell(coordinateX, coordinateY));

            }
        }
        return list;
    }

    public Optional<Cell> rightNeighbour() {

        Optional<Cell> nextCell = Optional.empty();
        char coordinateX = (char) (this.x + 1);

        if (Character.isLetter(coordinateX))
            nextCell = Optional.of(new Cell(coordinateX, this.y));

        return nextCell;
    }

    public Optional<Cell> upNeighbour() {

        Optional<Cell> nextCell = Optional.empty();
        int coordinateY = this.y + 1;

        if (coordinateY >= 1)
            nextCell = Optional.of(new Cell(this.x, coordinateY));

        return nextCell;
    }

    public Optional<Cell> leftNeighbour() {

        Optional<Cell> nextCell = Optional.empty();
        char coordinateX = (char) (this.x - 1);

        if (Character.isLetter(coordinateX))
            nextCell = Optional.of(new Cell(coordinateX, this.y));

        return nextCell;
    }

    public Optional<Cell> downNeighbour() {
        Optional<Cell> nextCell = Optional.empty();
        int coordinateY = this.y - 1;

        if (coordinateY >= 1)
            nextCell = Optional.of(new Cell(this.x, coordinateY));

        return nextCell;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cell)) return false;
        Cell that = (Cell) o;
        return this.x == that.x && this.y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public int compareTo(Cell that) {
        if (this.x > that.x) return 1;
        if (this.x < that.x) return -1;

        return Integer.compare(this.y, that.y);

    }

    @Override
    public String toString() {
        return this.x + "," + this.y;
    }

    private boolean isAdjacentTo(Cell that) {
        return abs(this.x - that.x) <= 1 && abs(this.y - that.y) <= 1;
    }

}
