package ru.codebattle.client.algorithm;

import ru.codebattle.client.api.BoardPoint;

import java.util.List;
import java.util.Objects;

/**
 * @author Daniil Kudiyarov
 */
public class Node {

    private final BoardPoint boardPoint;
    private int currentCost;
    private int heuristicCost;
    private Node previousNode;
    private List<Node> neighbours;

    public Node(BoardPoint boardPoint, Node previousNode) {
        this.boardPoint = boardPoint;
        this.previousNode = previousNode;
    }

    public void setNeighbours(List<Node> neighbours) {
        this.neighbours = neighbours;
    }

    public List<Node> getNeighbours() {
        return neighbours;
    }

    public int getPriority() {
        return currentCost + heuristicCost;
    }

    public int getCurrentCost() {
        return currentCost;
    }

    public void setCurrentCost(int currentCost) {
        this.currentCost = currentCost;
    }

    public int getHeuristicCost() {
        return heuristicCost;
    }

    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }

    public void setHeuristicCost(int heuristicCost) {
        this.heuristicCost = heuristicCost;
    }

    public BoardPoint getBoardPoint() {
        return boardPoint;
    }

    public Node getPreviousNode() {
        return previousNode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return currentCost == node.currentCost &&
                heuristicCost == node.heuristicCost &&
                Objects.equals(boardPoint, node.boardPoint) &&
                Objects.equals(previousNode, node.previousNode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardPoint, currentCost, heuristicCost, previousNode);
    }
}
