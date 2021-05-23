package Base;

import Exceptions.InvalidPlacementException;
import Placeables.Boat;
import Placeables.Placeable;

import java.util.*;

public class Grid {
    private final Map<Cell, Optional<Placeable>> items;
    private final char length;
    private final int width;

    public Grid(char length, int width) {

        if (!Character.isLetter(length))
            throw new IllegalArgumentException("length must be a char representing a letter. \"" + length + "\" provided.");
        if (width < 1)
            throw new IllegalArgumentException("width must be an integer greater than 0. \"" + width + "\" provided.");

        this.length = Character.toUpperCase(length);
        this.width = width;
        this.items = new HashMap<>();
        setGrid(length, width);
    }

    public int size() {
        return this.width * (this.length - 64);
    }

    public void setGrid(char length, int width) {

        for (int i = 1; i < width + 1; i++) {

            for (char j = 'A'; j < length + 1; j++) {

                this.items.put(new Cell(j, i), Optional.empty());

            }

        }

    }

    private Optional<Cell> getActualCell(Cell cell) {

        return this.items.keySet()
                .stream()
                .filter(c -> c.equals(cell))
                .findFirst();
    }

    public void receiveItem(List<Cell> cells, Placeable item) throws InvalidPlacementException {

        throwIfCellsAreNotWithinGrid(cells);
        throwIfDuplicateCells(cells);
        throwIfOccupiedCells(cells);
        throwIfOccupiedAdjacentCells(cells);
        throwIfNotLinearCells(cells);
        throwIfInvalidCapacity(cells, item);

        cells.forEach(e -> this.items.put(e, Optional.of(item)));
    }

    private void throwIfDuplicateCells(List<Cell> cells) throws InvalidPlacementException {
        var map = new HashSet<Cell>();

        for (var cell : cells) {
            if (!map.add(cell)) {
                throw new InvalidPlacementException("Invalid placement! ", "Cell (" + cell.toString() +
                        ") is present more than once. Duplicates are not allowed.");
            }
        }
    }

    private void throwIfOccupiedCells(List<Cell> cells) throws InvalidPlacementException {

        for (Cell e : cells) {

            if (this.items.get(e).isPresent())
                throw new InvalidPlacementException("Invalid placement! ", "Cell at (" + e.toString() + ") is occupied.");

        }

    }

    private void throwIfOccupiedAdjacentCells(List<Cell> cells) throws InvalidPlacementException {

        for (Cell e : cells) {

            for (Cell cell : e.getAdjacentCells()) {

                if (items.containsKey(cell) && items.get(cell).isPresent())
                    throw new InvalidPlacementException("Invalid placement! ", "Adjacent cell at (" + cell.toString() + ") is occupied.");

            }

        }

    }

    private void throwIfNotLinearCells(List<Cell> cells) throws InvalidPlacementException {

        List<Cell> list = new ArrayList<>(cells);

        for (int i = 0; i < list.size() - 1; i++) {

            Cell c1 = list.get(i);
            Cell c2 = list.get(i + 1);

            if (!c1.isLinearTo(c2)) {

                String message = String.format("Cell at (%s) is not linear to cell at (%s)", c1.toString(), c2.toString());
                throw new InvalidPlacementException("Invalid placement! ", message);

            }

        }

    }

    private void throwIfInvalidCapacity(List<Cell> cells, Placeable item) throws InvalidPlacementException {

        String message = String.format("Number of cells (%d) does not match %s's size (%d).",
                cells.size(), item.toString(), item.getCapacity());

        if (item.getCapacity() != cells.size())
            throw new InvalidPlacementException("Invalid placement! ", message);

    }

    private void throwIfCellsAreNotWithinGrid(List<Cell> cells) throws InvalidPlacementException {

        for (var cell : cells) {

            String message = String.format("Cell %s is not within the grid limits.",
                    cells.toString());

            if (!this.cellIsWithinGrid(cell)) {
                throw new InvalidPlacementException("Invalid placement! ", message);
            }
        }
    }

    public Effect receiveShot(Cell cell) throws InvalidPlacementException {

        if (!this.items.containsKey(cell))
            throw new InvalidPlacementException("Invalid cell! ", "");

        Effect effect = Effect.NOTHING;
        Optional<Cell> actualCell = getActualCell(cell);

        if (actualCell.isPresent() && !actualCell.get().isHit()) {

            actualCell.get().setIsHit();
            if (this.items.get(cell).isPresent()) effect = this.items.get(cell).get().receiveShot();

        }

        return effect;
    }

    private Set<Boat> getAvailableBoats() {

        Set<Boat> boats = new HashSet<>();

        for (Optional<Placeable> item : this.items.values()) {

            if (item.isPresent() && item.get() instanceof Boat)
                boats.add((Boat) item.get());

        }

        return boats;
    }

    public int getNumberOfFloatingBoats() {

        int numberOfFloatingBoats = 0;

        for (Boat boat : getAvailableBoats()) {

            if (!boat.isSunk())
                numberOfFloatingBoats++;

        }

        return numberOfFloatingBoats;
    }

    public boolean cellIsWithinGrid(Cell c) {

        return this.items.containsKey(c);

    }

    public Cell getRandomCell() {
        List<Cell> cells = new ArrayList<>(this.items.keySet());
        return cells.get(new Random().nextInt(cells.size()));
    }

    public Set<Cell> getAvailableCells() {

        Set<Cell> availableCells = new HashSet<>();

        for (Cell cell : this.items.keySet()) {

            if (this.items.get(cell).isEmpty()) {

                boolean isAvailable = true;

                for (Cell e : cell.getAdjacentCells()) {

                    if (items.containsKey(e) && items.get(e).isPresent()) {
                        isAvailable = false;
                        break;
                    }

                }

                if (isAvailable)
                    availableCells.add(cell);

            }

        }

        return availableCells;
    }

}
