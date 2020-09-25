package touro.snake;

import org.junit.Test;

import java.util.ArrayList;
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
        RockFactory rockFactory = mock(RockFactory.class);
        Garden garden = new Garden(snake, foodFactory, rockFactory);

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
        RockFactory rockFactory = mock(RockFactory.class);
        Garden garden = new Garden(snake, foodFactory, rockFactory);
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
        RockFactory rockFactory = mock(RockFactory.class);
        Garden garden = new Garden(snake, foodFactory, rockFactory);

        when(snake.getHead()).thenReturn(mock(Square.class));

        //when
        garden.createRockIfNecessary();

        //then
        verify(rockFactory).getRocks();
        assertNotNull(rockFactory.getRocks());
    }
}