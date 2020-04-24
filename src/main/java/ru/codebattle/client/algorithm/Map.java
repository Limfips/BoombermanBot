package ru.codebattle.client.algorithm;

import ru.codebattle.client.api.BoardElement;
import ru.codebattle.client.api.BoardPoint;

import java.util.*;

/**
 * @author Daniil Kudiyarov
 */
public class Map {

    private final List<Location> DIRS = Arrays.asList(
            new Location(1, 0),
            new Location(0, -1),
            new Location(-1, 0),
            new Location(0, 1));

    private int size;
    private List<Node> emptyCells;
    private List<Node> map;
    private List<BoardPoint> barriers;
    private List<Node> simpleGraph;
    private String boardString;

    public Map(List<BoardPoint> barriers, int size,String boardString) {
        this.size = size;
        this.barriers = barriers;
        this.boardString = boardString;
        this.simpleGraph = new ArrayList<>();
        this.emptyCells = new ArrayList<>();
        initEmptyBoardPoint();
        initNeighbours();
    }


    private void initEmptyBoardPoint(){
        for(int i =0;i<size*size;i++){
            BoardPoint pt = getPointByShift(i);
            if (!barriers.contains(pt)){
                Node node = new Node(pt,null,getElementAt(pt));
                emptyCells.add(node);
            }
        }
    }

    public List<Node> getEmptyCells(){
        return getEmptyCells();
    }

    public int getSize() {
        return size;
    }

    public List<Node> getMap() {
        return map;
    }

    public List<BoardPoint> getBarriers() {
        return barriers;
    }

    public List<Node> getSimpleGraph() {
        return simpleGraph;
    }

    public String getBoardString() {
        return boardString;
    }

    public Node getPointByLocation(BoardPoint boardPoint){
        for (Node node:emptyCells){
            if (node.getLocation().equals(boardPoint)){
                return node;
            }
        }
        return null;
    }

    private void initNeighbours(){
        List<Node> newGraph = new ArrayList<>();
        for (Node current : this.emptyCells) {
            BoardPoint id = current.getLocation();
            List<Node> neighbours = new LinkedList<>();
            for (Location location : DIRS) {
                BoardPoint neighborLocation = new BoardPoint(id.getX() + location.getX(), id.getY() + location.getY());
                if (!isOutOfBoard(neighborLocation)) {
                    Node newNode = new Node(neighborLocation, null, getElementAt(neighborLocation));
                    neighbours.add(newNode);
                }
            }
            current.setNeighbours(neighbours);
            newGraph.add(current);
        }
        this.simpleGraph = newGraph;
    }

    public boolean isOutOfBoard(BoardPoint pt) {
        return pt.isOutOfBoard(size);
    }

    public List<Node> getParents(Node node){
        List<Node> parents = node.getNeighbours();
        List<Node> result = new ArrayList<>();
        for (Node node1:parents){
            Node newNode = getPointByLocation(node1.getLocation());
            result.add(newNode);
        }
        return result;
    }

  //  private void initMap(){
  //      for(int i =0;i<size*size;i++){
   //         BoardPoint pt = getPointByShift(i);
   //         if (getElementAt(pt).equals()){
   //             Node node = new Node(pt,null,getElementAt(pt));
   //             emptyCells.add(node);
   //         }
   //     }
   // }


    private BoardPoint getPointByShift(int shift) {
        return new BoardPoint(shift % size, shift / size);
    }

    public BoardElement getElementAt(BoardPoint point) {
        return BoardElement.valueOf(boardString.charAt(getShiftByPoint(point)));
    }

    private int getShiftByPoint(BoardPoint point) {
        return point.getY() * size + point.getX();
    }
}
