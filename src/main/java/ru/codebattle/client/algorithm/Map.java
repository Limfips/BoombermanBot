package ru.codebattle.client.algorithm;

import ru.codebattle.client.api.BoardElement;
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

    private final List<BoardPoint> bombDIRS = Arrays.asList(
            new BoardPoint(1, 0),
            new BoardPoint(2, 0),
            new BoardPoint(3, 0),
            new BoardPoint(4, 0),
            new BoardPoint(-1, 0),
            new BoardPoint(-2, 0),
            new BoardPoint(-3, 0),
            new BoardPoint(-4, 0),
            new BoardPoint(0, 1),
            new BoardPoint(0, 2),
            new BoardPoint(0, 3),
            new BoardPoint(0,4),
            new BoardPoint(0, -1),
            new BoardPoint(0, -2),
            new BoardPoint(0, -3),
            new BoardPoint(0, -4));

    //todo В ЗАВИСИМОСТИ ОТ РАССТОЯНИЯ ОТ БОМБЫ И ТАЙМЕРА
    //todo убрать таймеры
    //todo если путь не найден, возвращаем рандомное направление

    private final int size;
    private List<Node> emptyCells;
    private List<Node> meatChopperDangerPoints;
    private List<Node> bombDangerPoints;
    private String boardString;

    public Map(List<BoardPoint> barriers, int size,List<BoardPoint> meatChoppers,BoardPoint botPosition,List<BoardPoint> bombs,String boardString) {
        this.size = size;
        this.emptyCells = new ArrayList<>();
        this.meatChopperDangerPoints = new ArrayList<>();
        this.bombDangerPoints = new ArrayList<>();
        this.boardString = boardString;
        initEmptyCells(barriers);
        getBombNeighbours(bombs);
        removeDangerZones(meatChoppers);
        deleteBombs();
        addBotPosition(botPosition);
        initNeighbours();
    }

    private void getBombNeighbours(List<BoardPoint> bombs){
        for (BoardPoint point:bombs){
            BoardElement element = getElementAt(point);
            if(element == BoardElement.BOMB_TIMER_1 || element == BoardElement.BOMB_TIMER_2 || element==BoardElement.BOMB_TIMER_3){
                this.bombDangerPoints.addAll(findNeighboursBomb(point));
            }
        }
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

    private BoardElement getElementAt(BoardPoint point) {
        return BoardElement.valueOf(boardString.charAt(getShiftByPoint(point)));
    }

    private int getShiftByPoint(BoardPoint point) {
        return point.getY() * this.size + point.getX();
    }

    private BoardPoint getPointByShift(int shift) {
        return new BoardPoint(shift % size, shift / size);
    }

    private void removeDangerZones(List<BoardPoint> meatChoppers){
        initMeatChoppersDangerNodes(meatChoppers);
        deleteDangerPoints();
    }

    private void deleteBombs(){
        emptyCells.removeAll(bombDangerPoints);
        this.bombDangerPoints = null;
    }

    private void initMeatChoppersDangerNodes(List<BoardPoint> meatChoppers){
        for (BoardPoint point: meatChoppers){
            meatChopperDangerPoints.addAll(findNeighbours(point));
        }
    }

    private void deleteDangerPoints(){
        emptyCells.removeAll(meatChopperDangerPoints);
        this.meatChopperDangerPoints = null;
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
            List<Node> neighbours = findNeighbours(id);
            current.setNeighbours(neighbours);
        }
    }

    private List<Node> findNeighbours(BoardPoint currentPoint){
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

    private List<Node> findNeighboursBomb(BoardPoint currentPoint){
        List<Node> neighbours = new ArrayList<>();
        for (BoardPoint location:bombDIRS){
            BoardPoint neighborBoardPoint = new BoardPoint(currentPoint.getX() + location.getX(), currentPoint.getY() + location.getY());
            if (!neighborBoardPoint.isOutOfBoard(this.size)) {
                Node newNode = new Node(neighborBoardPoint, null);
                neighbours.add(newNode);
            }
        }
        return neighbours;
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
