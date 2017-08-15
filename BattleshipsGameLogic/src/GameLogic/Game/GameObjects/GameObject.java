package GameLogic.Game.GameObjects;

import GameLogic.Game.Board.BoardCoordinates;

public abstract class GameObject {
    private BoardCoordinates position;
    private String objectTypeSimpleName;

    public GameObject(String objectTypeSimpleName, BoardCoordinates position) {
        this.objectTypeSimpleName = objectTypeSimpleName;
        this.position = position;
    }

    public void setPosition(BoardCoordinates position) {
        this.position = position;
    }

    public BoardCoordinates getCoordinates(){
        return position;
    }

    public String getObjectTypeSimpleName() {
        return objectTypeSimpleName;
    }
}
