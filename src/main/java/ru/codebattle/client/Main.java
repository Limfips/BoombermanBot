package ru.codebattle.client;

import java.io.IOException;
import java.net.URISyntaxException;

import ru.codebattle.client.algorithm.AStar;
import ru.codebattle.client.algorithm.Map;
import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.Direction;
import ru.codebattle.client.api.TurnAction;
import ru.codebattle.client.service.SonarServiceHelper;

public class Main {

    private static final String SERVER_ADDRESS = "http://codebattle2020s1.westeurope.cloudapp.azure.com/codenjoy-contest/board/player/tvq0nhelcofofsi75v1v?code=7025227631678866934";


    //Возможно ещё задержка происходит тк что-то долго обрабатывается
    int isStack = 0;
    public static void main(String[] args) throws URISyntaxException, IOException {
        CodeBattleClient client = new CodeBattleClient(SERVER_ADDRESS);
        client.run(gameBoard -> {
            BoardPoint bot = gameBoard.getBomberman().get(0);
            Map map = new Map(gameBoard.getBarriers(), gameBoard.size(),gameBoard.getMeatchoppers(),bot,gameBoard.getBombs(),gameBoard.getBoardString());
            BoardPoint goal = new BoardPoint(gameBoard.size()-2, gameBoard.size()-2);
            AStar aStar = new AStar(map, goal, bot);
            Direction step = aStar.getNextStep();
            boolean act;
            SonarServiceHelper helper = new SonarServiceHelper(gameBoard);
            helper.scan(bot);
            act =  helper.getMeatChopperPoints().size() > 0 && !helper.isDangerous() ;
            System.out.println(helper.getMeatChopperPoints().size());
            return new TurnAction(act,step);
        });

        System.in.read();

        client.initiateExit();
    }
}
