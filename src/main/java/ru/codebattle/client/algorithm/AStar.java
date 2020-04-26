package ru.codebattle.client.algorithm;

import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.Direction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * @author Daniil Kudiyarov
 */
public class AStar {

    private final BoardPoint mainGoal;
    private final BoardPoint botPosition;
    private final Map map;

    public AStar(final Map map, final BoardPoint mainGoal, BoardPoint botPosition) {
        this.map = map;
        this.mainGoal = mainGoal;
        this.botPosition = botPosition;
    }

    public Direction getNextStep() {
        if (checkMainGoal()){
            var random = new Random(System.currentTimeMillis());
           return Direction.values()[random.nextInt(Direction.values().length)];
        }
        Node target = getWayToGoal();
        BoardPoint nextStep = buildPath(target);
        return determineDirection(nextStep, botPosition);
    }

    private boolean checkMainGoal() {
        return map.getNodeByLocation(mainGoal) == null || mainGoal.equals(botPosition);
    }

    private Node getWayToGoal() {
        Node startNode = map.getNodeByLocation(botPosition);
        List<Node> openSet = new ArrayList<>();
        HashSet<Node> closedSet = new HashSet<>();
        openSet.add(startNode);

        while (openSet.size() > 0) {
            Node bestNode = getBestNode(openSet);
            openSet.remove(bestNode);
            closedSet.add(bestNode);

            if (bestNode.getBoardPoint().equals(mainGoal)) {
                return bestNode;
            }

            addNeighbourNodes(openSet, closedSet, bestNode);
        }
        return null;
    }

    private Node getBestNode(List<Node> openSet) {
        Node currentNode = openSet.get(0);
        int size = openSet.size();
        for (int i = 1; i < size; i++) {
            Node nextNode = openSet.get(i);
            if (comparePriority(nextNode, currentNode) && compareHeuristicCost(nextNode, currentNode)) {
                currentNode = openSet.get(i);
            }
        }
        return currentNode;
    }

    private void addNeighbourNodes(List<Node> openSet, HashSet<Node> closedSet, Node bestNode) {
        List<Node> neighbours = map.getParents(bestNode);
        for (Node neighbourNode : neighbours) {
            if ((closedSet.contains(neighbourNode)) || (neighbourNode == null)) {
                continue;
            }
            int movementCost = computeMovementCost(bestNode, neighbourNode);
            if (movementCost < neighbourNode.getCurrentCost() || !openSet.contains(neighbourNode)) {
                neighbourNode.setCurrentCost(movementCost);
                neighbourNode.setHeuristicCost(getDistance(neighbourNode.getBoardPoint(), mainGoal));
                neighbourNode.setPreviousNode(bestNode);

                if (!openSet.contains(neighbourNode)) {
                    openSet.add(neighbourNode);
                }
            }
        }
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
        Node currentNode = target;
        Node prevNode = new Node(botPosition,null);
        while (currentNode != null && !currentNode.getBoardPoint().equals(botPosition)) {
            prevNode = currentNode;
            currentNode = currentNode.getPreviousNode();
        }
        return prevNode.getBoardPoint();
    }


    private Direction determineDirection(BoardPoint nextStep, BoardPoint currentBotPosition) {
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
