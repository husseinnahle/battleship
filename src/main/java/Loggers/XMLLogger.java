package Loggers;

import Base.Cell;
import Base.Effect;
import Players.Player;

import java.util.logging.*;
import java.util.logging.Logger;

public class XMLLogger extends Loggers.Logger {
    private static final String LOG_FORMAT = "%5$s\n";
    private final Logger logger;

    public XMLLogger(String path) {
        super(path);

        // Create a new logger or get the last instance.
        this.logger = Logger.getLogger(TextLogger.class.getName());
        this.logger.setUseParentHandlers(false);
    }

    @Override
    public String log(Player p1, Player p2, Cell cell, Effect effect) {
        String logMessage;
        XMLFormatter xf = new XMLFormatter();

        try {
            // Setup the file handler.
            FileHandler fh = new FileHandler(path, true);
            System.setProperty("java.util.logging.SimpleFormatter.format", LOG_FORMAT);
            fh.setFormatter(new SimpleFormatter());
            this.logger.addHandler(fh);

            // Format and write the message.
            logMessage = String.format(DEFAULT_MSG, p1.getName(), p2.getName(), cell, effect);
            this.logger.info(xf.format(new LogRecord(Level.INFO, logMessage)));

            // Close file handler.
            fh.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to write to log file.");
        }
        return logMessage;
    }
}
