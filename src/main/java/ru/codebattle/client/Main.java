//test
package ru.codebattle.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import ru.codebattle.client.algorithm.AStar;
import ru.codebattle.client.algorithm.Map;
import ru.codebattle.client.api.*;
import ru.codebattle.client.service.SonarServiceHelper;

public class Main {
    private static final String SERVER_ADDRESS =
            "http://codebattle2020s1.westeurope.cloudapp.azure.com/codenjoy-contest/board/player/tvq0nhelcofofsi75v1v?code=7025227631678866934";

    static boolean isScan = false;
    static boolean isAchieved = true;
    static int current = 5;
    static BoardPoint goal;
    static List<BoardPoint> points = new ArrayList<>();
    public static void main(String[] args) throws URISyntaxException, IOException {
        CodeBattleClient client = new CodeBattleClient(SERVER_ADDRESS);
        client.run(gameBoard -> {
            SonarServiceHelper helper = new SonarServiceHelper(gameBoard);
            if (!isScan){
                points.add(helper.getFirstNonePoint(new BoardPoint(gameBoard.size()/2,gameBoard.size()/2)));
                points.add(helper.getFirstNonePoint(new BoardPoint(0,0)));
                points.add(helper.getFirstNonePoint(new BoardPoint(gameBoard.size(),0)));
                points.add(helper.getFirstNonePoint(new BoardPoint(0,gameBoard.size()-1)));
                points.add(helper.getFirstNonePoint(new BoardPoint(gameBoard.size()-1,gameBoard.size()-1)));
                isScan = true;
            }
            BoardPoint bot = gameBoard.getBomberman().get(0);
            Map map = new Map(gameBoard.getBarriers(), gameBoard.size(),gameBoard.getMeatchoppers(),bot,gameBoard.getBombs(),gameBoard.getBoardString());
            if (isAchieved){
                if (current>=5){
                    current = 0;
                } else{
                    current++;
                }
                goal = points.get(current);
                isAchieved = false;
            }
            System.out.println("Goal:" + goal.toString());
            System.out.println("Bot:" + bot.toString());
            if (goal.equals(bot)){
                isAchieved = true;
            }
            AStar aStar = new AStar(map, goal, bot);
            Direction step = aStar.getNextStep();
            System.out.println(step);
            if (aStar.isStack()){
                current++;
                if (current>=5){
                    current = 0;
                }
                goal = points.get(current);
            }
            boolean act;
            helper.scan(bot);
            act =  (helper.getMeatChopperPoints().size() > 0 || helper.isDestroyWall(1) || helper.getOtherBomberPoints().size()>0) && !helper.isDangerous(2) ;
            return new TurnAction(act,step);
        });

        System.in.read();

        client.initiateExit();
    }
}
