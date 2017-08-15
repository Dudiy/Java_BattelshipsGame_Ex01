package ConsoleUI;

import GameLogic.Game.Board.Board;
import GameLogic.Game.Board.BoardCell;
import com.sun.xml.internal.ws.util.StringUtils;

// TODO static ?
public class BoardDisplayer {
    // border symbol
    private final String BOARD_VERTICAL = "\u2551";
    private final String BOARD_LEFT_UP_CORNER = "\u2554";
    private final String BOARD_RIGHT_UP_CORNER = "\u2557";
    private final String BOARD_LEFT_DOWN_CORNER = "\u255A";
    private final String BOARD_RIGHT_DOWN_CORNER = "\u255D";
    // cell symbol
    private final String HIT = "1";
    private final String MISS = "2";
    private final String SHIP = "3";
    private final String WATER = "4";
    private final String MINE = "5";

    public void printBoard(Board board) {
        printFirstRowBorder();
        for(BoardCell[] row : board.getBoard()){
            printRow(row);
        }
        printLastRowBorder();
    }

    private void printFirstRowBorder() {

    }

    private void printRow(BoardCell[] row) {

    }

    private void printLastRowBorder() {

    }


}

//        int totalRow = board.getBoardSize();
//        printFirstRowOfBoard(board.getBoardRow(0));
//        for (int numRow = 1; numRow <= totalRow - 2; numRow++) {
//            printInsideRowOfBoard(board.getBoardRow(numRow));
//        }
//        printLastRowOfBoard(board.getBoardRow(totalRow-1));

//    private void printFirstRowOfBoard(BoardCell[] row) {
//    }
//
//    private void printInsideRowOfBoard(BoardCell[] row) {
//    }
//
//    private void printLastRowOfBoard(BoardCell[] row) {
//    }
