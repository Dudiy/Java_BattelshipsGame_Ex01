package GameLogic.Game;

import GameLogic.Exceptions.CellNotOnBoardException;
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
    private Player[] players = new Player[2];
    private int activePlayerIndex;
    private int movesCounter = 0;
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
        return players[activePlayerIndex];
    }

    public Player getOtherPlayer() {
        return players[(activePlayerIndex + 1) % 2];
    }

    public eGameState getGameState() {
        return gameState;
    }

    // ======================================= methods =======================================

    public void initGame(Player player1, Player player2) throws Exception {
        if (player1 == null || player2 == null) {
            throw new Exception("Players cannot be null");
        }

        activePlayerIndex = 0;
        players[0] = player1;
        players[1] = player2;
        initBoards();
        gameState = eGameState.INITIALIZED;
    }

    private void initBoards() throws Exception {
        int currentBoardIndex = 0;
        BattleShipGame gameLoadedFromXml = gameSettings.getGameLoadedFromXml();

        for (BattleShipGame.Boards.Board board : gameLoadedFromXml.getBoards().getBoard()) {
            Board currentBoard = addAllShipsToBoard(getActivePlayer(), board);
            players[currentBoardIndex].setMyBoard(currentBoard);
            players[(currentBoardIndex + 1) % 2].setOpponentBoard(currentBoard);
            currentBoardIndex++;
        }

        gameIsSet = true;
    }

    private Board addAllShipsToBoard(Player currentPlayer, BattleShipGame.Boards.Board board) throws Exception {
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
        return currentBoard;
    }

    public eAttackResult attack(BoardCoordinates position) throws CellNotOnBoardException {
        eAttackResult attackResult = getActivePlayer().attack(position);

        if (attackResult != eAttackResult.CELL_ALREADY_ATTACKED){
            movesCounter++;
        }

        if (attackResult.moveEnded()) {
            swapPlayers();
        }

        return attackResult;
    }

    private void swapPlayers() {
        activePlayerIndex = (activePlayerIndex + 1) % 2;
    }
}