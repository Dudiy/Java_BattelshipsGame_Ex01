package GameLogic.Game.GameObjects;

import GameLogic.Game.Board.BoardCoordinates;

public class Mine extends GameObject implements IHidable {

    public Mine(BoardCoordinates position) {
        super("Mine", position);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Mine(this.getPosition());
    }
}
