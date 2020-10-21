package touro.snake;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.InputStream;
import java.util.Random;


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
    private final Random rand = new Random();
    private final List<Rock> rocks = new ArrayList<>();
    private Rock latestRock;

    public Garden(Snake snake, FoodFactory foodFactory, Clip clip)  {
        this.snake = snake;
        this.foodFactory = foodFactory;
        this.clip = clip;
    }

    public Snake getSnake() {
        return snake;
    }

    public Food getFood() {
        return food;
    }

    public List<Rock> getRocks(){return rocks;}

    public void addRock() {
        latestRock = new Rock(rand.nextInt(Garden.WIDTH), rand.nextInt(Garden.HEIGHT));
        rocks.add(latestRock);
    }

    public void removeRock() {
        rocks.remove(latestRock);
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
            //make noise
            playSound();
            //remove food
            food = null;
            //increment rockCount
            rockCount++;
        }
        return true;
    }

    public boolean rockHit() {
        Square head = snake.getHead();
        for (Rock rock : rocks) {
            if (head.equals(rock)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a Food if there isn't one, making sure it's not already on a Square occupied by the Snake or rock.
     */
    void createFoodIfNecessary() {
        //if snake ate food, create new one
        if (food == null) {
            food = foodFactory.newInstance();

            //if new food on snake or rock, put it somewhere else
            while (snake.contains(food) || food.equals(latestRock)) {
                food = foodFactory.newInstance();
            }
        }
    }

    /**
     * Creates a Rock if there isn't one, making sure it's not already on a Square occupied by the Snake or food.
     */
    void createRockIfNecessary() {
        //if snake ate food, create additional rock
        if (rockCount > rocks.size()) {
            addRock();
            //get x and y distances between snake head and latest rock
            int xDistanceFromHead = abs(latestRock.getX() - snake.getHead().getX());
            int yDistanceFromHead = abs(latestRock.getY() - snake.getHead().getY());
            //if new rock on snake or food or too close in front of snake put it somewhere else
            while (snake.contains(latestRock) || food.equals(latestRock)
                    || (xDistanceFromHead < 2 && yDistanceFromHead == 0)
                    || (yDistanceFromHead < 2 && xDistanceFromHead == 0)) {
                removeRock();
                addRock();
            }
        }
    }

    /*
     *Plays sound from.wav file found in resources folder
     */
    private void playSound() {
        try {
            clip.setMicrosecondPosition(0); //restart clip
            clip.start();

        } catch (Exception e) {
            System.out.println("Error found when trying to play EatNoise");
            e.printStackTrace();
        }
    }
}