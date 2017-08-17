package GameLogic.Users;

import GameLogic.Game.Board.Board;

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

    public Object getName() {
        return name;
    }

    public Board getMyBoard() {
        return myBoard;
    }

    public Board getOpponentBoard() {
        return opponentBoard;
    }
}
