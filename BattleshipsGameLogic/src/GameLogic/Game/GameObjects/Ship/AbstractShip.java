package GameLogic.Game.GameObjects.Ship;

import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.GameObjects.GameObject;
import GameLogic.Game.GameObjects.IHidable;

public abstract class AbstractShip extends GameObject implements IHidable {
    private int length;
    // TODO make the enum direction abstract
    protected Enum direction;
    private int score; // TODO exercise 2

    public boolean isSunken() {
        return isSunken;
    }

    public void setSunken(boolean sunken) {
        isSunken = sunken;
    }

    private boolean isSunken = false;

    public AbstractShip(int length, BoardCoordinates position, Enum direction, int score) {
        super("Ship", position);
        this.length = length;
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
}
