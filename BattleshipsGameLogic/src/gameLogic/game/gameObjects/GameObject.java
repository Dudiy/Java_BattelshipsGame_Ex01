package gameLogic.game.gameObjects;

import gameLogic.game.board.BoardCoordinates;
import gameLogic.game.eAttackResult;

import java.io.Serializable;

public abstract class GameObject implements Serializable {
    protected static final boolean VISIBLE = true;
    private BoardCoordinates position;
    private static String objectTypeSimpleName;
    private boolean isVisible;

    public GameObject(String objectTypeSimpleName, BoardCoordinates position, boolean isVisible) {
        this.objectTypeSimpleName = objectTypeSimpleName;
        this.position = position;
        this.isVisible = isVisible;
    }

    // ======================================= setters =======================================
    public void setPosition(BoardCoordinates position) {
        this.position = position;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    // ======================================= getters =======================================
    public BoardCoordinates getPosition() {
        return position;
    }

    public static String getObjectTypeSimpleName() {
        return objectTypeSimpleName;
    }

    public boolean isVisible() {
        return isVisible;
    }

    // ======================================= methods =======================================
    public final eAttackResult attack() {
        isVisible = true;
        return getAttackResult();
    }

    public abstract eAttackResult getAttackResult();
}
