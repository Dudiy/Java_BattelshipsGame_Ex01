package GameLogic.Game;

import GameLogic.Exceptions.CellNotOnBoardException;
import GameLogic.Exceptions.InvalidGameObjectPlacementException;
import GameLogic.Game.Board.Board;
import GameLogic.Game.Board.BoardCell;
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

    // ======================================= methods =======================================

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
        //int currentPlayerIndex = 0;
        //Board[] initializedBoards = new Board[2];
        Player currentPlayer = activePlayer;
        BattleShipGame gameLoadedFromXml = gameSettings.getGameLoadedFromXml();

        for (BattleShipGame.Boards.Board board : gameLoadedFromXml.getBoards().getBoard()) {
            Board currentBoard = initBoardWithGameObject(currentPlayer, board);
            // TODO check all ships are on board
            // the xsd file forces the input to have exactly 2 boards
            currentPlayer.setMyBoard(currentBoard);
            // set a new blank opponent board
            currentPlayer.setOpponentBoard(new Board(gameSettings.getBoardSize()));
            currentPlayer = otherPlayer;
        }

        gameIsSet = true;
    }

    private Board initBoardWithGameObject(Player currentPlayer, BattleShipGame.Boards.Board board) throws Exception{
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
        // TODO player/board attack
        BoardCell cellToAttack = otherPlayer.getMyBoard().getBoardCellAtCoordinates(position);
        eAttackResult attackResult = cellToAttack.attack();
        // TODO "hit" to cell, not ship
        switch(attackResult){
            case HIT_SHIP :
                //attackResult = eAttackResult.GET_ANOTHER_MOVE;
                break;
            case HIT_AND_SUNK_SHIP:
                //attackResult = eAttackResult.GET_ANOTHER_MOVE;
                break;
            case HIT_MINE:
                activePlayer.getMyBoard().getBoardCellAtCoordinates(position).attack();
                swapPlayers();
                //attackResult = eAttackResult.MOVE_ENDED;
                break;
            case HIT_WATER:
                swapPlayers();
                //attackResult = eAttackResult.MOVE_ENDED;
                break;
            case CELL_ALREADY_ATTACKED:
                //attackResult = eAttackResult.GET_ANOTHER_MOVE;
                break;
        }

        return attackResult;
    }

    private void swapPlayers() {
        Player tempPlayerPtr = activePlayer;
        activePlayer = otherPlayer;
        otherPlayer = tempPlayerPtr;
    }
}





//    public Board getActiveBoard() {
//        return activePlayer.getMyBoard();
//    }
//
//    public Board getHiddenBoard() throws Exception {
//        return otherPlayer.getBoard().HideAllHidables();
//    }

//        if (attackResult != eAttackResult.CELL_ALREADY_ATTACKED){
//            if (attackResult == eAttackResult.HIT_MINE){
//                // TODO check what happens if the active players cell was already hit
//                activePlayer.getMyBoard().getBoardCellAtCoordinates(position).attack();
//            }
//            // TODO fix - if the player hit a ship he gets another turn
//            swapPlayers();
//        }
