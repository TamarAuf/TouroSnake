package touro.snake.strategy.astar;

import touro.snake.*;
import touro.snake.strategy.SnakeStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of SnakeStrategy based on A*.
 */
public class AstarStrategy implements SnakeStrategy {
    @Override
    public void turnSnake(Snake snake, Garden garden) {
        Square food = garden.getFood();
        Square head = snake.getHead();

        Node startNode = new Node(head);
        Node targetNode;
        if (food != null) {
            targetNode = new Node(food);
        } else return;

        ArrayList<Node> open = new ArrayList<>();
        ArrayList<Node> closed = new ArrayList<>();

        open.add(startNode);
        Node currentNode = open.get(0);

        while (!currentNode.equals(targetNode) || !open.isEmpty()) {
            for (Node possibleNode : open) {
                if (possibleNode.getCost() < currentNode.getCost()) {
                    currentNode = possibleNode;
                }
            }
            open.remove(currentNode);
            closed.add(currentNode);

            if (currentNode.equals(targetNode)) return;

            List<Node> neighbors = findNeighbors(snake, currentNode);

            for(Node neighbor : neighbors){
                if(closed.contains(neighbor)) continue;

                if(!open.contains(neighbor)){
                    Node newNode = new Node(neighbor, currentNode, targetNode);
                    open.add(neighbor);
                }
            }
        }
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
}
