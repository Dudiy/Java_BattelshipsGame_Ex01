package GameLogic.Users;

import GameLogic.Exceptions.CellNotOnBoardException;
import GameLogic.Game.Board.Board;
import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.GameObjects.Ship.AbstractShip;
import GameLogic.Game.eAttackResult;

import java.sql.Time;

public class Player {
    private String ID;
    private String name;
    private Board myBoard;
    private Board opponentBoard;
    private Time avgTurnDurationNew;
    private int score = 0;

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

    // ======================================= Methods =======================================
    public eAttackResult attack(BoardCoordinates position) throws CellNotOnBoardException {
        eAttackResult attackResult = opponentBoard.attack(position);

        if (attackResult.isScoreIncrementer()){
            score++;
        }

        if (attackResult == eAttackResult.HIT_MINE){
            // if hit a mine attack my own board
            myBoard.attack(position);
        }

        return attackResult;
    }
}
