package Parsers;

import Base.Cell;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CellsParser implements Parser<List<Cell>> {

    @Override
    public List<Cell> fromString(String input) throws ParseException {

        String formattedInput = input.trim();
        List<Cell> cells = new ArrayList<>();

        int i = 0;
        while (i < formattedInput.length()) {

            while (i < formattedInput.length() && formattedInput.charAt(i) == ' ') i++;

            char coordinateX = formattedInput.charAt(i);
            i++;

            StringBuilder buffer = new StringBuilder();
            while (i < formattedInput.length() && Character.isDigit(formattedInput.charAt(i))) {
                buffer.append(formattedInput.charAt(i));
                i++;
            }

            String coordinateY = buffer.toString();

            if (Character.isLetter(coordinateX) && !coordinateY.isEmpty()) {

                cells.add(new Cell(coordinateX, Integer.parseInt(coordinateY)));

            } else {

                throw new ParseException("\"" + coordinateX + coordinateY + "\" is not a valid coordinate for a cell.", i);

            }

        }

        return Collections.unmodifiableList(cells);
    }
}
