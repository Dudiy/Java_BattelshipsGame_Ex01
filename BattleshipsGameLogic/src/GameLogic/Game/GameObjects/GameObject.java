package GameLogic.Game.GameObjects;

import GameLogic.Game.Board.BoardCoordinates;

public abstract class GameObject implements Cloneable{
    private BoardCoordinates position;
    private String objectTypeSimpleName;
    private boolean Hit = false;

    public boolean isHit() {
        return Hit;
    }

    public void Attack(){
        Hit = true;
    }

    public GameObject(String objectTypeSimpleName, BoardCoordinates position) {
        this.objectTypeSimpleName = objectTypeSimpleName;
        this.position = position;
    }

    public void setPosition(BoardCoordinates position) {
        this.position = position;
    }

    public BoardCoordinates getPosition(){
        return position;
    }

    public String getObjectTypeSimpleName() {
        return objectTypeSimpleName;
    }

    @Override
    public abstract Object clone() throws CloneNotSupportedException;
}
