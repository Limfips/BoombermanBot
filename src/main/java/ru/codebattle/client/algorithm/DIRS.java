package ru.codebattle.client.algorithm;

import ru.codebattle.client.api.BoardPoint;

import java.util.Arrays;
import java.util.List;

/**
 * @author Daniil Kudiyarov
 */
public class DIRS {

    public static final List<BoardPoint> STANDARD_DIRS = Arrays.asList(
            new BoardPoint(1, 0),
            new BoardPoint(0, -1),
            new BoardPoint(-1, 0),
            new BoardPoint(0, 1));

    public static final List<BoardPoint> BOMB_DIRS = Arrays.asList(
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
}
