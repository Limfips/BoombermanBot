package ru.codebattle.client.service;

import lombok.val;
import org.junit.Test;
import ru.codebattle.client.api.GameBoard;
import static org.junit.Assert.*;

/**
 * @author Dudka Leonid RPIS-81
 */
public class SonarServiceTest {

    private static final String boardString = "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼                  #  # #     ☼☼ ☼   ☼   ☼       ☼   ☼## ☼&  ☼☼                         #   ☼☼   ☼   ☼   ☼   ☼   ☼   ☼   ☼ ☼☼   ♥       ♥          ♥ #    ☼☼ ☼1  ☼   ☼       ☼   ☼   ☼   ☼☼              #            ##☼☼   ☼ # ☼   ☼   ☼   ☼ ##☼   ☼ ☼☼                ♥ ##  #      ☼☼ ☼   ☼   ☼ ♥     ☼   ☼## ☼ ☼ ☼☼                   #         ☼☼   ☼   ☼#  ☼   ☼   ☼   ☼   ☼ ☼☼                ♥ #          ☼☼ ☼   ☼ # ☼ # ☼ ##☼   ☼   ☼   ☼☼                             ☼☼  &☼   ☼  &☼   ☼#  ☼  #☼   ☼ ☼☼                       &  & #☼☼ ☼   ☼   ☼       ☼  #☼ # ☼   ☼☼                             ☼☼   ☼   ☼   ☼   ☼   ☼ # ☼   ☼#☼☼       &  #         #  ##    ☼☼ ☼   ☼   ☼       ☼#  ☼   ☼#  ☼☼    &                        ☼☼   ☼   ☼   ☼   ☼   ☼## ☼   ☼ ☼☼                           # ☼☼ ☼   ☼   ☼     & ☼  #☼   ☼#  ☼☼                   &  ♥##  ##☼☼ # ☼#  ☼#  ☼   ☼   ☼   ☼ # ☼ ☼☼      #  #       &     # ☺  #☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼";

    private static final GameBoard gameBoard = new GameBoard(boardString);

    @Test
    public void testScan() {
        val characterPoint = gameBoard.getBomberman().get(0);
        SonarService sonar = new SonarService(gameBoard);
        sonar.scan(characterPoint);

        assertEquals(sonar.getCountWalls(), 11);
        assertEquals(sonar.getCountDestroyWalls(), 7);
        assertEquals(sonar.getCountNones(), 13);
        assertEquals(sonar.getScannerMap().size(), 32);
    }
}