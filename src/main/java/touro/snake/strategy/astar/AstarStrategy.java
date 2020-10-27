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

        Node node = new Node(head, currentNode, food);
        Node parent = node.getParent();
        open.add(parent);

        Node lowestNode = open.get(0);

        while (!currentNode.equals(goalNode) || !open.isEmpty()) {
            for (Node possibleNode : open) {
                if (possibleNode.getCost() < lowestNode.getCost()) {
                    lowestNode = possibleNode;
                }
            }
            closed.add(lowestNode);

            for (int i = 0; i < directions.length; i++) {
                Direction direction = directions[i];
                Node successorNode = (Node) head.moveTo(directions[i]);
                if (snake.canTurnTo(direction) && !closed.contains(successorNode)) {
                    if(!open.contains(successorNode)){
                        open.add(successorNode);
                        currentNode = parent;
                        if (successorNode.getCost() < lowestNode.getCost()) {
                            lowestNode = successorNode;
                        }
                    }
                    else {
                        if (successorNode.getCost() < lowestNode.getCost()) {
                            lowestNode = successorNode;
                        }
                        Node newNode = new Node(lowestNode, currentNode, food);
                    }
                }
            }
        }
    }
}
