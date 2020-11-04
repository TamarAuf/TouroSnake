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

        Direction[] directions = Direction.values();
        Food food = garden.getFood();
        Square head = snake.getHead();

        if (food == null) {
            return;
        }

        List<Node> open = new ArrayList<>();
        List<Node> closed = new ArrayList<>();

        Node targetNode = new Node(food);
        Node headNode = new Node(head);
        open.add(headNode);

        searchSpace.clear();
        path.clear();

        while (!open.isEmpty()) {

            Node currentNode = getLowestNode(open);
            open.remove(currentNode);
            closed.add(currentNode);

            if (currentNode.equals(targetNode)) {
                Node step = getStep(head, currentNode);
                Direction direction = head.directionTo(step);
                snake.turnTo(direction);
                break;
            }

            for (Direction direction : directions) {
                Node neighbor = new Node(currentNode.moveTo(direction), currentNode, targetNode);
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

    public Node getStep(Square head, Node currentNode) {
        Node end = currentNode;
        while (!end.getParent().equals(head)) {
            path.add(end);
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
