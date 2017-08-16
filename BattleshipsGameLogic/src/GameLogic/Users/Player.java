package GameLogic.Users;

import GameLogic.Game.Board.Board;

import java.sql.Time;

public class Player {
    private String ID;
    private String name;
    private Board board;
    private Time avgTurnDurationnew;
    private int score = 0;

    public Player(String playerID, String name) {
        this.ID = playerID;
        this.name = name;
    }

    // ======================================= setters =======================================
    public void setBoard(Board board) {
        this.board = board;
    }

    // ======================================= getters =======================================
    public String getID() {
        return ID;
    }

    public Object getName() {
        return name;
    }

    public Board getBoard() {
        return board;
    }
}
