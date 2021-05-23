package ParsersTests;

import Base.Cell;
import Parsers.CellsParser;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class CellsParserTest {

    static CellsParser cellsParser;

    @BeforeClass
    public static void InitCellsParser() {
        cellsParser = new CellsParser();
    }

    @Test
    public void StringHasZeroCellsTest() throws java.text.ParseException {

        var input = "     \n";
        int sizeExpected = 0;

        var cellsResult = cellsParser.fromString(input);
        int sizeResult = cellsResult.size();

        assertEquals(sizeExpected, sizeResult);
    }

    @Test
    public void StringHasOneCellTest() throws java.text.ParseException {

        var input = " Z123 ";
        int sizeExpected = 1;


        var cellsResult = cellsParser.fromString(input);
        int sizeResult = cellsResult.size();

        assertEquals(sizeExpected, sizeResult);
    }

    @Test
    public void StringHasTheseFiveCellsTest() throws java.text.ParseException {

        var input = " A10  B2 Z3 D4 E100 ";

        Cell[] cellsExpected = {
                new Cell('A', 10),
                new Cell('B', 2),
                new Cell('Z', 3),
                new Cell('D', 4),
                new Cell('E', 100)
        };

        var cellsResult = cellsParser.fromString(input);

        assertArrayEquals(cellsExpected, cellsResult.toArray());
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void StringHasAnInvalidCell() throws java.text.ParseException {
        exceptionRule.expect(java.text.ParseException.class);
        exceptionRule.expectMessage("100");
        var input = " A10  B2 C3 D4 100 ";

        Cell[] cellsExpected = {
                new Cell('A', 10),
                new Cell('B', 2),
                new Cell('C', 3),
                new Cell('D', 4),
                new Cell('E', 100)
        };

        cellsParser.fromString(input);

    }

}
