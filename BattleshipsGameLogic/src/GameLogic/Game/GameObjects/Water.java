package GameLogic.Game.GameObjects;

import GameLogic.Game.Board.BoardCoordinates;

public class Water extends GameObject {
    public Water(BoardCoordinates position) {
        super("Water", position);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Water(this.getPosition());
    }
}
