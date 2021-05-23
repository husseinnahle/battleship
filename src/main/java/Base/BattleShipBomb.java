package Base;

import Placeables.Bomb;
import Players.Player;

public class BattleShipBomb extends BattleShip {

    protected static final int DEFAULT_BOMB_NBR = 5;

    @Override
    public void setup() {
        super.setup();
        setupBombs();
    }

    @Override
    public void play() {

        Player shooter = this.human;
        Player receiver = this.computer;

        do {

            Effect effect = makePlayerShoot(shooter, receiver);

            // Switch turns
            shooter = receiver;
            receiver = switchPlayer(receiver);

            while (effect == Effect.SKIP_TURN) {

                System.out.println(receiver.getName() + " touched a bomb!");

                effect = makePlayerShoot(shooter, receiver);

                if (effect == Effect.SKIP_TURN) {

                    // Switch turns
                    shooter = receiver;
                    receiver = switchPlayer(receiver);

                }

            }

        } while (receiver.getNumberOfFloatingBoats() > 0);

        System.out.println(shooter + " wins!");
    }

    private void setupBombs() {

        Player[] players = new Player[]{this.human, this.computer};

        for (Player p : players) {

            for (int i = 0; i < DEFAULT_BOMB_NBR; i++)
                p.placeItem(new Bomb());

        }

    }
}
