package LoggerTests;

import Base.Cell;
import Base.Effect;
import Base.Grid;
import Loggers.Logger;
import Loggers.TextLogger;
import Players.AI.EasyAI;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class TextLoggerTest extends TestCase {
    private static final String LOG_FILE_NAME = "test.txt";

    @After
    public void tearDown(){
        new File(LOG_FILE_NAME).delete();
    }

    @Test
    public void testLogFileHasBeenCreated() throws IOException {
        // Objects
        EasyAI p1 = new EasyAI("P1", new Grid('J', 10));
        EasyAI p2 = new EasyAI("P2", new Grid('J', 10));
        Cell c = new Cell('A', 4);
        Logger logger = new TextLogger(LOG_FILE_NAME);

        // Test
        logger.log(p1, p2, c, Effect.TOUCHED);
        assertTrue("Log file has been created.", new File(LOG_FILE_NAME).exists());
    }

    @Test
    public void testLogReturnsCorrectStringFormat() throws IOException {
        // Objects
        EasyAI p1 = new EasyAI("P1", new Grid('J', 10));
        EasyAI p2 = new EasyAI("P2", new Grid('J', 10));
        Cell c = new Cell('A', 4);
        Logger logger = new TextLogger(LOG_FILE_NAME);

        // Test
        String result = logger.log(p1, p2, c, Effect.TOUCHED);
        assertEquals("P1 attacks P2 at (A,4) resulting in TOUCHED", result);
    }

    @Test
    public void testLogAppendMultipleLinesInFile() throws IOException {
        // Objects
        EasyAI p1 = new EasyAI("P1", new Grid('J', 10));
        EasyAI p2 = new EasyAI("P2", new Grid('J', 10));
        Cell c1 = new Cell('A', 4);
        Cell c2 = new Cell('D', 8);
        Logger logger = new TextLogger(LOG_FILE_NAME);

        // Write to file
        String expected = logger.log(p1, p2, c1, Effect.TOUCHED);
        String expected2 = logger.log(p1, p2, c2, Effect.SKIP_TURN);

        // Read file.
        Scanner output = new Scanner(new FileReader(LOG_FILE_NAME));
        String result = output.nextLine();
        String result2 = output.nextLine();
        output.close();

        // Compare results.
        assertTrue(result.contains(expected));
        assertTrue(result2.contains(expected2));
    }
}