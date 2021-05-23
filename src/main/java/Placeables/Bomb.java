package Placeables;

import Base.Effect;

public class Bomb implements Placeable {

    private final int capacity = 1;

    @Override
    public Effect receiveShot() {
        return Effect.SKIP_TURN;
    }

    @Override
    public int getCapacity() {
        return this.capacity;
    }

    @Override
    public String toString() {
        return "Bomb";
    }
}
