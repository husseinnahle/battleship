package Players;

import Base.*;
import Exceptions.InvalidPlacementException;
import Placeables.Placeable;

import java.util.HashSet;
import java.util.Set;

public abstract class Player extends Observer implements Observable {

    protected String name;
    protected Grid grid;
    private final Set<Observer> observers;

    public Player(String name, Grid grid) {
        this.name = name;
        this.grid = grid;
        this.observers = new HashSet<>();
    }

    @Override
    public void subscribe(Observer observer) {

        if (observer != this) this.observers.add(observer);
    }

    @Override
    public void unsubscribe(Observer observer) {
        this.observers.remove(observer);
    }

    public String getName() {
        return name;
    }

    public abstract Cell shoot();

    public abstract void placeItem(Placeable item);

    public int getNumberOfFloatingBoats() {
        return this.grid.getNumberOfFloatingBoats();
    }

    public Effect receiveShot(Cell cell) throws InvalidPlacementException {
        Effect effect = this.grid.receiveShot(cell);

        for (var observer : observers) {
            observer.notify(effect);
        }

        return effect;
    }

    public abstract void notify(Effect effect);

}
