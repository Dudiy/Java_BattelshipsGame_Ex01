package GameLogic.Game.GameObjects;

import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Users.eAttackResult;

public class Mine extends GameObject {

    public Mine(BoardCoordinates position) {
        super("Mine", position);
    }

    @Override
    public eAttackResult getAttackResult() {
        return eAttackResult.HIT_MINE;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Mine(this.getPosition());
    }
}
