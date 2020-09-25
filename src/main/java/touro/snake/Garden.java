package touro.snake;

import java.util.List;

import static java.lang.Math.abs;

/**
 * A model that contains the Snake and Food and is responsible for logic of moving the Snake,
 * seeing that food has been eaten and generating new food.
 */
public class Garden {

    public static final int WIDTH = 100;
    public static final int HEIGHT = 40;
    private int rockCount = 0;

    private final Snake snake;
    private final FoodFactory foodFactory;
    private Food food;
    private final RockFactory rockFactory;

    public Garden(Snake snake, FoodFactory foodFactory, RockFactory rockFactory) {
        this.snake = snake;
        this.foodFactory = foodFactory;
        this.rockFactory = rockFactory;
    }

    public Snake getSnake() {
        return snake;
    }

    public Food getFood() {
        return food;
    }

    public List<Rock> getRocks() {
        return rockFactory.getRocks();
    }

    /**
     * Moves the snake, checks to see if food has been eaten and creates food if necessary
     *
     * @return true if the snake is still alive, otherwise false.
     */
    public boolean advance() {
        if (moveSnake()) {
            createFoodIfNecessary();
            createRockIfNecessary();
            return true;
        }
        return false;
    }

    /**
     * Moves the Snake, eats the Food or collides with the wall (edges of the Garden), or eats self.
     *
     * @return true if the Snake is still alive, otherwise false.
     */
    boolean moveSnake() {
        snake.move();

        //if collides with wall, self, or rock
        if (!snake.inBounds() || snake.eatsSelf() || rockHit()) {
            return false;
        }

        //if snake eats the food
        if (snake.getHead().equals(food)) {
            //add square to snake
            snake.grow();
            //remove food
            food = null;
            //increment rockCount
            rockCount++;
        }
        return true;
    }

    public boolean rockHit() {
        boolean retVal = false;
        Square head = snake.getHead();
        for(Rock rock : rockFactory.getRocks()) {
            retVal = head.equals(rock);
        }
        return retVal;
    }

    /**
     * Creates a Food if there isn't one, making sure it's not already on a Square occupied by the Snake or rock.
     */
    void createFoodIfNecessary() {
        //if snake ate food, create new one
        if (food == null) {
            food = foodFactory.newInstance();

            //if new food on snake or rock, put it somewhere else
            while (snake.contains(food) || food.equals(rockFactory.getLatestRock())) {
                food = foodFactory.newInstance();
            }
        }
    }

    /**
     * Creates a Rock if there isn't one, making sure it's not already on a Square occupied by the Snake or food.
     */
    void createRockIfNecessary() {
        //if snake ate food, create new rock
        if (rockCount > rockFactory.getRocks().size()) {
            rockFactory.addRock();
            int xDistanceFromFace = abs(rockFactory.getLatestRock().getX() - snake.getHead().getX());
            int yDistanceFromFace = abs(rockFactory.getLatestRock().getY() - snake.getHead().getY());
            //if new rock on snake or food or too close in front of snake put it somewhere else
            while (snake.contains(rockFactory.getLatestRock()) || food.equals(rockFactory.getLatestRock())
                    || (xDistanceFromFace<2 && yDistanceFromFace == 0)
                    || (yDistanceFromFace<2 && xDistanceFromFace == 0)) {
                rockFactory.removeRock();
                rockFactory.addRock();
            }
        }
    }

}
