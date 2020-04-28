//test
package ru.codebattle.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import ru.codebattle.client.algorithm.AStar;
import ru.codebattle.client.algorithm.Map;
import ru.codebattle.client.api.*;
import ru.codebattle.client.service.SonarServiceHelper;

public class Main {
    private static final String SERVER_ADDRESS =
            "http://codebattle2020s1.westeurope.cloudapp.azure.com/codenjoy-contest/board/player/tvq0nhelcofofsi75v1v?code=7025227631678866934";

    static boolean isScan = false;
    static boolean isAchieved = true;
    static int current = 1;
    static BoardPoint goal;
    static List<BoardPoint> points = new ArrayList<>();
    public static void main(String[] args) throws URISyntaxException, IOException {
        CodeBattleClient client = new CodeBattleClient(SERVER_ADDRESS);
        client.run(gameBoard -> {
            SonarServiceHelper helper = new SonarServiceHelper(gameBoard);
            if (!isScan){
                points.add(helper.getFirstNonePoint(new BoardPoint(0,0)));
                points.add(helper.getFirstNonePoint(new BoardPoint(gameBoard.size(),0)));
                points.add(helper.getFirstNonePoint(new BoardPoint(0,gameBoard.size())));
                points.add(helper.getFirstNonePoint(new BoardPoint(gameBoard.size(),gameBoard.size())));
                isScan = true;
            }
            BoardPoint bot = gameBoard.getBomberman().get(0);
            Map map = new Map(gameBoard.getBarriers(), gameBoard.size(),gameBoard.getMeatchoppers(),bot,gameBoard.getBombs(),gameBoard.getBoardString());
            if (isAchieved){
                if (current>=1){
                    current = 0;
                } else{
                    current++;
                }
                goal = points.get(current);
                isAchieved = false;
            }
            BoardPoint goal = new BoardPoint(gameBoard.size()-2,gameBoard.size()-2);
            if (goal.equals(bot)){
                isAchieved = true;
            }
            System.out.println(goal);
            AStar aStar = new AStar(map, goal, bot);
            Direction step = aStar.getNextStep();
            boolean act;
            helper.scan(bot);
            act =  (helper.getMeatChopperPoints().size() > 0 || helper.isDestroyWall(1)) && !helper.isDangerous(3) ;
            System.out.println(helper.getMeatChopperPoints().size());
            System.out.println(helper.isDestroyWall(1));
            return new TurnAction(act,step);
        });

        System.in.read();

        client.initiateExit();
    }
}
