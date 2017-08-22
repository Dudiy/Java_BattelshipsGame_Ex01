package GameLogic.Game.GameObjects;

import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.eAttackResult;

public class Mine extends GameObject {
    eAttackResult explosionResult;

    public Mine(BoardCoordinates position) {
        super("Mine", position, !VISIBLE);
    }

    // ======================================= setters =======================================
    public void setExplosionResult(eAttackResult explosionResult) {
        this.explosionResult = explosionResult;
    }

    // ======================================= getters =======================================
    public eAttackResult getExplosionResult() {
        return explosionResult;
    }

    @Override
    public eAttackResult getAttackResult() {
        return eAttackResult.HIT_MINE;
    }
}
