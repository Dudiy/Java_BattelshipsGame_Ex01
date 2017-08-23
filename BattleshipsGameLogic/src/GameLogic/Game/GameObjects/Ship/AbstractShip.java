package GameLogic.Game.GameObjects.Ship;

import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.GameObjects.GameObject;
import GameLogic.Game.eAttackResult;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;

public abstract class AbstractShip extends GameObject {
    private int length;
    private int hitsRemainingUntilSunk;
    protected eShipDirection direction;
    // TODO implement in exercise 2
    private int score;

    public AbstractShip(int length, BoardCoordinates position, eShipDirection direction, int score) {
        super("Ship", position, !VISIBLE);
        this.length = length;
        this.hitsRemainingUntilSunk = length;
        this.direction = direction;
        this.score = score;
    }

    // ======================================= setters =======================================

    public abstract void setDirection(String direction) throws Exception;
    // ======================================= getters =======================================

    public int getLength() {
        return length;
    }

    public int getScore() {
        return score;
    }

    public abstract eShipDirection getDirection();

    public int getHitsRemainingUntilSunk() {
        return hitsRemainingUntilSunk;
    }

    // ======================================= methods =======================================
    @Override
    public eAttackResult getAttackResult() {
        eAttackResult attackResult;

        hitsRemainingUntilSunk--;
        if (hitsRemainingUntilSunk == 0) {
            attackResult = eAttackResult.HIT_AND_SUNK_SHIP;
        } else if (hitsRemainingUntilSunk > 0) {
            attackResult = eAttackResult.HIT_SHIP;
        } else {
            throw new ValueException("Hits remaining until sunk can't be negative");
        }

        return attackResult;
    }
}
