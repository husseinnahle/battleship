package Loggers;

import Base.Cell;
import Base.Effect;
import Players.Player;

import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class TextLogger extends Loggers.Logger {
    private static final String LOG_FORMAT = "[%1$tFs %1$tH:%1$tM:%1$tS] [%4$s] %5$s\n";
    private final Logger logger;

    public TextLogger(String path) {
        super(path);
        // Create a new logger or get the last instance.
        this.logger = Logger.getLogger(TextLogger.class.getName());
        this.logger.setUseParentHandlers(false);
    }

    @Override
    public String log(Player p1, Player p2, Cell cell, Effect effect) {
        String logMessage;
        try {
            // Setup the file handler.
            System.setProperty("java.util.logging.SimpleFormatter.format", LOG_FORMAT);
            FileHandler fh = new FileHandler(path, true);
            fh.setFormatter(new SimpleFormatter());
            this.logger.addHandler(fh);

            // Write to file.
            logMessage = String.format(DEFAULT_MSG, p1.getName(), p2.getName(), cell, effect);
            this.logger.info(logMessage);

            // Close file handler.
            fh.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to write to log file.");
        }
        return logMessage;
    }


}
