package GameLogic.Users;

import GameLogic.Game.Board.Board;

import java.sql.Time;

public class Player {
    private String playerID;
    private String playerName;
    private Board myBoard;
    private Board guessesBoard;
    private Time avgTurnDurationnew;
    private int score = 0;

    public Player(String playerID, String playerName) {
        this.playerID = playerID;
        this.playerName = playerName;
    }

    public void setMyBoard(Board myBoard) {
        this.myBoard = myBoard;
        guessesBoard = new Board(myBoard.getBoardSize());
    }

    public Board getMyBoard() {
        return myBoard;
    }
}
