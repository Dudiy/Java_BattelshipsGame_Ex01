package GameLogic.Exceptions;


import GameLogic.Game.Board.BoardCoordinates;

public class InvalidShipPlacementException extends InvalidGameObjectPlacementException {

    public InvalidShipPlacementException(BoardCoordinates position) {
        super(position);
    }

    @Override
    public String getMessage() {
        return String.format("Ship cannot be placed at " + coordinates);
    }
}
