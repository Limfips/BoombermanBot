package ru.codebattle.client.api;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TurnAction {
    private static final String ACT_COMMAND_PREFIX = "ACT,";

    private final boolean act;
    private final Direction direction;


    public TurnAction(BoardPoint nextStep,BoardPoint botPosition){
        this.act = false;
        this.direction = determineDirection(nextStep,botPosition);
    }


    /**
     * Sets bot direction
     *
     * @param nextStep Next bot position
     * @param currentBotPosition Current bot position
     *
     * @return bot direction
     *
     */
    public Direction determineDirection(BoardPoint nextStep, BoardPoint currentBotPosition) {
        if (nextStep.getX() != currentBotPosition.getX()) {
            if (compareCoordinates(nextStep.getX(), currentBotPosition.getX())) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } else if (compareCoordinates(nextStep.getY(), currentBotPosition.getY())) {
            return Direction.DOWN;
        } else {
            return Direction.UP;
        }
    }

    private boolean compareCoordinates(int a1, int a2) {
        return a1 > a2;
    }

    @Override
    public String toString() {
        var cmd = act ? ACT_COMMAND_PREFIX : "";
        return cmd + direction.toString();
    }
}
