package ru.codebattle.client.service;

import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.GameBoard;

import java.util.List;

/**
 * Так сказать вспомогательный класс для работы {@link SonarService}
 * @version 1.0
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
     * @throws NullPointerException смотреть {@value SCANNING_CENTER_POINT_NULL_MESSAGE}
     */
    public BoardPoint getFirstNonePoint(BoardPoint startScanPoint) {
        checkScanningCenterPointIsValid(startScanPoint);
        scan(startScanPoint);
        return getNonesPoints().get(0);
    }

    /**
     * Метод проверки опасна ли место в радиусе от игрока
     * @return boolean
     * @throws IllegalArgumentRadiusException смотреть {@value RADIUS_EXCEPTION_MESSAGE}
     */
    public boolean isDangerous(int radius) {
        checkRadiusValid(radius);
        return scanDangerous(getScanningCenterPoint(), radius).size() > 0;
    }

    /**
     * Метод проверки если ли разрушаемые стены в радиусе от игрока
     * @return boolean
     * @throws IllegalArgumentRadiusException смотреть {@value RADIUS_EXCEPTION_MESSAGE}
     */
    public boolean isDestroyWall(int radius) {
        checkRadiusValid(radius);
        return scanDestroyWall(getScanningCenterPoint(), radius).size() > 0;
    }

    /**
     * Метод проверки если ли МитЧеперы в радиусе от игрока
     * @return boolean
     * @throws IllegalArgumentRadiusException смотреть {@value RADIUS_EXCEPTION_MESSAGE}
     */
    public boolean isMeatChopper(int radius) {
        checkRadiusValid(radius);
        return scanMeatChopper(getScanningCenterPoint(), radius).size() > 0;
    }
}
