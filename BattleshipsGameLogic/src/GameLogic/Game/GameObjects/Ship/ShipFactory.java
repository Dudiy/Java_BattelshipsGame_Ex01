package GameLogic.Game.GameObjects.Ship;

import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.GameSettings;
import jaxb.generated.BattleShipGame;

public class ShipFactory {
    private GameSettings gameSettings;

    public ShipFactory(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    public AbstractShip createShip(BattleShipGame.Boards.Board.Ship ship) throws Exception {
        BattleShipGame.ShipTypes.ShipType shipType = getShipType(ship.getShipTypeId());
        String shipCatagory = shipType.getCategory();
        AbstractShip shipObject;
        BoardCoordinates coordinates = new BoardCoordinates(ship.getPosition().getX(), ship.getPosition().getY());

        if (shipCatagory.equals("REGULAR")) {
            RegularShip.eShipDirection direction = RegularShip.eShipDirection.valueOf(ship.getDirection());
            shipObject = new RegularShip(shipType.getLength(), coordinates, direction, shipType.getScore());
        } else if (shipCatagory.equals("L_SHAPE")) {
            LShapeShip.eShipDirection direction = LShapeShip.eShipDirection.valueOf((ship.getDirection()));
            shipObject = new LShapeShip(shipType.getLength(), coordinates, direction, shipType.getScore());
        } else {
            throw new Exception("Ship factory error - invalid ship category");
        }

        return shipObject;
    }

    private BattleShipGame.ShipTypes.ShipType getShipType(String shipTypeID) {
        return gameSettings.getShipTypes().get(shipTypeID);
    }
}
