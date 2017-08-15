package GameLogic.Game.GameObjects.Ship;

import GameLogic.Game.GameSettings;
import jaxb.generated.BattleShipGame;

public class ShipFactory {
    private GameSettings gameSettings;

    public ShipFactory(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    public AbstractShip CreateShip(BattleShipGame.Boards.Board.Ship i_Ship) throws Exception {
        String shipCatagory = getShipCategory(i_Ship.getShipTypeId());
        AbstractShip shipObject;

        if (shipCatagory.equals("REGULAR")) {
            shipObject = new RegularShip();
        } else if (shipCatagory.equals("L_SHAPE")) {
            shipObject = new LShapeShip();
        } else {
            throw new Exception("Ship factory error - invalid ship catagory");
        }

        return shipObject;
    }

    private String getShipCategory(String shipTypeID){
        return gameSettings.getShipTypes().get(shipTypeID).getCategory();
    }
}
