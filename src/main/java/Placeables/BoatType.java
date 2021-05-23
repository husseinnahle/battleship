package Placeables;

public enum BoatType {
    CARRIER	(5),
    BATTLESHIP	(4),
    CRUISER	(3),
    DESTROYER (2)
    ;

    final private int capacity;

    BoatType(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }
}
