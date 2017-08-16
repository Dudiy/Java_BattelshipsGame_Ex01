package GameLogic.Game.GameObjects.Ship;

import GameLogic.Game.Board.BoardCoordinates;

public class RegularShip extends AbstractShip {
    public RegularShip(int length, BoardCoordinates position, Enum direction, int score) {
        super(length, position, direction, score);
    }



    public enum eShipDirection {
        COLUMN,
        ROW;
    }
    @Override
    public void setDirection(String direction) {
        this.direction = eShipDirection.valueOf(direction);
    }

    @Override
    public Enum getDirection() {
        return (eShipDirection)direction;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new RegularShip(this.getLength(), this.getPosition(),this.getDirection(),this.getScore());
    }
}
