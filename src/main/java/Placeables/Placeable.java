package Placeables;

import Base.Effect;

public interface Placeable {
    Effect receiveShot();
    int getCapacity();
    String toString();
}
