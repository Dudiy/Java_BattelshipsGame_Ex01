package GameLogic.Game.GameObjects;

import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Users.eAttackResult;

public class Mine extends GameObject {

    public Mine(BoardCoordinates position) {
        super("Mine", position);
    }

    // ======================================= getters =======================================
    @Override
    public eAttackResult getAttackResult() {
        return eAttackResult.HIT_MINE;
    }

    // ======================================= methods =======================================
    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Mine(this.getPosition());
    }
}
