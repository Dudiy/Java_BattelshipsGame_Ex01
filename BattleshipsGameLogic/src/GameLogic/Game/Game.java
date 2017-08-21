package GameLogic.Game;

import GameLogic.Exceptions.CellNotOnBoardException;
import GameLogic.Exceptions.InvalidGameObjectPlacementException;
import GameLogic.Exceptions.NoMinesAvailableException;
import GameLogic.Game.Board.Board;
import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.GameObjects.Ship.*;
import GameLogic.Game.GameObjects.Water;
import GameLogic.Users.*;
import jaxb.generated.BattleShipGame;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Game implements Serializable{
    private static int IDGenerator = 1000;
    private int ID;
    private int activePlayerIndex;
    private int winnerPlayerIndex = -1;
    private int movesCounter = 0;
    private boolean gameIsSet = false;
    private Player[] players = new Player[2];
    private Instant gameStartTime;
    private ShipFactory shipFactory;
    private eGameState gameState = eGameState.INVALID;
    private GameSettings gameSettings;
    //private Map<String, User> spectators = new HashMap<>();

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

    public Player[] getPlayers() {
        return players;
    }

    public Player getActivePlayer() {
        return players[activePlayerIndex];
    }

    public int getMovesCounter() {
        return movesCounter;
    }

    public Player getOtherPlayer() {
        return players[(activePlayerIndex + 1) % 2];
    }

    public Player getWinnerPlayer() {
        return players[winnerPlayerIndex];
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
        gameStartTime = Instant.now();
    }

    public Duration getTotalGameDuration() {
        return Duration.between(gameStartTime, Instant.now());
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
        Map<String, Integer> shipTypesAmount = gameSettings.getShipTypesAmount();
        currentBoard.setMinesAvailable(gameSettings.getMinesPerPlayer());

        for (BattleShipGame.Boards.Board.Ship ship : board.getShip()) {
            try {
                AbstractShip shipObject = shipFactory.createShip(ship);
                currentBoard.addShipToBoard(shipObject);
                int currShipTypeAmount = shipTypesAmount.get(ship.getShipTypeId()) - 1;
                shipTypesAmount.put(ship.getShipTypeId(), currShipTypeAmount);
            } catch (InvalidGameObjectPlacementException e) {
                throw e;
            } catch (Exception e) {
                String message = String.format("Error while initializing " + currentPlayer.getName() + "'s board, inner exception: " + e.getMessage());
                throw new Exception(message);
            }
        }

        if (!allRequiredShipsAdded(shipTypesAmount)) {
            throw new InputMismatchException("Error: not all the required ship was added.");
        }

        return currentBoard;
    }

    private boolean allRequiredShipsAdded(Map<String, Integer> shipTypes) {
        Boolean allShipsAdded = true;

        for (Map.Entry<String, Integer> shipType : shipTypes.entrySet()) {
            if (shipType.getValue() != 0) {
                allShipsAdded = false;
                break;
            }
        }

        return allShipsAdded;
    }


    public eAttackResult attack(BoardCoordinates position) throws CellNotOnBoardException {
        eAttackResult attackResult = getActivePlayer().attack(position);

        if (attackResult == eAttackResult.HIT_MINE &&
            !(getActivePlayer().getMyBoard().getBoardCellAtCoordinates(position).getCellValue() instanceof Water)){
            getOtherPlayer().incrementScore();
        }
        // TODO check when we need to increment the move counter? every time players swap or every attack?
//        if (attackResult != eAttackResult.CELL_ALREADY_ATTACKED) {
//        }

        if (attackResult.moveEnded()) {
            swapPlayers();
            movesCounter++;
        }

        return attackResult;
    }

    private void swapPlayers() {
        activePlayerIndex = (activePlayerIndex + 1) % 2;
    }


    public void plantMineOnActivePlayersBoard(BoardCoordinates cell) throws CellNotOnBoardException, InvalidGameObjectPlacementException, NoMinesAvailableException {
        getActivePlayer().plantMine(cell);
        swapPlayers();
    }

    public void endGame() {
        // the player who left the game lose
        swapPlayers();
        winnerPlayerIndex = activePlayerIndex;
        gameState = eGameState.PLAYER_QUIT;
    }

    public static void saveToFile(Game game, final String fileName) throws Exception {
        try (ObjectOutputStream outputStream =
                     new ObjectOutputStream(
                             new FileOutputStream(fileName))) {
            outputStream.writeObject(game);
            outputStream.flush();
        }
    }
    public static Game loadFromFile(final String fileName) throws Exception {
        Game game = null;
        try (ObjectInputStream inStream =
                     new ObjectInputStream(
                             new FileInputStream(fileName))) {
            game = (Game)inStream.readObject();
        }
        return game;
    }
}