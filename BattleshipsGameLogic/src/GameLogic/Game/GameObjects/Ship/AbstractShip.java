package GameLogic.Game.GameObjects.Ship;

import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.GameObjects.GameObject;
import GameLogic.Users.eAttackResult;

public abstract class AbstractShip extends GameObject {
    private int length;
    // TODO make the enum direction abstract
    protected Enum direction;
    private int score; // TODO exercise 2
    private int hitsRemainingUntilSunk;
//    private boolean isSunken = false;

//    public boolean isSunken() {
//
//        return isSunken;
//    }

//    public void setSunken(boolean sunken) {
//        isSunken = sunken;
//    }

    public AbstractShip(int length, BoardCoordinates position, Enum direction, int score) {
        super("Ship", position);
        this.length = length;
        this.hitsRemainingUntilSunk = length;
        this.direction = direction;
        this.score = score;
    }

    public int getLength() {
        return length;
    }

    public int getScore() {
        return score;
    }

    public abstract void setDirection(String direction);

    public abstract Enum getDirection();

    @Override
    public eAttackResult getAttackResult() {
        eAttackResult attackResult;

        if (hitsRemainingUntilSunk <= 1) {
            hitsRemainingUntilSunk = 0;
            attackResult = eAttackResult.HIT_AND_SUNK_SHIP;
        } else {
            hitsRemainingUntilSunk--;
            attackResult = eAttackResult.HIT_SHIP;
        }

        return attackResult;
    }
}
