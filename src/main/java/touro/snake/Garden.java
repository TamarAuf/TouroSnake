package touro.snake;

import java.util.List;
import static java.lang.Math.abs;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.InputStream;


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
    private Clip clip;
    private final RockListManipulator rockListManipulator;

    public Garden(Snake snake, FoodFactory foodFactory, Clip clip, RockListManipulator rockListManipulator) {
        this.snake = snake;
        this.foodFactory = foodFactory;
        this.clip = clip;
        this.rockListManipulator = rockListManipulator;
        }

        public Snake getSnake () {
            return snake;
        }

        public Food getFood () {
            return food;
        }

        public List<Rock> getRocks () {
            return rockListManipulator.getRocks();
        }

        /**
         * Moves the snake, checks to see if food has been eaten and creates food if necessary
         *
         * @return true if the snake is still alive, otherwise false.
         */
        public boolean advance () {
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
        boolean moveSnake () {
            snake.move();

            //if collides with wall, self, or rock
            if (!snake.inBounds() || snake.eatsSelf() || rockHit()) {
                return false;
            }

            //if snake eats the food
            if (snake.getHead().equals(food)) {
                //add square to snake
                snake.grow();
                //make noise
                playSound();
                //remove food
                food = null;
                //increment rockCount
                rockCount++;
            }
            return true;
        }

        public boolean rockHit () {
            Square head = snake.getHead();
            for (Rock rock : rockListManipulator.getRocks()) {
                if (head.equals(rock)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Creates a Food if there isn't one, making sure it's not already on a Square occupied by the Snake or rock.
         */
        void createFoodIfNecessary () {
            //if snake ate food, create new one
            if (food == null) {
                food = foodFactory.newInstance();

                //if new food on snake or rock, put it somewhere else
                while (snake.contains(food) || food.equals(rockListManipulator.getLatestRock())) {
                    food = foodFactory.newInstance();
                }
            }
        }

        /**
         * Creates a Rock if there isn't one, making sure it's not already on a Square occupied by the Snake or food.
         */
        void createRockIfNecessary () {
            //if snake ate food, create additional rock
            if (rockCount > rockListManipulator.getRocks().size()) {
                rockListManipulator.addRock();
                Rock lastRock = rockListManipulator.getLatestRock();
                //get x and y distances between snake head and latest rock
                int xDistanceFromHead = abs(lastRock.getX() - snake.getHead().getX());
                int yDistanceFromHead = abs(lastRock.getY() - snake.getHead().getY());
                //if new rock on snake or food or too close in front of snake put it somewhere else
                while (snake.contains(lastRock) || food.equals(lastRock)
                        || (xDistanceFromHead < 2 && yDistanceFromHead == 0)
                        || (yDistanceFromHead < 2 && xDistanceFromHead == 0)) {
                    rockListManipulator.removeRock();
                    rockListManipulator.addRock();
                    lastRock = rockListManipulator.getLatestRock();
                }
            }
        }
     /*
     *Plays sound from.wav file found in resources folder
     */
        private void playSound () {
            try {
                clip.setMicrosecondPosition(0); //restart clip
                clip.start();

            } catch (Exception e) {
                System.out.println("Error found when trying to play EatNoise");
                e.printStackTrace();
            }
        }
    }
