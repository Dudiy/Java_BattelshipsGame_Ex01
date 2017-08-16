package GameLogic.Game.GameObjects.Ship;

import GameLogic.Game.Board.BoardCoordinates;

public class LShapeShip extends AbstractShip {


    public enum eShipDirection{
        DOWN_RIGHT,
        UP_RIGHT,
        RIGHT_UP,
        RIGHT_DOWN;
    }
    public LShapeShip(int length, BoardCoordinates position, Enum direction, int score) {
        super(length, position, direction, score);
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
        return new LShapeShip(this.getLength(),this.getPosition(),this.getDirection(),this.getScore());
    }
}
