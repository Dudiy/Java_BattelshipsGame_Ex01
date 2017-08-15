package GameLogic.Game.GameObjects;

import GameLogic.Game.Board.BoardCoordinates;

public class GameObject {
    private BoardCoordinates position;

    public void setPosition(BoardCoordinates position) {
        this.position = position;
    }

    public BoardCoordinates getCoordinates() {
        return position;
    }
}
