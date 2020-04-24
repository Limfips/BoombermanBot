//test
package ru.codebattle.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import lombok.val;
import ru.codebattle.client.api.*;

public class Main {
    private static final String SERVER_ADDRESS =
            "http://codebattle2020s1.westeurope.cloudapp.azure.com/codenjoy-contest/board/player/tvq0nhelcofofsi75v1v?code=7025227631678866934";

    public static void main(String[] args) throws URISyntaxException, IOException {
        CodeBattleClient client = new CodeBattleClient(SERVER_ADDRESS);
        client.run(gameBoard -> {
//            //ToDo логика по умолчанию
//            var random = new Random(System.currentTimeMillis());
//            var direction = Direction.values()[random.nextInt(Direction.values().length)];
//            var act = random.nextInt() % 2 == 0;
//            return new TurnAction(act, direction);

            //1. Определяет своё положение на карте
            val characterPoint = gameBoard.getBomberman().get(0);
            //2. проверяет свободные вокруг себя клетки в радиусе 1 клетка (9 клеток)
            if (validBoard(gameBoard, characterPoint)) {

            }
            val result =
                    gameBoard.getElementAt(characterPoint.shiftBottom()).toString() +
                    gameBoard.getElementAt(characterPoint.shiftTop()).toString() +
                    gameBoard.getElementAt(characterPoint.shiftLeft()).toString() +
                    gameBoard.getElementAt(characterPoint.shiftRight()).toString();
            System.out.println(result);
            return new TurnAction(true, Direction.RIGHT);
        });

        System.in.read();

        client.initiateExit();
    }

    private static boolean validBoard(GameBoard gameBoard, BoardPoint characterPoint) {
        List<BoardElement> elements = new ArrayList<>();
        elements.add(gameBoard.getElementAt(characterPoint.shiftBottom()));
        if (elements.size() > 5) {
            return false;
        } else {
            return true;
        }
    }
}
