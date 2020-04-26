package ru.codebattle.client.service;

import ru.codebattle.client.api.BoardElement;
import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.GameBoard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@link SonarService} - Сервис позволяющий сканировать облость с заданным радиусом
 * @see RADIUS_SCANNER - определяет радиус сканирования
 * @see CHARACTER_POINT_NULL_MESSAGE - кастомное сообщение возникающее в методе scan()
 * @author Dudka Leonid RPIS-81
 */
class SonarService {

    private static final int RADIUS_SCANNER = 4;
    private static final String CHARACTER_POINT_NULL_MESSAGE = "scan(): characterPoint is null";

    private GameBoard gameBoard;
    private BoardPoint characterPoint;
    private List<BoardPoint> scannerMap;

    /**
     * @param gameBoard - игровое поле класса {@link GameBoard}
     */
    SonarService(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.scannerMap = new ArrayList<>();
    }

    /**
     * Метод, при котором происходит сканирование облости вокруг {@value characterPoint}
     * и последующая запись в {@value scannerMap}
     *
     * @param characterPoint - перезаписывает {@value characterPoint} и создаёт область {@value scannerMap}
     */
    void scan(BoardPoint characterPoint) {
        if (characterPoint == null) throw new NullPointerException(CHARACTER_POINT_NULL_MESSAGE);
        this.characterPoint = characterPoint;
        this.scannerMap.clear();
        scannerMap.add(characterPoint);
        for (int i = 0; i < RADIUS_SCANNER + 1; i++) {
            if (i == 0) {
                scannerLineX(RADIUS_SCANNER - i, i);
            } else {
                scannerLineX(RADIUS_SCANNER - i, -1 * i);
                scannerLineX(RADIUS_SCANNER - i, i);
            }
        }
    }

    /**
     * @return ссылка на {@value scannerMap}
     */
    List<BoardPoint> getScannerMap() {
        return scannerMap;
    }

    /**
     * @return точки стен
     */
    List<BoardPoint> getWallsPoints() {
        return findAllElements(BoardElement.WALL);
    }

    /**
     * @return точки разрушаемых стен
     */
    List<BoardPoint> getDestroyWallsPoints() {
        return findAllElements(BoardElement.DESTROY_WALL);
    }

    /**
     * @return точки свободных клеток
     */
    List<BoardPoint> getNonesPoints() {
        return findAllElements(BoardElement.NONE);
    }

    /**
     * @return точки бомб
     */
    List<BoardPoint> getBombsPoints() {
        return findAllElements(
                BoardElement.BOMB_TIMER_5,
                BoardElement.BOMB_TIMER_4,
                BoardElement.BOMB_TIMER_3,
                BoardElement.BOMB_TIMER_2,
                BoardElement.BOMB_TIMER_1,
                BoardElement.BOOM);
    }

    /**
     * @return точки врагов-бомберов
     */
    List<BoardPoint> getOtherBomberPoints() {
        return findAllElements(BoardElement.OTHER_BOMBERMAN);
    }

    /**
     * @return точки врагов-митчеперов
     */
    List<BoardPoint> getMeatChopperPoints() {
        return findAllElements(BoardElement.MEAT_CHOPPER);
    }

    //Поиск элементов в зоне сканирования
    private List<BoardPoint> findAllElements(BoardElement... elementType) {
        List<BoardPoint> result = new ArrayList<>();
        for (BoardPoint pt : scannerMap) {
            for (BoardElement elemType : elementType) {
                if (hasElementAt(pt, elemType)) {
                    result.add(pt);
                }
            }
        }
        return result;
    }

    BoardPoint getCharacterPoint() {
        return characterPoint;
    }

    List<BoardPoint> scanDangerous(BoardPoint scannerPoint) {
        scan(scannerPoint);
        List<BoardPoint> scanningPoints = new ArrayList<>();
        for (int i = 0; i < RADIUS_SCANNER; i++) {
            for (BoardPoint point : scannerMap) {
                if (isValidDangerousPoint(point, i, scannerPoint) && isBomb(point)) {
                    scanningPoints.add(point);
                }
            }
        }
        return scanningPoints;
    }

    private boolean isBomb(BoardPoint boardPoint) {
        List<BoardElement> elementType = Arrays.asList(BoardElement.BOMB_TIMER_5,
                BoardElement.BOMB_TIMER_4,
                BoardElement.BOMB_TIMER_3,
                BoardElement.BOMB_TIMER_2,
                BoardElement.BOMB_TIMER_1,
                BoardElement.BOOM);
        for (BoardElement elemType : elementType) {
            if (hasElementAt(boardPoint, elemType)) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidDangerousPoint(BoardPoint point, int i, BoardPoint scannerPoint) {
        return (point.getX() + i == scannerPoint.getX() || point.getX() - i == scannerPoint.getX()) ||
                (point.getY() + i == scannerPoint.getY() || point.getY() - i == scannerPoint.getY());
    }

    //Условие подлинности объекта на координате
    private boolean hasElementAt(BoardPoint point, BoardElement element) {
        return getElementAt(point) == element;
    }

    //Получение элемента по координате
    private BoardElement getElementAt(BoardPoint point) {
        return gameBoard.getElementAt(point);
    }

    //Метод создания точек по оси Х
    private void scannerLineX(int width, int height) {
        if (height != 0) {
            addPoint(new BoardPoint(characterPoint.getX(), characterPoint.getY() + height));
        }
        for (int i = 1; i < width + 1; i++) {
            addPoint(new BoardPoint(characterPoint.getX() + -1 * i, characterPoint.getY() + height));
            addPoint(new BoardPoint(characterPoint.getX() + i, characterPoint.getY() + height));
        }
    }

    //Добавление точки в зону сканирования при наличии удовлетворяющих условие
    private void addPoint(BoardPoint point) {
        if (!isOutOfBoard(point)) {
            scannerMap.add(point);
        }
    }

    //Условие (точка не выходит за границы карты)
    private boolean isOutOfBoard(BoardPoint pt) {
        return pt.isOutOfBoard(gameBoard.size());
    }
}
