package ru.codebattle.client.algorithm;

import ru.codebattle.client.api.BoardPoint;

import java.util.*;

/**
 * @author Daniil Kudiyarov
 */
public class Map {

    private final List<BoardPoint> DIRS = Arrays.asList(
            new BoardPoint(1, 0),
            new BoardPoint(0, -1),
            new BoardPoint(-1, 0),
            new BoardPoint(0, 1));

    private final int size;
    private final List<Node> emptyCells;

    public Map(List<BoardPoint> barriers, int size) {
        this.size = size;
        this.emptyCells = new ArrayList<>();
        initEmptyCells(barriers);
        initNeighbours();
    }

    private void initEmptyCells(List<BoardPoint> barriers) {
        int cellAmount = size * size;
        for (int i = 0; i < cellAmount; i++) {
            BoardPoint pt = getPointByShift(i);
            if (!barriers.contains(pt)) {
                Node node = new Node(pt, null);
                emptyCells.add(node);
            }
        }
    }

    private BoardPoint getPointByShift(int shift) {
        return new BoardPoint(shift % size, shift / size);
    }

    private void initNeighbours() {
        for (Node current : this.emptyCells) {
            BoardPoint id = current.getBoardPoint();
            List<Node> neighbours = new LinkedList<>();
            for (BoardPoint location : DIRS) {
                BoardPoint neighborBoardPoint = new BoardPoint(id.getX() + location.getX(), id.getY() + location.getY());
                if (!isOutOfBoard(neighborBoardPoint)) {
                    Node newNode = new Node(neighborBoardPoint, null);
                    neighbours.add(newNode);
                }
            }
            current.setNeighbours(neighbours);
        }
    }

    private boolean isOutOfBoard(BoardPoint pt) {
        return pt.isOutOfBoard(size);
    }

    public Node getNodeByLocation(BoardPoint boardPoint) {
        for (Node node : emptyCells) {
            if (node.getBoardPoint().equals(boardPoint)) {
                return node;
            }
        }
        return null;
    }

    public List<Node> getParents(Node node) {
        List<Node> parents = node.getNeighbours();
        List<Node> result = new ArrayList<>();
        for (Node node1 : parents) {
            Node newNode = getNodeByLocation(node1.getBoardPoint());
            result.add(newNode);
        }
        return result;
    }
}
