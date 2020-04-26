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
        SonarServiceHelper sonar = new SonarServiceHelper(gameBoard);
        sonar.scan(characterPoint);

        assertEquals(sonar.getWallsPoints().size(), 11);
        assertEquals(sonar.getDestroyWallsPoints().size(), 7);
        assertEquals(sonar.getNonesPoints().size(), 13);
        assertEquals(sonar.getScannerMap().size(), 32);
    }

    @Test
    public void secondTestScan() {
        GameBoard gameBoard = new GameBoard(secondBoardString);
        val characterPoint = gameBoard.getBomberman().get(0);
        SonarService sonar = new SonarService(gameBoard);
        sonar.scan(characterPoint);
        gameBoard.printBoard();

        assertEquals(sonar.getWallsPoints().size(), 9);
        assertEquals(sonar.getDestroyWallsPoints().size(), 0);
        assertEquals(sonar.getNonesPoints().size(), 21);
        assertEquals(sonar.getScannerMap().size(), 32);
    }

    @Test
    public void XXX() {
        GameBoard gameBoard = new GameBoard(firstBoardString);
        val characterPoint = gameBoard.getBomberman().get(0);
        SonarService sonar = new SonarService(gameBoard);
        sonar.scan(characterPoint);



        assertEquals(sonar.getWallsPoints().size(), 11);
        assertEquals(sonar.getDestroyWallsPoints().size(), 7);
        assertEquals(sonar.getNonesPoints().size(), 13);
        assertEquals(sonar.getScannerMap().size(), 32);
    }
}