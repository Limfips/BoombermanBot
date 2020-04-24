package ru.codebattle.client.algorithm;

import ru.codebattle.client.api.BoardElement;
import ru.codebattle.client.api.BoardPoint;

import java.util.List;
import java.util.Objects;

/**
 * @author Daniil Kudiyarov
 */
public class Node {

    private final BoardPoint location;
    private final BoardElement element;
    private int currentCost;
    private int heuristicCost;
    private Node parent;
    private List<Node> neighbours;

    public Node(BoardPoint location, Node parent,BoardElement element) {
        this.location = location;
        this.parent = parent;
        this.element = element;
    }

    public BoardElement getElement() {
        return element;
    }

    public void setNeighbours(List<Node> neighbours){
        this.neighbours = neighbours;
    }

    public List<Node> getNeighbours() {
        return neighbours;
    }

    public int getPriority(){
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

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void setHeuristicCost(int heuristicCost) {
        this.heuristicCost = heuristicCost;
    }

    public BoardPoint getLocation() {
        return location;
    }

    public Node getParent() {
        return parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return element == node.element &&
                currentCost == node.currentCost &&
                heuristicCost == node.heuristicCost &&
                Objects.equals(location, node.location) &&
                Objects.equals(parent, node.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, parent, element, currentCost, heuristicCost);
    }
}
