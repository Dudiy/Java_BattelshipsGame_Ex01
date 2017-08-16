package GameLogic.Game;

import GameLogic.Exceptions.InvalidGameObjectPlacementException;
import GameLogic.Game.Board.Board;
import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.GameObjects.Ship.*;
import GameLogic.Users.*;
import jaxb.generated.BattleShipGame;

import java.util.HashMap;
import java.util.Map;

public class Game {
    private static int IDGenerator = 1000;
    private int ID;
    private boolean gameIsSet = false;
    private Player[] players = new Player[ 2 ];
    private Player activePlayer;
    private Map<String, User> spectators = new HashMap<>();
    private GameSettings gameSettings;
    private ShipFactory shipFactory;
    private eGameState gameState = eGameState.INVALID;

    public Game(GameSettings gameSettings) {
        this.ID = IDGenerator++;
        this.gameSettings = gameSettings;
        shipFactory = new ShipFactory(gameSettings);
        gameState = eGameState.LOADED;
    }

    // ======================================= setters =======================================
    public void setGameState(eGameState gameState) {
        this.gameState = gameState;
    }

    // ======================================= getters =======================================

    public int getID() {
        return ID;
    }

    public Player getPlayer(int playerNum) {
        return players[ playerNum - 1 ];
    }

    public eGameState getGameState() {
        return gameState;
    }

    // ======================================= Methods =======================================
    public void initGame(Player player1, Player player2) throws Exception {
        players[ 0 ] = player1;
        players[ 1 ] = player2;
        activePlayer = players[ 0 ];
        initBoards();
        //gameState = eGameState.INITIALIZED;
    }

    private void initBoards() throws Exception {
        int currentPlayerIndex = 0;

        BattleShipGame gameLoadedFromXml = gameSettings.getGameLoadedFromXml();

        for (BattleShipGame.Boards.Board board : gameLoadedFromXml.getBoards().getBoard()) {
            Board currentBoard = new Board(gameSettings.getBoardSize());
            currentBoard.setMinesAvailable(gameSettings.getMinesPerPlayer());

            for (BattleShipGame.Boards.Board.Ship ship : board.getShip()) {
                try {
                    AbstractShip shipObject = shipFactory.createShip(ship);
                    currentBoard.addShipToBoard(shipObject);
                } catch (InvalidGameObjectPlacementException e) {
                    throw e;
                } catch (Exception e) {
                    String message = String.format("Error while initializing board of player \"" + players[ currentPlayerIndex ].getName() + "\", inner exception: " + e.getMessage());
                    throw new Exception(message);
                }
            }

            // TODO check all ships are on board

            players[ currentPlayerIndex ].setMyBoard(currentBoard);
            currentPlayerIndex++;
        }

        gameIsSet = true;
    }

    public eShootReturn playMove(BoardCoordinates userSelectionCell) {
        // TODO
        return null;
    }
}
