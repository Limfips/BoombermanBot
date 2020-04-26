package ru.codebattle.client.service;

import lombok.val;
import org.junit.Test;
import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.GameBoard;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Dudka Leonid RPIS-81
 */
public class SonarServiceTest {

    private static final String firstBoardString = "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼                  #  # #     ☼☼ ☼   ☼   ☼       ☼   ☼## ☼&  ☼☼                         #   ☼☼   ☼   ☼   ☼   ☼   ☼   ☼   ☼ ☼☼   ♥       ♥          ♥ #    ☼☼ ☼1  ☼   ☼       ☼   ☼   ☼   ☼☼              #            ##☼☼   ☼ # ☼   ☼   ☼   ☼ ##☼   ☼ ☼☼                ♥ ##  #      ☼☼ ☼   ☼   ☼ ♥     ☼   ☼## ☼ ☼ ☼☼                   #         ☼☼   ☼   ☼#  ☼   ☼   ☼   ☼   ☼ ☼☼                ♥ #          ☼☼ ☼   ☼ # ☼ # ☼ ##☼   ☼   ☼   ☼☼                             ☼☼  &☼   ☼  &☼   ☼#  ☼  #☼   ☼ ☼☼                       &  & #☼☼ ☼   ☼   ☼       ☼  #☼ # ☼   ☼☼                             ☼☼   ☼   ☼   ☼   ☼   ☼ # ☼   ☼#☼☼       &  #         #  ##    ☼☼ ☼   ☼   ☼       ☼#  ☼   ☼#  ☼☼    &                        ☼☼   ☼   ☼   ☼   ☼   ☼## ☼   ☼ ☼☼                           # ☼☼ ☼   ☼   ☼     & ☼  #☼   ☼#  ☼☼                   &  ♥##  ##☼☼ # ☼#  ☼#  ☼   ☼   ☼   ☼ # ☼ ☼☼      #  #       &     # ☺  #☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼";
    private static final String secondBoardString = "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼   #    ☺    #                 # ☼☼                   # #           ☼☼    ☼☼☼ ♥    ☼☼   ☼☼    & ☼☼☼    ☼☼             ☼    #☼             ☼☼  ☼     ☼         &     ☼     ☼  ☼☼♥ ☼ #☼  ☼♥      ☼ &     ☼# ☼  ☼  ☼☼♥ ☼     ☼#       #      ☼     ☼  ☼☼##   #       ☼     ☼ #           ☼☼    ☼☼☼      ☼☼   ☼☼ ## # ☼☼☼    ☼☼#                                ☼☼         &   #&#            &    ☼☼     ♥                      &♥   ☼☼                           ♥     ☼☼  ☼☼   ☼☼      ☼☼☼    & ☼☼   ☼☼  ☼☼  ☼ ♥   ☼  #   ##&      ☼     ☼  ☼☼         & ♥ ☼     ☼♥   #        ☼☼     ☼# &    ☼ ♥☼# ☼       ☼     ☼☼             ☼     ☼             ☼☼# ☼#  # ☼      #        ☼&#   ☼  ☼☼  ☼☼   ☼☼      ☼☼☼      ☼☼   ☼☼  ☼☼ #                               ☼☼             &           ♥       ☼☼ &             ♥   &             ☼☼         #   ♥   ♥               ☼☼  ♥ ☼☼☼ #    ☼☼   ☼☼      ☼☼☼    ☼☼             ☼     ☼             ☼☼ &☼     ☼               ☼ ♥  ♥☼  ☼☼  ☼  ☼  ☼ #   # ☼       ☼# ☼# ☼  ☼☼  ☼     ☼               ☼♥  # ☼  ☼☼###    &     ☼     ☼#            ☼☼    ☼☼☼ #    ☼☼  &☼☼      ☼☼☼   &☼☼        &                        ☼☼                     #           ☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼";

    @Test
    public void firstTestScan() {
        GameBoard gameBoard = new GameBoard(firstBoardString);
        val characterPoint = gameBoard.getBomberman().get(0);
        SonarService sonar = new SonarService(gameBoard);
        sonar.scan(characterPoint);

        assertEquals(sonar.getCountWalls(), 11);
        assertEquals(sonar.getCountDestroyWalls(), 7);
        assertEquals(sonar.getCountNones(), 13);
        assertEquals(sonar.getScannerMap().size(), 32);
    }

    @Test
    public void secondTestScan() {
        GameBoard gameBoard = new GameBoard(secondBoardString);
        val characterPoint = gameBoard.getBomberman().get(0);
        SonarService sonar = new SonarService(gameBoard);
        sonar.scan(characterPoint);
        gameBoard.printBoard();

        assertEquals(sonar.getCountWalls(), 9);
        assertEquals(sonar.getCountDestroyWalls(), 0);
        assertEquals(sonar.getCountNones(), 21);
        assertEquals(sonar.getScannerMap().size(), 32);
    }

    @Test
    public void XXX() {
        GameBoard gameBoard = new GameBoard(firstBoardString);
        val characterPoint = gameBoard.getBomberman().get(0);
        SonarService sonar = new SonarService(gameBoard);
        sonar.scan(characterPoint);



        assertEquals(sonar.getCountWalls(), 11);
        assertEquals(sonar.getCountDestroyWalls(), 7);
        assertEquals(sonar.getCountNones(), 13);
        assertEquals(sonar.getScannerMap().size(), 32);
    }
}