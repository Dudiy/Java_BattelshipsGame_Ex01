package GameLogic.Users;

import GameLogic.Exceptions.CellNotOnBoardException;
import GameLogic.Exceptions.InvalidGameObjectPlacementException;
import GameLogic.Exceptions.NoMinesAvailableException;
import GameLogic.Game.Board.Board;
import GameLogic.Game.Board.BoardCell;
import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.GameObjects.Mine;
import GameLogic.Game.GameObjects.Water;
import GameLogic.Game.eAttackResult;

import java.io.Serializable;
import java.time.Duration;

public class Player implements Serializable {
    private String ID;
    private String name;
    private Board myBoard;
    private Board opponentBoard;
    private int score = 0;
    private int timesMissed = 0;
    // duration of a turn is from the time the user selects make move until he enters the cell to attack
    private Duration totalTurnsDuration = Duration.ZERO;
    private int numTurnsPlayed = 0;

    public Player(String playerID, String name) {
        this.ID = playerID;
        this.name = name;
    }

    // ======================================= setters =======================================

    public void setMyBoard(Board board) {
        this.myBoard = board;
    }

    public void setOpponentBoard(Board board) {
        this.opponentBoard = board;
    }

    // ======================================= getters =======================================

    public String getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public Board getMyBoard() {
        return myBoard;
    }

    public Board getOpponentBoard() {
        return opponentBoard;
    }

    public int getScore() {
        return score;
    }

    public int getTimesMissed() {
        return timesMissed;
    }

    public void addTurnDurationToTotal(Duration turnDuration) {
        totalTurnsDuration = totalTurnsDuration.plus(turnDuration);
    }

    public Duration getAvgTurnDuration() {
        return numTurnsPlayed == 0 ? Duration.ZERO : totalTurnsDuration.dividedBy(numTurnsPlayed);
    }

    // ======================================= Methods =======================================
    public eAttackResult attack(BoardCoordinates position) throws CellNotOnBoardException {
        eAttackResult attackResult = opponentBoard.attack(position);

        if (attackResult == eAttackResult.HIT_WATER) {
            timesMissed++;
        }

        if (attackResult.isScoreIncrementer()) {
            incrementScore();
        }

        if (attackResult == eAttackResult.HIT_MINE) {
            // if I hit a mine, attack my own board
//            Mine mineThatWasHit = (Mine)opponentBoard.getBoardCellAtCoordinates(position).getCellValue();
//            mineThatWasHit.setExplosionResult(myBoard.attack(position));
            myBoard.attack(position);
//        // TODO implement in EX02
//            if (mineExplosionResult == eAttackResult.HIT_MINE) {
//                BoardCell cellHit = opponentBoard.getBoardCellAtCoordinates(position);
//                cellHit.removeGameObjectFromCell();
//                // TODO verify we don't need to attack again to make is a "MISS"
//            } else if (mineExplosionResult == eAttackResult.CELL_ALREADY_ATTACKED){
//                // ....
//            }
//            else{
//                // ...
//            }
        }

        if (attackResult != eAttackResult.CELL_ALREADY_ATTACKED) {
            numTurnsPlayed++;
        }

        return attackResult;
    }

    public void incrementScore() {
        score++;
    }

    public void plantMine(BoardCoordinates position) throws CellNotOnBoardException, InvalidGameObjectPlacementException, NoMinesAvailableException {
        BoardCell cellToPlantMine = myBoard.getBoardCellAtCoordinates(position);

        if (myBoard.getMinesAvailable() == 0) {
            throw new NoMinesAvailableException();
        }

        if (cellToPlantMine.wasAttacked()){
            throw new InvalidGameObjectPlacementException(Mine.getObjectTypeSimpleName(), position, "Cannot place mine on a cell that was attacked");
        }

        if (myBoard.allSurroundingCellsClear(cellToPlantMine, null)) {
            cellToPlantMine.SetCellValue(new Mine(position));
            myBoard.minePlanted();
        } else {
            throw new InvalidGameObjectPlacementException(Mine.class.getSimpleName(), position, "All surrounding cells must be clear.");
        }
    }
}
