package touro.snake;

import touro.snake.strategy.astar.AstarStrategy;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.InputStream;

public class SnakeMain {

    public static void main(String[] args) {

        // Set up all class dependencies here.

        SnakeHeadStateMachine snakeHeadStateMachine = new SnakeHeadStateMachine(Direction.West);
        AstarStrategy astar = new AstarStrategy();
        Snake snake = new Snake(snakeHeadStateMachine, astar);
        FoodFactory foodFactory = new FoodFactory();
        try {
            InputStream inputStream = Garden.class.getClassLoader().getResourceAsStream("EatNoise.wav");
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(inputStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            Garden garden = new Garden(snake, foodFactory, clip);
            GardenView gardenView = new GardenView(garden, astar);
            SnakeKeyListener snakeKeyListener = new SnakeKeyListener(snake);

            GardenThread thread = new GardenThread(garden, gardenView);
            thread.start();
            BackgroundSound backgroundSound = new BackgroundSound();
            new SnakeFrame(gardenView, snakeKeyListener, backgroundSound).setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
