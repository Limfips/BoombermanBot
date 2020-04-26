package ru.codebattle.client.service;

import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.GameBoard;

/**
 * @author Dudka Leonid RPIS-81
 */
public class SonarServiceHelper extends SonarService {

    public SonarServiceHelper(GameBoard gameBoard) {
        super(gameBoard);
    }

    /**
     * Метод по поиску первой любой свободной точки в радиусе действия
     *
     * @param startScanPoint начало сканирования (центр области)
     * @return первоя свободная точка
     */
    public BoardPoint getFirstNonePoint(BoardPoint startScanPoint) {
        scan(startScanPoint);
        return getNonesPoints().get(0);
    }

    /**
     * Метод проверки опасна ли место под игроком
     * @return boolean
     */
    public boolean isDangerous() {
        return scanDangerous(getCharacterPoint()).size() > 0;
    }
}
