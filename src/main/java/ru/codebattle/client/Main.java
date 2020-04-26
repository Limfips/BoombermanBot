package ru.codebattle.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Random;

import ru.codebattle.client.algorithm.AStar;
import ru.codebattle.client.algorithm.Map;
import ru.codebattle.client.api.BoardPoint;
import ru.codebattle.client.api.Direction;
import ru.codebattle.client.api.TurnAction;
import ru.codebattle.client.service.IPointsListener;
import ru.codebattle.client.service.SonarService;

public class Main {

    private static final String SERVER_ADDRESS = "http://codebattle2020s1.westeurope.cloudapp.azure.com/codenjoy-contest/board/player/tvq0nhelcofofsi75v1v?code=7025227631678866934";


    //Возможно ещё задержка происходит тк что-то долго обрабатывается
    static int i =0;
    public static void main(String[] args) throws URISyntaxException, IOException {
        CodeBattleClient client = new CodeBattleClient(SERVER_ADDRESS);
        client.run(gameBoard -> {
            BoardPoint bot = gameBoard.getBomberman().get(0);
            Map map = new Map(gameBoard.getBarriers(), gameBoard.size(),gameBoard.getMeatchoppers(),bot);
            BoardPoint goal = new BoardPoint(gameBoard.size()-2, 2);
            AStar aStar = new AStar(map, goal, bot);
            Direction step = aStar.getNextStep();
            boolean act = false;
            SonarService service = new SonarService(gameBoard);
            service.addListenerAlertEnemy(new IPointsListener() {
                @Override
                public void alertEnemyNotify(List<BoardPoint> points) {
                }

                @Override
                public void alertBombsNotify(List<BoardPoint> points) {

                }
            });
            service.scan(bot);
            if (service.getCountOtherBomber() > 0 && i == 2){
                act = true;
                i = 0;
            } else {
                i++;
            }
            return new TurnAction(act,step);
        });

        System.in.read();

        client.initiateExit();
    }
}
