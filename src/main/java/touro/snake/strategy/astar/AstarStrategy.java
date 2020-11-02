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

    List<Square> path = new ArrayList<>();
    List<Square> searchSpace = new ArrayList<>();
    @Override
    public void turnSnake(Snake snake, Garden garden) {
        Food food = garden.getFood();
        Square head = snake.getHead();

        if (food == null) {
            return;
        }

        List<Node> open = new ArrayList<>();
        List<Node> closed = new ArrayList<>();

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
                path.add(step);
                break;
            }

            List<Node> neighbors = findNeighbors(snake, currentNode, food);

            for (Node neighbor : neighbors) {
                if (closed.contains(neighbor) || !neighbor.inBounds() || snake.contains(neighbor)) continue;

                if (open.contains(neighbor)) {
                    int index = open.indexOf(neighbor);
                    Node oldNeighbor = open.get(index);
                    if (neighbor.getCost() < oldNeighbor.getCost()) {
                        open.remove(oldNeighbor);
                        open.add(neighbor);
                        searchSpace.add(neighbor);
                    }
                } else {
                    open.add(neighbor);
                    searchSpace.add(neighbor);
                }
            }
        }
    }

    private Node getLowestNode(List<Node> open) {
        Node currentNode = open.get(0);
        for (Node possibleNode : open) {
            if (possibleNode.getCost() < currentNode.getCost()) {
                currentNode = possibleNode;
            }
        }
        return currentNode;
    }

    private List<Node> findNeighbors(Snake snake, Node currentNode, Food food) {
        List<Node> neighbors = new ArrayList<>();
        Direction[] directions = Direction.values();
        for (Direction direction : directions) {
            if (snake.canTurnTo(direction)) {
                Node node = new Node(currentNode.moveTo(direction), currentNode, food);
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

    public List<Square> getPath() {
        return path;
    }

    public List<Square> getSearchSpace() {
        return searchSpace;
    }
}
