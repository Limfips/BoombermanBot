package ru.codebattle.client.service;

import ru.codebattle.client.api.BoardElement;
import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.GameBoard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@link SonarService} - Сервис позволяющий сканировать облость с заданным радиусом
 * @see RADIUS_SCANNER - определяет радиус сканирования (максимальный - дефолтный)
 * @see SCANNING_CENTER_POINT_NULL_MESSAGE - сообщение ошибки при передаче нулевого значения в scanningCenterPoint
 * @see SCANNER_MAP_NULL_MESSAGE - сообщение ошибки при получении значения из пустой карты
 * @see ELEMENT_TYPE_NULL_MESSAGE - сообщение ошибки при передаче нулевого значения в elementType
 * @see RADIUS_EXCEPTION_MESSAGE - сообщение ошибки при не валидном значении {@link IllegalArgumentRadiusException}
 * @version 1.0
 * @author Dudka Leonid RPIS-81
 */
class SonarService {

    static final int RADIUS_SCANNER = 4;
    static final String SCANNING_CENTER_POINT_NULL_MESSAGE = "scanningCenterPoint is null";
    static final String SCANNER_MAP_NULL_MESSAGE = "scannerMap is null";
    static final String ELEMENT_TYPE_NULL_MESSAGE = "element type is null";
    static final String RADIUS_EXCEPTION_MESSAGE = "радиус меньше 1 или больше радиуса, " +
                                "указанного в статической переменной класса SonarService}";

    private GameBoard gameBoard;
    private BoardPoint scanningCenterPoint;
    private List<BoardPoint> scannerMap;
    private int maxRadius;

    /**
     * @param gameBoard - игровое поле класса {@link GameBoard}
     */
    SonarService(GameBoard gameBoard) {
        this(gameBoard, RADIUS_SCANNER);
    }

    /**
     * @param gameBoard - игровое поле класса {@link GameBoard}
     * @param maxRadius - максимальный радиус
     */
    public SonarService(GameBoard gameBoard, int maxRadius) {
        this.gameBoard = gameBoard;
        this.maxRadius = maxRadius;
        this.scannerMap = new ArrayList<>();
    }

    /**
     * Метод, при котором происходит сканирование облости вокруг {@value characterPoint}
     * и последующая запись в {@value scannerMap}
     *
     * @param scanningCenterPoint - перезаписывает {@value characterPoint} и создаёт область {@value scannerMap}
     * @throws NullPointerException смотреть {@value SCANNING_CENTER_POINT_NULL_MESSAGE}
     */
    public void scan(BoardPoint scanningCenterPoint) {
        checkScanningCenterPointIsValid(scanningCenterPoint);
        this.scanningCenterPoint = scanningCenterPoint;
        clearScan();
        scannerMap.add(scanningCenterPoint);
        for (int i = 0; i < this.maxRadius + 1; i++) {
            if (i == 0) {
                scannerLineX(this.maxRadius - i, i);
            } else {
                scannerLineX(this.maxRadius - i, -1 * i);
                scannerLineX(this.maxRadius - i, i);
            }
        }
    }

    /**
     * Очищает карту сканирования
     */
    public void clearScan() {
        this.scannerMap.clear();
    }

    /**
     * Метод вызывается после метода {@value scan}
     * @return ссылка на {@value scannerMap}
     */
    public List<BoardPoint> getScannerMap(){
        checkMap();
        return scannerMap;
    }

    /**
     * Метод вызывается после метода {@value scan}
     * @return точки стен
     */
    public List<BoardPoint> getWallsPoints() {
        checkMap();
        return findAllElements(BoardElement.WALL);
    }

    /**
     * Метод вызывается после метода {@value scan}
     * @return точки разрушаемых стен
     */
    public List<BoardPoint> getDestroyWallsPoints() {
        checkMap();
        return findAllElements(BoardElement.DESTROY_WALL);
    }

    /**
     * Метод вызывается после метода {@value scan}
     * @return точки свободных клеток
     */
    public List<BoardPoint> getNonesPoints() {
        checkMap();
        return findAllElements(BoardElement.NONE);
    }

    /**
     * Метод вызывается после метода {@value scan}
     * @return точки бомб
     */
    public List<BoardPoint> getBombsPoints() {
        checkMap();
        return findAllElements(
                BoardElement.BOMB_TIMER_5,
                BoardElement.BOMB_TIMER_4,
                BoardElement.BOMB_TIMER_3,
                BoardElement.BOMB_TIMER_2,
                BoardElement.BOMB_TIMER_1,
                BoardElement.BOOM);
    }

    /**
     * Метод вызывается после метода {@value scan}
     * @return точки врагов-бомберов
     */
    public List<BoardPoint> getOtherBomberPoints() {
        checkMap();
        return findAllElements(BoardElement.OTHER_BOMBERMAN);
    }

    /**
     * Метод вызывается после метода {@value scan}
     * @return точки врагов-митчеперов
     */
    public List<BoardPoint> getMeatChopperPoints() {
        checkMap();
        return findAllElements(BoardElement.MEAT_CHOPPER);
    }

