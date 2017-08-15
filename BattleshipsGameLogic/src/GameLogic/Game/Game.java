package GameLogic.Game;

import GameLogic.Game.Board.Board;
import GameLogic.Game.GameObjects.Ship.*;
import GameLogic.Users.*;
import jaxb.generated.BattleShipGame;

import java.util.HashMap;
import java.util.Map;

public class Game {
    private boolean gameIsSet = false;
    private Player[] players = new Player[2];
    private Map<String, User> spectators = new HashMap<>();
    private GameSettings gameSettings;
    private Board[] boards = new Board[2];
    private ShipFactory shipFactory;


    public Game(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
        shipFactory = new ShipFactory(gameSettings);
    }

    public void InitGame() throws Exception {
        int currentBoardIndex = 0;

        BattleShipGame gameLoadedFromXml = gameSettings.getGameLoadedFromXml();

        for (BattleShipGame.Boards.Board board : gameLoadedFromXml.getBoards().getBoard()) {
            for (BattleShipGame.Boards.Board.Ship ship : board.getShip()) {
                AbstractShip shipObject = shipFactory.CreateShip(ship);

//                boards[currentBoardIndex].addShipToBoard(shipObject);

            }
        }
    }
}
