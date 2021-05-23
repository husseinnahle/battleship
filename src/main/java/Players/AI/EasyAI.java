package Players.AI;

import Base.Cell;
import Base.Effect;
import Base.Grid;

public class EasyAI extends AI {

    public EasyAI(String name, Grid grid) {
        super(name, grid);
    }

    public Cell shoot() {

        return this.grid.getRandomCell();

    }

    @Override
    public void notify(Effect effect) {

    }

}
