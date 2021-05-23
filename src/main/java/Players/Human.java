package Players;

import Base.Cell;
import Base.Effect;
import Base.Grid;
import Parsers.CellsParser;
import Parsers.Parser;
import Placeables.Placeable;

import java.util.List;
import java.util.Scanner;

public class Human extends Player {

    private final Parser<List<Cell>> parser;
    private final Scanner scanner;

    public Human(String name, Grid grid, Scanner scanner) {
        super(name, grid);
        this.parser = new CellsParser();
        this.scanner = scanner;
    }

    @Override
    public Cell shoot() {

        Cell cell;

        while (true) {

            System.out.println("Enter one cell coordinates you want to shoot.");
            String input = this.scanner.nextLine();

            try {

                var cells = parser.fromString(input);

                if (cells.size() != 1) {

                    System.out.println("Only one cell should be entered!\n");

                } else {

                    cell = cells.get(0);
                    break;

                }

            } catch (Exception e) {

                System.out.println(e.getMessage());

            }

        }

        return cell;
    }

    @Override
    public void placeItem(Placeable item) {

        while (true) {

            System.out.printf("Enter the cell coordinate(s) where you want to place the following item -- %s of size %d%n",
                    item.toString(),
                    item.getCapacity());

            try {

                String input = this.scanner.nextLine();
                var cells = parser.fromString(input);
                this.grid.receiveItem(cells, item);
                break;

            } catch (Exception e) {

                System.out.println(e.getMessage());

            }

        }

    }

    @Override
    public void notify(Effect effect) {
    }
}
