package ru.codebattle.client.algorithm;

import ru.codebattle.client.api.BoardElement;
import ru.codebattle.client.api.BoardPoint;

import java.util.*;

/**
 * @author Daniil Kudiyarov
 */
public class Map {

    private final int size;
    private List<Node> emptyCells;
    private final String boardString;

    public Map(List<BoardPoint> barriers, int size,List<BoardPoint> meatChoppers,BoardPoint botPosition,List<BoardPoint> bombs,String boardString) {
        this.size = size;
        this.emptyCells = new ArrayList<>();
        this.boardString = boardString;
        initEmptyCells(barriers);
        deleteDangerNodes(bombs,meatChoppers);
        addBotPosition(botPosition);
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

    private void deleteDangerNodes(List<BoardPoint> bombs,List<BoardPoint> meatChoppers){
        List<Node> bombsDangerNodes = initBombNeighbours(bombs);
        List<Node> meatChopperDangerNodes = initMeatChopperNeighbours(meatChoppers);
        ArrayList<List<Node>> dangerZones = new ArrayList<>();
        dangerZones.add(bombsDangerNodes);
        dangerZones.add(meatChopperDangerNodes);
        removeCells(dangerZones);

    }

    private List<Node> initBombNeighbours(List<BoardPoint> bombs){
        List<Node> dangerZones = new ArrayList<>();
        for (BoardPoint point:bombs){
            BoardElement element = getElementAt(point);
            if(element == BoardElement.BOMB_TIMER_1 || element == BoardElement.BOMB_TIMER_2){
                dangerZones.addAll(findNeighbours(point,DIRS.BOMB_DIRS));
            }
        }
        return dangerZones;
    }

    private List<Node> findNeighbours(BoardPoint currentPoint,List<BoardPoint> DIRS){
        List<Node> neighbours = new ArrayList<>();
        for (BoardPoint location:DIRS){
            BoardPoint neighborBoardPoint = new BoardPoint(currentPoint.getX() + location.getX(), currentPoint.getY() + location.getY());
            if (!neighborBoardPoint.isOutOfBoard(this.size)) {
                Node newNode = new Node(neighborBoardPoint, null);
                neighbours.add(newNode);
            }
        }
        return neighbours;
    }

    private List<Node> initMeatChopperNeighbours(List<BoardPoint> meatChoppers){
        List<Node> dangerNeatChopperNodes = new ArrayList<>();
        for (BoardPoint point: meatChoppers){
            dangerNeatChopperNodes.addAll(findNeighbours(point, DIRS.STANDARD_DIRS));
        }
        return dangerNeatChopperNodes;
    }

    private void removeCells(ArrayList<List<Node>> danger){
        for (List<Node> list:danger){
            this.emptyCells.removeAll(list);
        }
    }

    private void addBotPosition(BoardPoint botPosition) {
        Node bot = new Node(botPosition,null);
        if (!emptyCells.contains(bot)){
            emptyCells.add(bot);
        }
    }

    private void initNeighbours() {
        for (Node current : this.emptyCells) {
            BoardPoint id = current.getBoardPoint();
            List<Node> neighbours = findNeighbours(id, DIRS.STANDARD_DIRS);
            current.setNeighbours(neighbours);
        }
    }

    private BoardElement getElementAt(BoardPoint point) {
        return BoardElement.valueOf(boardString.charAt(getShiftByPoint(point)));
    }

    private int getShiftByPoint(BoardPoint point) {
        return point.getY() * this.size + point.getX();
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
