package GameLogic.Game.GameObjects;

import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.eAttackResult;

public class Water extends GameObject {
    public Water(BoardCoordinates position) {
        super("Water", position);
    }

    // ======================================= getters =======================================
    @Override
    public eAttackResult getAttackResult() {
        return eAttackResult.HIT_WATER;
    }

    // ======================================= methods =======================================
    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Water(this.getPosition());
    }
}