    //Поиск элементов в зоне сканирования
    private List<BoardPoint> findAllElements(BoardElement... elementType) {
        if (elementType == null) throw new NullPointerException(ELEMENT_TYPE_NULL_MESSAGE);
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

    /**
     * @return текущюю точку сканара (последнее изменение)
     */
    public BoardPoint getScanningCenterPoint() {
        return scanningCenterPoint;
    }

    /**
     * @return текущий максимальный радиус
     */
    public int getMaxRadius() {
        return maxRadius;
    }

    /**
     * Метод ищет все точки разрушаемых стен класса {@link BoardElement}
     * @param scanningCenterPoint - начальная точка сканирования
     * @param radius - радиус сканирования
     * @return список точек
     * @throws NullPointerException смотреть {@value SCANNING_CENTER_POINT_NULL_MESSAGE}
     * @throws IllegalArgumentRadiusException смотреть {@value RADIUS_EXCEPTION_MESSAGE}
     */
    List<BoardPoint> scanDestroyWall(BoardPoint scanningCenterPoint, int radius) {
        checkScanningCenterPointIsValid(scanningCenterPoint);
        checkRadiusValid(radius);
        scan(scanningCenterPoint);
        List<BoardPoint> scanningPoints = new ArrayList<>();
        for (int i = 1; i < radius + 1; i++) {
            for (BoardPoint point : scannerMap) {
                if (isValidDangerousPoint(point, i, scanningCenterPoint) && isDestroyWall(point)) {
                    scanningPoints.add(point);
                }
            }
        }
        return scanningPoints;
    }

    /**
     * Метод ищет все точки бомб класса {@link BoardElement}
     * @param scanningCenterPoint - начальная точка сканирования
     * @param radius - радиус сканирования
     * @return список точек
     * @throws NullPointerException смотреть {@value SCANNING_CENTER_POINT_NULL_MESSAGE}
     * @throws IllegalArgumentRadiusException смотреть {@value RADIUS_EXCEPTION_MESSAGE}
     */
    List<BoardPoint> scanDangerous(BoardPoint scanningCenterPoint, int radius) {
        checkScanningCenterPointIsValid(scanningCenterPoint);
        checkRadiusValid(radius);
        scan(scanningCenterPoint);
        List<BoardPoint> scanningPoints = new ArrayList<>();
        for (int i = 1; i < radius + 1; i++) {
            for (BoardPoint point : scannerMap) {
                if (isValidDangerousPoint(point, i, scanningCenterPoint) && isBomb(point)) {
                    scanningPoints.add(point);
                }
            }
        }
        return scanningPoints;
    }

    /**
     * Метод ищет все точки МитЧеперов класса {@link BoardElement}
     * @param scanningCenterPoint - начальная точка сканирования
     * @param radius - радиус сканирования
     * @return список точек
     * @throws NullPointerException смотреть {@value SCANNING_CENTER_POINT_NULL_MESSAGE}
     * @throws IllegalArgumentRadiusException смотреть {@value RADIUS_EXCEPTION_MESSAGE}
     */
    List<BoardPoint> scanMeatChopper(BoardPoint scanningCenterPoint, int radius) {
        checkScanningCenterPointIsValid(scanningCenterPoint);
        checkRadiusValid(radius);
        scan(scanningCenterPoint);
        List<BoardPoint> scanningPoints = new ArrayList<>();
        for (int i = 1; i < radius + 1; i++) {
            for (BoardPoint point : scannerMap) {
                if (isValidDangerousPoint(point, i, scanningCenterPoint) && isMeatChopper(point)) {
                    scanningPoints.add(point);
                }
            }
        }
        return scanningPoints;
    }

    void checkRadiusValid(int radius) {
        if (radius < 1 || radius > this.maxRadius) throw new IllegalArgumentRadiusException(RADIUS_EXCEPTION_MESSAGE);
    }

    void checkScanningCenterPointIsValid(BoardPoint scanningCenterPoint) {
        if (scanningCenterPoint == null) throw new NullPointerException(SCANNING_CENTER_POINT_NULL_MESSAGE);
    }

    private boolean isMeatChopper(BoardPoint boardPoint) {
        return hasElementAt(boardPoint, BoardElement.MEAT_CHOPPER);
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

    private boolean isDestroyWall(BoardPoint boardPoint) {
        return hasElementAt(boardPoint, BoardElement.DESTROY_WALL);
    }

    private boolean isValidDangerousPoint(BoardPoint point, int i, BoardPoint scannerPoint) {
        return ((point.getX() + i == scannerPoint.getX() || point.getX() - i == scannerPoint.getX())
                && point.getY() == scannerPoint.getY()) ||
                ((point.getY() + i == scannerPoint.getY() || point.getY() - i == scannerPoint.getY())
                && point.getX() == scannerPoint.getX());
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
            addPoint(new BoardPoint(scanningCenterPoint.getX(), scanningCenterPoint.getY() + height));
        }
        for (int i = 1; i < width + 1; i++) {
            addPoint(new BoardPoint(scanningCenterPoint.getX() + -1 * i, scanningCenterPoint.getY() + height));
            addPoint(new BoardPoint(scanningCenterPoint.getX() + i, scanningCenterPoint.getY() + height));
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

    /**
     * ПРоверяет не пустая ли карта
     * @throws ScannerMapNullPointerException если карта пустая
     */
    private void checkMap() {
        if (scannerMap.size() < 1) throw new ScannerMapNullPointerException(SCANNER_MAP_NULL_MESSAGE);
    }
}
