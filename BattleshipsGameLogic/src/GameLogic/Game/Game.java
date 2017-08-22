package GameLogic.Game;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import jaxb.generated.BattleShipGame;
import GameLogic.Users.*;
import GameLogic.Exceptions.*;
import GameLogic.Game.Board.Board;
import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.GameObjects.Ship.*;
import GameLogic.Game.GameObjects.Water;

public class Game implements Serializable {
    private static int IDGenerator = 1000;
    private int ID;
    private int activePlayerIndex;
    private int movesCounter = 0;
    private boolean gameIsSet = false;
    private Player winner;
    private Player[] players = new Player[2];
    private Instant gameStartTime;
    private ShipFactory shipFactory;
    private eGameState gameState = eGameState.INVALID;
    private GameSettings gameSettings;

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
        return winner;
    }

    public eGameState getGameState() {
        return gameState;
    }

    public Duration getTotalGameDuration() {
        return Duration.between(gameStartTime, Instant.now());
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
            throw new InputMismatchException("Error: amount of ships added to " + currentPlayer.getName() + "'s board is invalid.");
        }

        return currentBoard;
    }

    private boolean allRequiredShipsAdded(Map<String, Integer> shipTypes) {
        Boolean allShipsWereAdded = true;

        for (Map.Entry<String, Integer> shipType : shipTypes.entrySet()) {
            if (shipType.getValue() != 0) {
                allShipsWereAdded = false;
                break;
            }
        }

        return allShipsWereAdded;
    }

    public eAttackResult attack(BoardCoordinates position) throws CellNotOnBoardException {
        eAttackResult attackResult = getActivePlayer().attack(position);

        //if a mine was attacked which hit my own ship or mine, increment the other player's score
        // TODO if a mine hits another mine do we increment the score?
        if (attackResult == eAttackResult.HIT_MINE &&
                !(getActivePlayer().getMyBoard().getBoardCellAtCoordinates(position).getCellValue() instanceof Water)) {
            getOtherPlayer().incrementScore();
        }
        // TODO check when we need to increment the move counter? every time players swap or every attack?
//        if (attackResult != eAttackResult.CELL_ALREADY_ATTACKED) {
//        }

        if (attackResult == eAttackResult.HIT_AND_SUNK_SHIP) {
            movesCounter++;
            if (activePlayerSunkAllShips()) {
                activePlayerWon();
            }
        } else if (attackResult.moveEnded()) {
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

    public boolean activePlayerSunkAllShips() {
        return getActivePlayer().getOpponentBoard().allShipsWereSunk();
    }

    public void activePlayerForfeit(){
        winner = getOtherPlayer();
        gameState = eGameState.PLAYER_QUIT;
    }

    public void activePlayerWon(){
        winner = getActivePlayer();
        gameState = eGameState.PLAYER_WON;
    }

    public static void saveToFile(Game game, final String fileName) throws Exception {
        File file = new File(fileName);

        if (!file.getParentFile().exists()) {
            if (!file.getParentFile().mkdirs()) {
                throw new Exception("\"Saved Games\" directory was not found and could not be created");
            }
        }

        try (ObjectOutputStream outputStream =
                     new ObjectOutputStream(
                             new FileOutputStream(fileName))) {
            // TODO are the players saved? only thir reference is here
            outputStream.writeObject(game);
            outputStream.flush();
        }
    }

    public static Game loadFromFile(final String fileName) throws Exception {
        Game game;

        try (ObjectInputStream inStream =
                     new ObjectInputStream(
                             new FileInputStream(fileName))) {
            game = (Game) inStream.readObject();
        }

        return game;
    }
}