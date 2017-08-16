package GameLogic.Game;

import GameLogic.Exceptions.InvalidGameObjectPlacementException;
import GameLogic.Game.Board.Board;
import GameLogic.Game.GameObjects.Ship.*;
import GameLogic.Users.*;
import jaxb.generated.BattleShipGame;

import java.util.HashMap;
import java.util.Map;

public class Game {
    private static int IDGenerator = 1000;
    private int ID;
    private boolean gameIsSet = false;
    private Player activePlayer;
    private Player otherPlayer;
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

    public Player getActivePlayer() {
        return activePlayer;
    }

    public Player getOtherPlayer() {
        return otherPlayer;
    }

    public eGameState getGameState() {
        return gameState;
    }

    public Board getActiveBoard() {
        return activePlayer.getBoard();
    }

    public Board getHiddenBoard() throws Exception {
        return otherPlayer.getBoard().HideAllHidables();
    }

    // ======================================= Methods =======================================

    public void initGame(Player player1, Player player2) throws Exception {
        if (player1 == null || player2 == null) {
            throw new Exception("Players cannot be null");
        }

        activePlayer = player1;
        otherPlayer = player2;
        initBoards();
        gameState = eGameState.INITIALIZED;
    }

    private void initBoards() throws Exception {
        int currentPlayerIndex = 0;
        Board[] initializedBoards = new Board[2];
        Player currentPlayer = activePlayer;

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
                    String message = String.format("Error while initializing " + currentPlayer.getName() + "'s board, inner exception: " + e.getMessage());
                    throw new Exception(message);
                }
            }

            // TODO check all ships are on board

            // the xsd file forces the input to have exactly 2 boards
            currentPlayer.setBoard(currentBoard);
            currentPlayer = otherPlayer;
        }

        gameIsSet = true;
    }
}
