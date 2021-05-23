package Base;

import Exceptions.InvalidPlacementException;
import Loggers.Logger;
import Loggers.LoggerFactory;
import Placeables.Boat;
import Placeables.BoatType;
import Players.AI.EasyAI;
import Players.AI.HardAI;
import Players.AI.MediumAI;
import Players.Human;
import Players.Player;

import java.util.Scanner;

public class BattleShip {
    protected Player human;
    protected Player computer;
    protected Logger logger;
    protected static final char DEFAULT_GRID_SIZE_X = 'J';
    protected static final int DEFAULT_GRID_SIZE_Y = 10;
    protected static BoatType[] DEFAULT_BOATS = {BoatType.CARRIER, BoatType.BATTLESHIP, BoatType.CRUISER, BoatType.CRUISER, BoatType.DESTROYER};

    public BattleShip() {
        this.logger = null;
        this.computer = null;
        this.human = null;
    }

    /**
     * Function to setup the game by running the required configuration methods.
     */
    public void setup() {
        setupOptions();
        setupFleet();
        setupSubscriptions();
    }

    /**
     * Configure the game settings: Player, Difficulty, Logger (to be added).
     */
    private void setupOptions() {
        Scanner input = new Scanner(System.in);

        setHumanPlayer(input);
        setDifficulty(input);
        setLogger(input);
    }

    private void setupSubscriptions() {

        this.human.subscribe(computer);
        this.computer.subscribe(human);
    }

    /**
     * Configure the placement of the fleet on each of the player's grid.
     */
    private void setupFleet() {
        Player[] players = new Player[]{this.human, this.computer};

        // Inform the user of the placements rules and format.
        System.out.println("\nYou will now have to place your ships on your grid!");
        System.out.println("Please provide the series of coordinates for each of your ships.");
        System.out.println("For example, a valid input for a size 4 ship would be 'A1 A2 A3 A4'");
        System.out.println("None of the cells provided may be directly adjacent to another boat.");
        System.out.println("First coordinate must be within A and " + DEFAULT_GRID_SIZE_X + ".");
        System.out.println("Second coordinate must be within 1 and " + DEFAULT_GRID_SIZE_Y + ".\n");

        // Iterate over each player and place each items.
        for (Player p : players) {
            for (BoatType b : DEFAULT_BOATS) {
                p.placeItem(new Boat(b));
            }
        }
    }

    private void setLogger(Scanner input) {

        String path;

        while (true) {
            try {
                System.out.println("Enter the relative path/filename for the log file. Valid log extensions are TXT, XML and JSON:");
                path = input.nextLine();
                this.logger = LoggerFactory.getLogger(path);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void setHumanPlayer(Scanner input) {

        String name;

        do {

            // Initialize the human player.
            System.out.println("Please enter your name:");
            name = input.nextLine();

            if (name.strip().isEmpty()) {
                System.out.println("Name cannot be empty.");
            } else {
                this.human = new Human(name, new Grid(DEFAULT_GRID_SIZE_X, DEFAULT_GRID_SIZE_Y), input);
            }

        } while (this.human == null);

    }


    private void setDifficulty(Scanner input) {

        String difficulty;

        // Select difficulty
        do {

            System.out.println("Select the AI difficulty level -- (E)asy | (N)ormal | (H)ard :");
            difficulty = input.nextLine();

            switch (difficulty) {
                case "E" -> this.computer = new EasyAI("EASY AI", new Grid(DEFAULT_GRID_SIZE_X, DEFAULT_GRID_SIZE_Y));
                case "N" -> this.computer = new MediumAI("MEDIUM AI", new Grid(DEFAULT_GRID_SIZE_X, DEFAULT_GRID_SIZE_Y));
                case "H" -> this.computer = new HardAI("HARD AI", new Grid(DEFAULT_GRID_SIZE_X, DEFAULT_GRID_SIZE_Y));
                default -> System.out.println("Invalid chosen difficulty.");
            }

        } while (this.computer == null);

    }

    /**
     * Main function to run the player turns.
     */
    public void play() {

        Player shooter = this.human;
        Player receiver = this.computer;

        do {

            makePlayerShoot(shooter, receiver);

            // Switch turns
            shooter = receiver;
            receiver = switchPlayer(receiver);

        } while (receiver.getNumberOfFloatingBoats() > 0);

        // Winner
        System.out.println(shooter.getName() + " wins!");

    }

    protected Effect makePlayerShoot(Player shooter, Player receiver) {

        Cell cell;
        Effect effect;

        while (true) {

            try {

                cell = shooter.shoot();
                effect = receiver.receiveShot(cell);
                break;

            } catch (InvalidPlacementException e) {
                System.out.println(e.getMessage());
            }

        }

        System.out.println(this.logger.log(shooter, receiver, cell, effect));

        return effect;
    }

    protected Player switchPlayer(Player receiver) {

        return receiver == this.human ? this.computer : this.human;

    }

}
