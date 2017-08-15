package GameLogic.Exceptions;


import GameLogic.Game.Board.BoardCoordinates;

public class InvalidGameObjectPlacementException extends Exception {
    private BoardCoordinates coordinates;
    private String reason;

    private String gameObjectType;

    public InvalidGameObjectPlacementException(String gameObjectType, BoardCoordinates coordinates, String reason) {
        this.coordinates = coordinates;
        this.reason = reason;
        this.gameObjectType = gameObjectType;
    }

    @Override
    public String getMessage() {
        return String.format("Game object cannot be placed at " + coordinates + ". " + reason);
    }

    public BoardCoordinates GetCoordinates() {
        return coordinates;
    }

    public String getReason() {
        return reason;
    }

    public String getGameObjectType() {
        return gameObjectType;
    }
}
