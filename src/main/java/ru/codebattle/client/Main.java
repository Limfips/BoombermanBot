//MrD0C
package ru.codebattle.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;

import ru.codebattle.client.algorithm.AStar;
import ru.codebattle.client.algorithm.Map;
import ru.codebattle.client.algorithm.Node;
import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.Direction;
import ru.codebattle.client.api.TurnAction;

public class Main {

    private static final String SERVER_ADDRESS = "http://localhost:8080/codenjoy-contest/board/player/6mlolfpaekvspk868rdh?code=1855478191833212450&gameName=bomberman";

    public static void main(String[] args) throws URISyntaxException, IOException {
        CodeBattleClient client = new CodeBattleClient(SERVER_ADDRESS);
        int i = 0;
        client.run(gameBoard -> {
            Map map = new Map(gameBoard.getBarriers(),gameBoard.size(),gameBoard.getBoardString());
            BoardPoint goal = new BoardPoint(15,6);
            AStar aStar = new AStar(map,goal,gameBoard.getBomberman().get(0));
            List<Node> path = aStar.getPath();
            BoardPoint bot = gameBoard.getBomberman().get(0);
            BoardPoint nextPath = path.get(0).getLocation();
            var direction = Direction.values()[0];
            TurnAction turnAction = new TurnAction(true,direction);
            if (nextPath.getX() != bot.getX()){
                if (nextPath.getX()>bot.getX()){
                    turnAction = new TurnAction(true,Direction.RIGHT);
                } else {
                    turnAction = new TurnAction(true,Direction.LEFT);
                }
            } else {
                if (nextPath.getY()>bot.getY()){
                    turnAction = new TurnAction(true,Direction.DOWN);
                } else {
                    turnAction = new TurnAction(true,Direction.UP);
                }
            }
            return turnAction;
        });

        System.in.read();

        client.initiateExit();
    }
}
