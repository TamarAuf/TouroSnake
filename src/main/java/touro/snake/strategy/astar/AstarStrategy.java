package touro.snake.strategy.astar;

import touro.snake.*;
import touro.snake.strategy.SnakeStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * An implementation of SnakeStrategy based on A*.
 */
public class AstarStrategy implements SnakeStrategy {
    @Override
    public void turnSnake(Snake snake, Garden garden) {
        Food food = garden.getFood();
        Square head = snake.getHead();

        if (food == null) {
            return;
        }

        ArrayList<Node> open = new ArrayList<>();
        ArrayList<Node> closed = new ArrayList<>();

        Node foodNode = new Node(food);
        Node headNode = new Node(head);
        open.add(headNode);

        while (!open.isEmpty()) {
            Node currentNode = getLowestNode(open);
            open.remove(currentNode);
            closed.add(currentNode);

            if (currentNode.equals(foodNode)) {
                Node step = getStep(head, currentNode);
                Direction direction = head.directionTo(step);
                snake.turnTo(direction);
                break;
            }

            List<Node> neighbors = findNeighbors(snake, currentNode);

            for (Node neighbor : neighbors) {
                if (closed.contains(neighbor) || !neighbor.inBounds() || snake.contains(neighbor)) continue;

                if (open.contains(neighbor)) {
                    int index = open.indexOf(neighbor);
                    Node oldNeighbor = open.get(index);
                    if (neighbor.getCost() < oldNeighbor.getCost()) {
                        open.remove(oldNeighbor);
                        open.add(neighbor);
                    }
                } else open.add(neighbor);
            }
        }
    }

    public Node getLowestNode(List<Node> open) {
        Node currentNode = open.get(0);
        for (Node possibleNode : open) {
            if (possibleNode.getCost() < currentNode.getCost()) {
                currentNode = possibleNode;
            }
        }
        return currentNode;
    }

    public List<Node> findNeighbors(Snake snake, Node currentNode) {
        List<Node> neighbors = new ArrayList<>();
        Direction[] directions = Direction.values();
        for (Direction direction : directions) {
            if (snake.canTurnTo(direction)) {
                Node node = new Node(currentNode.moveTo(direction));
                neighbors.add(node);
            }
        }
        return neighbors;
    }

    public Node getStep(Square head, Node end) {
        while (!end.getParent().equals(head)) {
            end = end.getParent();
        }
        return end;
    }
}
