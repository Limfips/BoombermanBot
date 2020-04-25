package ru.codebattle.client.service;

import ru.codebattle.client.api.BoardPoint;

import java.util.List;

/**
 * @author Dudka Leonid RPIS-81
 */
public interface IPointsListener {
    void alertEnemyNotify(List<BoardPoint> points);
    void alertWallNotify(List<BoardPoint> points);
}
