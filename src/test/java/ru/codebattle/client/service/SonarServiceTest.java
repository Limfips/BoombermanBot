package ru.codebattle.client.service;

import lombok.val;
import org.junit.Test;
import ru.codebattle.client.api.BoardElement;
import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.GameBoard;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

import static org.junit.Assert.*;

/**
 * @author Dudka Leonid RPIS-81
 */
public class SonarServiceTest {

    @Test
    public void firstTestScan() {
        GameBoard gameBoard = new GameBoard(MapData.firstBoardString);
        val characterPoint = gameBoard.getBomberman().get(0);
        SonarServiceHelper sonar = new SonarServiceHelper(gameBoard);
        sonar.scan(characterPoint);

        assertEquals(sonar.getScannerMap().size(), 32);
        assertEquals(sonar.getWallsPoints().size(), 11);
        assertEquals(sonar.getDestroyWallsPoints().size(), 7);
        assertEquals(sonar.getNonesPoints().size(), 13);
    }

    @Test
    public void secondTestScan() {
        GameBoard gameBoard = new GameBoard(MapData.secondBoardString);
        val characterPoint = gameBoard.getBomberman().get(0);
        SonarService sonar = new SonarService(gameBoard);
        sonar.scan(characterPoint);
        gameBoard.printBoard();
        assertEquals(sonar.getScannerMap().size(), 32);
        assertEquals(sonar.getWallsPoints().size(), 9);
        assertEquals(sonar.getDestroyWallsPoints().size(), 0);
        assertEquals(sonar.getNonesPoints().size(), 21);
        assertEquals(sonar.getScannerMap().size(), 32);
    }

    @Test
    public void thirdTestAScan() {
        GameBoard gameBoard = new GameBoard(MapData.secondBoardString);
        val characterPoint = gameBoard.getBomberman().get(0);
        SonarServiceHelper helper = new SonarServiceHelper(gameBoard);
        helper.scan(characterPoint);
        gameBoard.printBoard();
        val scanPoint = new BoardPoint(25, 2);
        helper.scan(scanPoint);

        assertFalse(helper.isDestroyWall(2));
        assertTrue(helper.isDestroyWall(3));
        assertEquals(helper.getMeatChopperPoints().size(), 1);
    }

    @Test
    public void getFirstNonePoint() {
        GameBoard gameBoard = new GameBoard(MapData.secondBoardString);
        val characterPoint = gameBoard.getBomberman().get(0);
        SonarServiceHelper helper = new SonarServiceHelper(gameBoard);
        helper.scan(characterPoint);
        gameBoard.printBoard();
        helper.scan(characterPoint);
        assertNotNull(helper.getFirstNonePoint(new BoardPoint(5, 3)));
    }

    @Test
    public void scanDestroyWall() {
        GameBoard gameBoard = new GameBoard(MapData.firstBoardString);
        val characterPoint = gameBoard.getBomberman().get(0);
        gameBoard.printBoard();
        SonarServiceHelper sonar = new SonarServiceHelper(gameBoard);
        sonar.scan(characterPoint);
        var result = sonar.scanDestroyWall(characterPoint, 2);
        result.forEach( it->
                assertEquals(gameBoard.getElementAt(it), BoardElement.DESTROY_WALL)
        );
        assertEquals(result.size(), 2);
        result = sonar.scanDestroyWall(characterPoint, 3);
        result.forEach( it->
                assertEquals(gameBoard.getElementAt(it), BoardElement.DESTROY_WALL)
        );
        assertEquals(result.size(), 3);
    }

    @Test
    public void scanMeatChopper() {
        GameBoard gameBoard = new GameBoard(MapData.secondBoardString);
        val characterPoint = gameBoard.getBomberman().get(0);
        SonarServiceHelper helper = new SonarServiceHelper(gameBoard);
        helper.scan(characterPoint);
        gameBoard.printBoard();
        val scanPoint = new BoardPoint(25, 2);
        val result = helper.scanMeatChopper(scanPoint, helper.getMaxRadius());
        assertEquals(result.size(), 1);
    }

    @Test
    public void clearScan() {
        val countPoints = 41;
        GameBoard gameBoard = new GameBoard(MapData.secondBoardString);
        val scannerPoint = new BoardPoint(15, 15);
        SonarServiceHelper helper = new SonarServiceHelper(gameBoard);
        Throwable thrown = catchThrowable(helper::getScannerMap);
        helper.clearScan();
        assertThat(thrown).isInstanceOf(ScannerMapNullPointerException.class);
        thrown = catchThrowable(helper::getScannerMap);
        assertThat(thrown).isInstanceOf(ScannerMapNullPointerException.class);
        helper.scan(scannerPoint);
        assertEquals(helper.getScannerMap().size(), countPoints);
        helper.clearScan();
        thrown = catchThrowable(helper::getScannerMap);
        assertThat(thrown).isInstanceOf(ScannerMapNullPointerException.class);
    }
}