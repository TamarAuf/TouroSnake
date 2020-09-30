package touro.snake;

import org.junit.Test;
import java.util.ArrayList;
import org.mockito.Mockito;
import javax.sound.sampled.Clip;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GardenTest {

    @Test
    public void moveSnake() {
        /*
        Tests that snake moves and that when snake's move does not result
        in death.
         */
        //given
        Snake snake = mock(Snake.class);
        FoodFactory foodFactory = mock(FoodFactory.class);
        RockListManipulator rockListManipulator = mock(RockListManipulator.class);
        Clip clip = mock(Clip.class);
        Garden garden = new Garden(snake, foodFactory, clip, rockListManipulator);

        doReturn(true).when(snake).inBounds();
        doReturn(false).when(snake).eatsSelf();
        Square square = mock(Square.class);
        doReturn(square).when(snake).getHead();

        //when and then
        assertTrue(garden.moveSnake());
        verify(snake).move();
    }

    @Test
    public void createFoodIfNecessary() {

        //given
        Snake snake = mock(Snake.class);
        FoodFactory foodFactory = mock(FoodFactory.class);
        RockListManipulator rockListManipulator = mock(RockListManipulator.class);
        Clip clip = mock(Clip.class);
        Garden garden = new Garden(snake, foodFactory, clip, rockListManipulator);

        when(foodFactory.newInstance()).thenReturn(mock(Food.class));

        //when
        garden.createFoodIfNecessary();

        //then
        verify(foodFactory).newInstance();
        assertNotNull(garden.getFood());
    }

    @Test
    public void createRockIfNecessary() {

        //given
        Snake snake = mock(Snake.class);
        FoodFactory foodFactory = mock(FoodFactory.class);
        RockListManipulator rockListManipulator = mock(RockListManipulator.class);
        Clip clip = mock(Clip.class);
        Garden garden = new Garden(snake, foodFactory, clip, rockListManipulator);

        when(snake.getHead()).thenReturn(mock(Square.class));

        //when
        garden.createRockIfNecessary();

        //then
        verify(rockListManipulator).getRocks();
        assertNotNull(rockListManipulator.getRocks());
    }

    public void playSound() {
        //given
        Snake snake = mock(Snake.class);
        FoodFactory foodFactory = mock(FoodFactory.class);
        Food food = new Food(50, 20);
        when(foodFactory.newInstance()).thenReturn(food);
        Clip clip = mock(Clip.class);
        RockListManipulator rockListManipulator = mock(RockListManipulator.class);
        Garden garden = new Garden(snake, foodFactory, clip, rockListManipulator);
        List<Square> squares = List.of(new Square(50, 20));

        when(snake.inBounds()).thenReturn(true);
        when(snake.eatsSelf()).thenReturn(false);
        when(snake.getHead()).thenReturn(squares.get(0));

        //when
        garden.createFoodIfNecessary();
        garden.moveSnake();

        //then
        verify(clip).start();
    }
}
