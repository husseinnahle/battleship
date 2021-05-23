import Base.BattleShip;
import Base.BattleShipBomb;

import java.util.Scanner;

class Main {
    private static final String INTRO_MESSAGE =
            """
                             _           _   _   _           _     _      \s
                            | |         | | | | | |         | |   (_)     \s
                            | |__   __ _| |_| |_| | ___  ___| |__  _ _ __ \s
                            | '_ \\ / _` | __| __| |/ _ \\/ __| '_ \\| | '_ \\\s
                            | |_) | (_| | |_| |_| |  __/\\__ \\ | | | | |_) |
                            |_.__/ \\__,_|\\__|\\__|_|\\___||___/_| |_|_| .__/\s
                                                                    | |   \s
                                                                    |_|   \s
                    ----- GREETINGS SOLDIER! WELCOME TO BATTLESHIPS. -----
                    """;

    public static void main(String[] args) {
        String choice;
        BattleShip game = null;
        Scanner input = new Scanner(System.in);

        // Prompt user for game mode.
        System.out.println(INTRO_MESSAGE);

        // Select game mode.
        do {

            System.out.println("Please select the game mode -- (N)ormal | (B)ombs:");
            choice = input.nextLine();

            switch (choice) {
                case "N" -> game = new BattleShip();
                case "B" -> game = new BattleShipBomb();
                default -> System.out.println("Please select an appropriate game mode.");
            }
        } while (game == null);

        // Setup and play.
        game.setup();
        game.play();
    }
}