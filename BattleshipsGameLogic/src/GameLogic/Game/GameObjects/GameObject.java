package GameLogic.Game.GameObjects;

import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Users.eAttackResult;

public abstract class GameObject implements Cloneable {
    private BoardCoordinates position;
    private String objectTypeSimpleName;
    private boolean hit = false;

    public boolean isHit() {
        return hit;
    }

    public final eAttackResult Attack() {
        eAttackResult attackResult;

        if (hit) {
            attackResult = eAttackResult.CELL_ALREADY_ATTACKED;
        } else {
            hit = true;
            attackResult = getAttackResult();
        }

        return attackResult;
    }

    public GameObject(String objectTypeSimpleName, BoardCoordinates position) {
        this.objectTypeSimpleName = objectTypeSimpleName;
        this.position = position;
    }

    public void setPosition(BoardCoordinates position) {
        this.position = position;
    }

    public BoardCoordinates getPosition() {
        return position;
    }

    public String getObjectTypeSimpleName() {
        return objectTypeSimpleName;
    }

    public abstract eAttackResult getAttackResult();

    @Override
    public abstract Object clone() throws CloneNotSupportedException;
}
