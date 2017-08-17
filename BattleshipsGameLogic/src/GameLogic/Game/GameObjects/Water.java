package GameLogic.Game.GameObjects;

import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Users.eAttackResult;

public class Water extends GameObject {
    public Water(BoardCoordinates position) {
        super("Water", position);
    }

    @Override
    public eAttackResult getAttackResult() {
        return eAttackResult.HIT_WATER;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new Water(this.getPosition());
    }
}
