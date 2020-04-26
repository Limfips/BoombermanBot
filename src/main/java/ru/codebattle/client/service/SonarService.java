package ru.codebattle.client.service;

import lombok.val;
import lombok.val;
import ru.codebattle.client.api.BoardElement;
import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.GameBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link SonarService} - Сервис позволяющий сканировать облость с заданным радиусом
 * @see RADIUS_SCANNER - определяет радиус сканирования
 * @see CHARACTER_POINT_NULL_MESSAGE - кастомное сообщение возникающее в методе scan()
 * @author Dudka Leonid RPIS-81
 */
public class SonarService {

    private static final int RADIUS_SCANNER = 4;
    private static final String CHARACTER_POINT_NULL_MESSAGE = "scan(): characterPoint is null";

    private GameBoard gameBoard;
    private BoardPoint characterPoint;
    private List<BoardPoint> scannerMap;
    private IPointsListener listener;

    /**
     * @param gameBoard - игровое поле класса {@link GameBoard}
     */
    public SonarService(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
        this.scannerMap = new ArrayList<>();
    }

    /**
     * Метод подключение листенера {@link IPointsListener} к данному классу {@link SonarService}
     * @param listener {@link IPointsListener}
     */
    public void addListenerAlertEnemy(IPointsListener listener) {
        this.listener = listener;
    }

    /**
     * Метод отключение листенера {@link IPointsListener} от данного класса {@link SonarService}
     */
    public void removeListenerAlertEnemy() {
        this.listener = null;
    }

    /**
     * Метод, при котором происходит сканирование облости вокруг {@value characterPoint}
     * и последующая запись в {@value scannerMap}
     * @param characterPoint - перезаписывает {@value characterPoint} и создаёт область {@value scannerMap}
     */
    public void scan(BoardPoint characterPoint) {
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

        processingMap();
    }

    //Сканирование если есть листенер
    private void processingMap() {
        if (listener != null) {
            onScanEnemy();
            onScanBombs();
        }
    }

    //Метод сканирование области на наличие бомб
    private void onScanBombs() {
        if (getCountBombs() > 0) {
            listener.alertBombsNotify(getBombsPoints());
        }
    }

    //Метод сканирование области на наличие врагов
    private void onScanEnemy() {
        if (getCountOtherBomber() > 0) {
            listener.alertEnemyNotify(getOtherBomberPoints());
        }
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

    /**
     * @return ссылка на {@value scannerMap}
     */
    public List<BoardPoint> getScannerMap() {
        return scannerMap;
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

    //Условие подлинности объекта на координате
    private boolean hasElementAt(BoardPoint point, BoardElement element) {
        return getElementAt(point) == element;
    }

    //Получение элемента по координате
    private BoardElement getElementAt(BoardPoint point) {
        return gameBoard.getElementAt(point);
    }

    /**
     * @return количество стен
     */
    public int getCountWalls() {
        return findAllElements(BoardElement.WALL).size();
    }

    /**
     * @return количество разрушаемых стен
     */
    public int getCountDestroyWalls() {
        return findAllElements(BoardElement.DESTROY_WALL).size();
    }

    /**
     * @return количество свободных клеток
     */
    public int getCountNones()   {
        return findAllElements(BoardElement.NONE).size();
    }

    /**
     * @return количество бомб
     */
    public int getCountBombs() {
        return findAllElements(BoardElement.BOMB_TIMER_5,
                BoardElement.BOMB_TIMER_4,
                BoardElement.BOMB_TIMER_3,
                BoardElement.BOMB_TIMER_2,
                BoardElement.BOMB_TIMER_1,
                BoardElement.BOOM).size();
    }

    public int getCountOtherBomber() {
        return findAllElements(BoardElement.OTHER_BOMBERMAN).size();
    }

    private List<BoardPoint> getOtherBomberPoints() {
        return findAllElements(BoardElement.OTHER_BOMBERMAN);
    }

    private List<BoardPoint> getBombsPoints() {
        return findAllElements(BoardElement.BOMB_TIMER_5,
                BoardElement.BOMB_TIMER_4,
                BoardElement.BOMB_TIMER_3,
                BoardElement.BOMB_TIMER_2,
                BoardElement.BOMB_TIMER_1,
                BoardElement.BOOM);
    }

    //1) ToDo создать метод ближайшей свободной точки от координаты поиска
    //2) ToDo создать observer (если обнаружен противник в области сканирования)
    //3) ToDo создать метод по поиску бомб

    //1) ToDo Сканирование области (сохраняет область и анализирует)
    //2) ToDo Листенер на события (если в поле сканирования есть враг или бомба)
    // Данила, если ты дочитал до сюда, то земля тебе пухом и твоему оставшемуся в норме глазу,
    // у меня какая то херь тоже началась с глазом (левым), так что я хз когда завтра появлюсь (точно до обеда)
    // Попробуй по пользоваться данным классом. Если будут ошибки или будет требоваться доработка, пиши, как зайду сохзвонимся и исправим.
    // Комментарии врорде понятные)))))
}
