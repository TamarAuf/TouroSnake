package touro.snake.strategy.astar;

import touro.snake.*;
import touro.snake.strategy.SnakeStrategy;

import java.util.ArrayList;

/**
 * An implementation of SnakeStrategy based on A*.
 */
public class AstarStrategy implements SnakeStrategy {
    @Override
    public void turnSnake(Snake snake, Garden garden) {
        Direction[] directions = Direction.values();
        Square food = garden.getFood();
        Square head = snake.getHead();

        Node currentNode = new Node(head);
        Node goalNode = new Node(food);

        ArrayList<Node> open = new ArrayList<>();
        ArrayList<Node> closed = new ArrayList<>();
        ArrayList<Node> successorNodes = new ArrayList<>();

        Node node = new Node(head, currentNode, food);
        Node parent = node.getParent();
        open.add(parent);

        Node lowestNode = open.get(0);

        while (!currentNode.equals(goalNode) || !open.isEmpty()) {
            for (int i = 0; i < directions.length; i++) {
                if (snake.canTurnTo(directions[i])) {
                    successorNodes.add((Node) head.moveTo(directions[i]));
                }
            }
            open.addAll(successorNodes);
            
            for (Node possibleNode : open) {
                if (possibleNode.getCost() < lowestNode.getCost()) {
                    lowestNode = possibleNode;
                }
            }

            closed.add(lowestNode);
            open.remove(lowestNode);


        }
    }
}
