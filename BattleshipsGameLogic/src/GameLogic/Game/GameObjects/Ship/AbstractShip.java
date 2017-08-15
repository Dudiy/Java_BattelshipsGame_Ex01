package GameLogic.Game.GameObjects.Ship;

import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.GameObjects.GameObject;

public abstract class AbstractShip extends GameObject {
    private int length;
    private BoardCoordinates position;
    // TODO make the enum direction abstract
    protected Enum direction;
    private int score; // TODO exercise 2

    public AbstractShip(int length, BoardCoordinates position, Enum direction, int score) {
        this.length = length;
        this.position = position;
        this.direction = direction;
        this.score = score;
    }

    public int getLength() {
        return length;
    }

    public BoardCoordinates getPosition() {
        return position;
    }

    public int getScore() {
        return score;
    }

    public abstract void setDirection(String direction);

    public abstract Enum getDirection();
}
