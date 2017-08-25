package gameLogic.game.gameObjects.ship;

import java.io.Serializable;

import jaxb.generated.BattleShipGame;
import gameLogic.game.board.BoardCoordinates;
import gameLogic.game.GameSettings;

public class ShipFactory implements Serializable {
    private GameSettings gameSettings;

    public ShipFactory(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    public AbstractShip createShip(BattleShipGame.Boards.Board.Ship ship) throws Exception {
        BattleShipGame.ShipTypes.ShipType shipType = getShipType(ship.getShipTypeId());
        String shipCategory = shipType.getCategory();
        AbstractShip shipObject;
        BoardCoordinates coordinates = BoardCoordinates.convertFromXmlToBoard(ship.getPosition().getX(), ship.getPosition().getY());

        if (shipCategory.equals("REGULAR")) {
            eShipDirection direction = eShipDirection.valueOf(ship.getDirection());
            shipObject = new RegularShip(shipType.getLength(), coordinates, direction, shipType.getScore());
        } else if (shipCategory.equals("L_SHAPE")) {
            eShipDirection direction = eShipDirection.valueOf((ship.getDirection()));
            shipObject = new LShapeShip(shipType.getLength(), coordinates, direction, shipType.getScore());
        } else {
            throw new Exception("ship factory error - invalid ship category");
        }

        return shipObject;
    }

    private BattleShipGame.ShipTypes.ShipType getShipType(String shipTypeID) {
        return gameSettings.getShipTypes().get(shipTypeID);
    }
}
