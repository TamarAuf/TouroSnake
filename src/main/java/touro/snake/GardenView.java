package touro.snake;

import touro.snake.strategy.astar.AstarStrategy;

import javax.swing.*;
import java.awt.*;

public class GardenView extends JComponent {

    private final Garden garden;
    private final AstarStrategy astar;
    public static final int CELL_SIZE = 10;

    public GardenView(Garden garden, AstarStrategy astar) {
        this.garden = garden;
        this.astar = astar;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintGrass(g);
        paintFood(g);
        paintSnake(g);
        paintPath(g);
        paintSearchSpace(g);
    }

    void paintGrass(Graphics g) {
        // Berger
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    void paintSnake(Graphics g) {
        g.setColor(Color.RED);
        for (Square s : garden.getSnake().getSquares()) {
            g.fillRect(s.getX() * CELL_SIZE, s.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
        }
    }

    void paintFood(Graphics g) {
        // Berger
        if (garden.getFood() != null) {
            Food food = garden.getFood();
            g.setColor(Color.LIGHT_GRAY);

            int x = food.getX() * CELL_SIZE;
            int y = food.getY() * CELL_SIZE;
            g.fillRect(x, y, CELL_SIZE, CELL_SIZE);
        }
    }

    void paintPath(Graphics g) {
        g.setColor(Color.CYAN);
        for (Square s : astar.getPath()) {
            if (!garden.getSnake().contains(s)) {
                g.fillRect(s.getX() * CELL_SIZE, s.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

    void paintSearchSpace(Graphics g) {
        g.setColor(Color.BLUE);
        for (Square s : astar.getSearchSpace()) {
            if (!garden.getSnake().contains(s)) {
                g.fillRect(s.getX() * CELL_SIZE, s.getY() * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
    }

}
