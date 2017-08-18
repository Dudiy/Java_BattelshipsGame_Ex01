package GameLogic.Game.GameObjects.Ship;

import GameLogic.Game.Board.BoardCoordinates;

public class RegularShip extends AbstractShip {
    public enum eShipDirection {
        COLUMN,
        ROW;
    }

    public RegularShip(int length, BoardCoordinates position, Enum direction, int score) {
        super(length, position, direction, score);
    }

    // ======================================= setters =======================================
    @Override
    public void setDirection(String direction) {
        this.direction = eShipDirection.valueOf(direction);
    }

    // ======================================= getters =======================================
    @Override
    public Enum getDirection() {
        return (eShipDirection)direction;
    }

    // ======================================= methods =======================================
    @Override
    public Object clone() throws CloneNotSupportedException {
        return new RegularShip(this.getLength(), this.getPosition(),this.getDirection(),this.getScore());
    }
}
