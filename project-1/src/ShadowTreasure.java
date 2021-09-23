import bagel.*;


import java.io.IOException;



/**
 * An example Bagel game.
 */
public class ShadowTreasure extends AbstractGame {

    // for rounding double number; use this to print the location of the player
    private Entity entity = new Entity();
    private int frame = 0;
    @Override
    public void update(Input input) {
        // Logic to update the game, as per specification must go here
        if (input.wasPressed(Keys.ESCAPE)) {
            Window.close();
        } else {
            if (frame % 10 != 0) {
                entity.drawGame();
                frame += 1;
            } else {
                frame += 1;
                try {
                    entity.algorithmOne();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    /**
     * The entry point for the program.
     */
    public static void main(String[] args) throws IOException {
        ShadowTreasure game = new ShadowTreasure();
        game.run();
    }
}
