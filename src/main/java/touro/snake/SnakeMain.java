package touro.snake;

import java.awt.*;

public class SnakeMain {

    public static void main(String[] args) {

        // Set up all class dependencies here.

        SnakeHeadStateMachine snakeHeadStateMachine = new SnakeHeadStateMachine(Direction.West);
        Snake snake = new Snake(snakeHeadStateMachine);
        FoodFactory foodFactory = new FoodFactory();
        RockFactory rockFactory = new RockFactory();
        Garden garden = new Garden(snake, foodFactory, rockFactory);
        GardenView gardenView = new GardenView(garden);
        SnakeKeyListener snakeKeyListener = new SnakeKeyListener(snake);

        GardenThread thread = new GardenThread(garden, gardenView);
        thread.start();

        new SnakeFrame(gardenView, snakeKeyListener).setVisible(true);
    }

}
