package GameLogic.Exceptions;


import GameLogic.Game.Board.BoardCoordinates;

public class InvalidGameObjectPlacementException extends Exception {
    BoardCoordinates coordinates;

    public InvalidGameObjectPlacementException(BoardCoordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String getMessage() {
        return String.format("Game object cannot be placed at " + coordinates);
    }

    public BoardCoordinates GetCoordinates(){
        return coordinates;
    }
}
