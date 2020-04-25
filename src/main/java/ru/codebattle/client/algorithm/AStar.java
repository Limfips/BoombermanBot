package ru.codebattle.client.algorithm;

import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * @author Daniil Kudiyarov
 */
public class AStar {

    private final BoardPoint mainGoal;
    private final BoardPoint currentBotPosition;
    private final Map map;

    public AStar(final Map map, final BoardPoint mainGoal, BoardPoint currentBotPosition) {
        this.map = map;
        this.mainGoal = mainGoal;
        this.currentBotPosition = currentBotPosition;
    }

    public Direction getNextStep() {
        if (map.getNodeByLocation(mainGoal) == null || mainGoal.equals(currentBotPosition)){
            return Direction.STOP;
        }
        Node target = getWayToGoal(mainGoal);
        BoardPoint nextStep = buildPath(target);
        return determineDirection(nextStep,currentBotPosition);
    }

    private Node getWayToGoal(BoardPoint goal) {
        Node startNode = map.getNodeByLocation(currentBotPosition);
        BoardPoint goalLocation = map.getNodeByLocation(goal).getBoardPoint();
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

            if (currentNode.getBoardPoint().equals(goalLocation)) {
                return currentNode;
            }

            List<Node> neighbours = map.getParents(currentNode);
            for (Node neighbourNode : neighbours) {
                if ((closedSet.contains(neighbourNode)) || (neighbourNode == null)) {
                    continue;
                }
                int movementCost = computeMovementCost(currentNode, neighbourNode);
                if (movementCost < neighbourNode.getCurrentCost() || !openSet.contains(neighbourNode)) {
                    neighbourNode.setCurrentCost(movementCost);
                    neighbourNode.setHeuristicCost(getDistance(neighbourNode.getBoardPoint(), goalLocation));
                    neighbourNode.setPreviousNode(currentNode);

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
        return a1.getCurrentCost() + getDistance(a1.getBoardPoint(), a2.getBoardPoint());
    }

    private int getDistance(BoardPoint a, BoardPoint b) {
        int dstX = Math.abs(a.getX() - b.getX());
        int dstY = Math.abs(a.getY() - b.getY());
        if (dstX > dstY) {
            return 2 * dstY + (dstX - dstY);
        }
        return 2 * dstX + (dstY - dstX);
    }

    private BoardPoint buildPath(final Node target) {
        List<Node> path = new ArrayList<>();
        Node currentNode = target;
        if (currentNode == null){
            return currentBotPosition;
        }
        while (!currentNode.getBoardPoint().equals(currentBotPosition)) {
            path.add(currentNode);
            currentNode = currentNode.getPreviousNode();
            if (currentNode == null){
                return currentBotPosition;
            }
        }
        Collections.reverse(path);
        return path.get(0).getBoardPoint();
    }


    private Direction determineDirection(BoardPoint nextStep, BoardPoint currentBotPosition) {
        if (nextStep.getX() == currentBotPosition.getX() && nextStep.getY() == currentBotPosition.getY()){
            return Direction.STOP;
        }
        if (nextStep.getX() != currentBotPosition.getX()) {
            if (compareCoordinates(nextStep.getX(), currentBotPosition.getX())) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } else if (compareCoordinates(nextStep.getY(), currentBotPosition.getY())) {
            return Direction.DOWN;
        } else {
            return Direction.UP;
        }
    }

    private boolean compareCoordinates(int a1, int a2) {
        return a1 > a2;
    }
}
