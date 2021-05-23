package Loggers;

import Players.Player;
import Base.Cell;
import Base.Effect;

public abstract class Logger {
    protected static final String DEFAULT_MSG = "%s attacks %s at (%s) resulting in %s";
    protected String path;

    public Logger(String path) {
        this.path = path;
    }

    public abstract String log(Player p1, Player p2, Cell cell, Effect effect);
}
