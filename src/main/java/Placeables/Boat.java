package Placeables;

import Base.Effect;

public class Boat implements Placeable {

    private final BoatType type;
    private int numberOfHits;

    public Boat(BoatType type) {
        this.type = type;
        this.numberOfHits = 0;
    }

    @Override
    public Effect receiveShot() {

        this.numberOfHits++;

        if (isSunk()) return Effect.SUNK;

        return Effect.TOUCHED;
    }

    @Override
    public int getCapacity() {
        return type.getCapacity();
    }

    public boolean isSunk() {
        return numberOfHits >= type.getCapacity();
    }

    @Override
    public String toString() {
        return this.type.toString();
    }
}
