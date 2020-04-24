package ru.codebattle.client.algorithm;

import ru.codebattle.client.api.BoardPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * @author Daniil Kudiyarov
 */
public class AStar {

    private BoardPoint mainGoal;
    private Map map;
    private BoardPoint bot;

    public AStar(final Map map, final BoardPoint mainGoal, BoardPoint bot) {
        this.map = map;
        this.mainGoal = mainGoal;
        this.bot = bot;
    }

    public List<Node> getPath() {
        Node target = getWayToGoal(mainGoal);
        return buildPath(target);
    }

    private Node getWayToGoal(BoardPoint goal) {
        Node startNode = map.getPointByLocation(bot);
        BoardPoint goalLocation = map.getPointByLocation(goal).getLocation();
        List<Node> openSet = new ArrayList<>();
        HashSet<Node> closedSet = new HashSet<>();
        openSet.add(startNode);

        while (openSet.size() > 0) {
            Node currentNode = openSet.get(0);
            int size = openSet.size();
            for (int i = 1; i < size; i++) {
                Node nextNode = openSet.get(i);
                if (comparePriority(nextNode, currentNode) && compareHeuristicCost(nextNode, currentNode)) {
                    currentNode = openSet.get(i);
                }
            }
            openSet.remove(currentNode);
            closedSet.add(currentNode);

            if (currentNode.getLocation().equals(goalLocation)) {
                return currentNode;
            }
            List<Node> neighbours = map.getParents(currentNode);
            for (Node neighbourNode : neighbours) {
                if (closedSet.contains(neighbourNode)) {
                    continue;
                }
                if (neighbourNode == null){
                    continue;
                }
                int movementCost = computeMovementCost(currentNode, neighbourNode);
                if (movementCost < neighbourNode.getCurrentCost() || !openSet.contains(neighbourNode)) {
                    neighbourNode.setCurrentCost(movementCost);
                    neighbourNode.setHeuristicCost(getDistance(neighbourNode.getLocation(), goalLocation));
                    neighbourNode.setParent(currentNode);

                    if (!openSet.contains(neighbourNode)) {
                        openSet.add(neighbourNode);
                    }
                }
            }
        }
        return null;
    }

    private boolean comparePriority(Node a1, Node a2) {
        return a1.getPriority() < a2.getPriority() || a1.getPriority() == a2.getPriority();
    }

    private boolean compareHeuristicCost(Node a1, Node a2) {
        return a1.getHeuristicCost() < a2.getHeuristicCost();
    }

    private int computeMovementCost(Node a1, Node a2) {
        return a1.getCurrentCost() + getDistance(a1.getLocation(), a2.getLocation());
    }

    private int getDistance(BoardPoint a, BoardPoint b) {
        int dstX = Math.abs(a.getX() - b.getX());
        int dstY = Math.abs(a.getY() - b.getY());
        if (dstX > dstY) {
            return 2 * dstY + (dstX - dstY);
        }
        return 2 * dstX + (dstY - dstX);
    }

    private List<Node> buildPath(Node target) {
        List<Node> path = new ArrayList<>();
        Node currentNode = target;
        BoardPoint botLocation = bot;
        while (!currentNode.getLocation().equals(botLocation)) {
            path.add(currentNode);
            currentNode = currentNode.getParent();
        }
        Collections.reverse(path);
        return path;
    }
}
