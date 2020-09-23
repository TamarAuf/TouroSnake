package touro.snake;

/**
 * A model that contains the Snake and Food and is responsible for logic of moving the Snake,
 * seeing that food has been eaten and generating new food.
 */
public class Garden {

    public static final int WIDTH = 100;
    public static final int HEIGHT = 40;

    private final Snake snake;
    private final FoodFactory foodFactory;
    private Food food;
    private final RockFactory rockFactory;
    private Rock rock;

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

    public Rock getRock() {
        return rock;
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
            rock = null;
        }
        return true;
    }

    public boolean rockHit() {
        Square head = snake.getHead();

        return head.equals(rock);
    }

    /**
     * Creates a Food if there isn't one, making sure it's not already on a Square occupied by the Snake or rock.
     */
    void createFoodIfNecessary() {
        //if snake ate food, create new one
        if (food == null) {
            food = foodFactory.newInstance();

            //if new food on snake or rock, put it somewhere else
            while (snake.contains(food) || food.equals(rock)) {
                food = foodFactory.newInstance();
            }
        }
    }

    /**
     * Creates a Rock if there isn't one, making sure it's not already on a Square occupied by the Snake or food.
     */
    void createRockIfNecessary() {
        //if snake ate food, create new rock
        if (rock == null) {
            rock = rockFactory.newInstance();

            //if new rock on snake or food, put it somewhere else
            while (snake.contains(rock) || rock.equals(food)) {
                rock = rockFactory.newInstance();
            }
        }
    }

}
