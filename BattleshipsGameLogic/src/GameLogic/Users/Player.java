package GameLogic.Users;

import GameLogic.Game.Board.Board;
import GameLogic.Game.Board.BoardCoordinates;

import java.sql.Time;

public class Player {
    private String ID;
    private String name;
    private Board myBoard;
    private Board guessesBoard;
    private Time avgTurnDurationnew;
    private int score = 0;

    public Player(String playerID, String name) {
        this.ID = playerID;
        this.name = name;
    }

    // ======================================= setters =======================================
    public void setMyBoard(Board myBoard) {
        this.myBoard = myBoard;
        guessesBoard = new Board(myBoard.getBoardSize());
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

    public eShootReturn shootCell(BoardCoordinates cellToShoot){
        // TODO
        return null;
    }
}
