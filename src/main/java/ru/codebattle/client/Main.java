//MrD0C
package ru.codebattle.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import ru.codebattle.client.algorithm.AStar;
import ru.codebattle.client.algorithm.Map;
import ru.codebattle.client.algorithm.Node;
import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.Direction;
import ru.codebattle.client.api.TurnAction;

public class Main {

    private static final String SERVER_ADDRESS = "http://codebattle2020s1.westeurope.cloudapp.azure.com/codenjoy-contest/board/player/tvq0nhelcofofsi75v1v?code=7025227631678866934";


    public static void main(String[] args) throws URISyntaxException, IOException {
        CodeBattleClient client = new CodeBattleClient(SERVER_ADDRESS);
        client.run(gameBoard -> {
            Map map = new Map(gameBoard.getBarriers(), gameBoard.size(),gameBoard.getMeatchoppers());
            BoardPoint goal = new BoardPoint(4, 2);
            AStar aStar = new AStar(map, goal, gameBoard.getBomberman().get(0));
            Direction step = aStar.getNextStep();
            return new TurnAction(false,step);
        });

        System.in.read();

        client.initiateExit();
    }
}
