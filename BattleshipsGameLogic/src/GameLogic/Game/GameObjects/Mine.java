package GameLogic.Game.GameObjects;

import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.eAttackResult;

public class Mine extends GameObject {

    public Mine(BoardCoordinates position) {
        super("Mine", position, !VISIBLE);
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
