package GameLogic.Game.GameObjects;

import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Users.eAttackResult;

public abstract class GameObject implements Cloneable {
    private BoardCoordinates position;
    private String objectTypeSimpleName;

    public GameObject(String objectTypeSimpleName, BoardCoordinates position) {
        this.objectTypeSimpleName = objectTypeSimpleName;
        this.position = position;
    }

    // ======================================= setters =======================================
    public void setPosition(BoardCoordinates position) {
        this.position = position;
    }

    // ======================================= getters =======================================
    public BoardCoordinates getPosition() {
        return position;
    }

    public String getObjectTypeSimpleName() {
        return objectTypeSimpleName;
    }

    // ======================================= methods =======================================
    public final eAttackResult Attack() {
        return getAttackResult();
    }
    public abstract eAttackResult getAttackResult();

    @Override
    public abstract Object clone() throws CloneNotSupportedException;
}

//    private boolean hit = false;
//
//    public boolean isHit() {
//        return hit;
//    }

//
//    public final eAttackResult Attack() {
//        return getAttackResult();
////        eAttackResult attackResult;
////        if (hit) {
////            attackResult = eAttackResult.CELL_ALREADY_ATTACKED;
////        } else {
////            hit = true;
////            attackResult = getAttackResult();
////        }
////
////        return attackResult;
//    }