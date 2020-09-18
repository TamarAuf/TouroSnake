package touro.snake;

import java.util.Random;

/**
 * Factory class for creating new Rock objects within the Garden.
 */
public class RockFactory {

    /**
     * @return a new Rock with random coordinates in the Garden
     */

    private final Random rand = new Random();

    public Rock newInstance() {
        int randX = rand.nextInt(Garden.WIDTH);
        int randY = rand.nextInt(Garden.HEIGHT);
        return new Rock(randX,randY);
    }

}