package Loggers;

public class LoggerFactory {
    private static final String XML_EXTENSION = ".xml";
    private static final String TEXT_EXTENSION = ".txt";
    private static final String JSON_EXTENSION = ".json";

    public static Logger getLogger(String path) {
        Logger logger;

        // Check path name for the correct logger.
        if (path.endsWith(XML_EXTENSION)) {
            logger = new XMLLogger(path);
        } else if (path.endsWith(TEXT_EXTENSION)) {
            logger = new TextLogger(path);
        } else if (path.endsWith(JSON_EXTENSION)) {
            logger = new JSONLogger(path);
        } else {
            throw new IllegalArgumentException("%s does not have a supported extension.".formatted(path));
        }

        return logger;
    }
}
