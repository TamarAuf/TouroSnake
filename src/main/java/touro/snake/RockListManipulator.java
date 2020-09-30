package touro.snake;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RockListManipulator {

    /**Adds rock to rock list
     * Removes last rock from rock list
     */

    private final Random rand = new Random();
    private final List<Rock> rocks = new ArrayList<>();
    private Rock latestRock;

    public void addRock() {
        latestRock = new Rock(rand.nextInt(Garden.WIDTH), rand.nextInt(Garden.HEIGHT));
        rocks.add(latestRock);
    }

    public void removeRock() {
        rocks.remove(latestRock);
    }

    public Rock getLatestRock(){
        return latestRock;
    }

    public List<Rock> getRocks() {
        return rocks;
    }
}
