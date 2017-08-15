package GameLogic.Game.GameObjects;

import GameLogic.Game.Board.BoardCoordinates;

public abstract class GameObject {
    private BoardCoordinates position;

    public GameObject(BoardCoordinates position) {
        this.position = position;
    }

    public void setPosition(BoardCoordinates position) {
        this.position = position;
    }

    public BoardCoordinates getCoordinates(){
        return position;
    }
}
