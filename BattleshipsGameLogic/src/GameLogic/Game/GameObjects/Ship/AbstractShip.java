package GameLogic.Game.GameObjects.Ship;

import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.GameObjects.GameObject;
import GameLogic.Users.eAttackResult;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

public abstract class AbstractShip extends GameObject{
    private int length;
    // TODO make the enum direction abstract
    protected Enum direction;
    private int score; // TODO exercise 2
    private int hitsRemainingUntilSunk;

    public AbstractShip(int length, BoardCoordinates position, Enum direction, int score) {
        super("Ship", position);
        this.length = length;
        this.hitsRemainingUntilSunk = length;
        this.direction = direction;
        this.score = score;
    }
    // ======================================= setters =======================================
    public abstract void setDirection(String direction);

    // ======================================= getters =======================================
    public int getLength() {
        return length;
    }

    public int getScore() {
        return score;
    }

    public abstract Enum getDirection();

    // ======================================= methods =======================================
    @Override
    public eAttackResult getAttackResult() {
        eAttackResult attackResult;

        hitsRemainingUntilSunk--;
        if (hitsRemainingUntilSunk == 0) {
            //hitsRemainingUntilSunk = 0;
            attackResult = eAttackResult.HIT_AND_SUNK_SHIP;
        } else if(hitsRemainingUntilSunk > 0) {
            attackResult = eAttackResult.HIT_SHIP;
        }else{
            throw new ValueException("Hits remaining until sunk can't be negative");
        }

        return attackResult;
    }
}

//    private boolean isSunken = false;

//    public boolean isSunken() {
//
//        return isSunken;
//    }

//    public void setSunken(boolean sunken) {
//        isSunken = sunken;
//    }
