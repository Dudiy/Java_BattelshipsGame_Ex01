package ConsoleUI;

import GameLogic.Game.Board.Board;
import GameLogic.Game.Board.BoardCell;
import GameLogic.Game.GameObjects.Mine;
import GameLogic.Game.GameObjects.Ship.AbstractShip;
import GameLogic.Game.GameObjects.Water;
import com.sun.xml.internal.ws.util.StringUtils;

// TODO static ?
public class BoardDisplayer {
    // border symbol
    final String BOARD_HORIZONTAL = "\u2550";
    private final String BOARD_VERTICAL = "\u2551";
    private final String BOARD_LEFT_UP_CORNER = "\u2554";
    private final String BOARD_RIGHT_UP_CORNER = "\u2557";
    private final String BOARD_LEFT_DOWN_CORNER = "\u255A";
    private final String BOARD_RIGHT_DOWN_CORNER = "\u255D";
    // special frame
    private final String BOARD_HORIZONTAL_FIRST_LINE = "\u2566";
    private final String BOARD_HORIZONTAL_LAST_LINE = "\u2569";
    private final String BOARD_LEFT_VERTICAL_ROW_SAPERATOR = "\u2560";
    private final String BOARD_RIGHT_VERTICAL_ROW_SAPERATOR = "\u2563";
    private final String BOARD_PLUS_ROW_SAPERATOR = "\u256C";

    // cell symbol
    private final Character HIT = '1';
    private final Character MISS = '2';
    private final Character SHIP = '3';
    private final Character WATER = '4';
    private final Character MINE = '5';

    private Board board;

    public void printBoard(Board board) {
        int currRowNum = 0;
        this.board = board;
        printFirstRowBorder();
        for (BoardCell[] currRow : board.getBoard()) {
            printRow(currRow);
            currRowNum++;
            if(currRowNum != board.getBoardSize()){
                printSaperatorRow();
            }
        }
        printLastRowBorder();
    }

    private void printFirstRowBorder() {
        System.out.print(BOARD_LEFT_UP_CORNER);
        for (int i = 0; i < board.getBoardSize() - 1; i++) {
            System.out.print(BOARD_HORIZONTAL);
            System.out.print(BOARD_HORIZONTAL_FIRST_LINE);
        }
        System.out.print(BOARD_HORIZONTAL);
        System.out.println(BOARD_RIGHT_UP_CORNER);
    }

    private void printRow(BoardCell[] row) {
        for (BoardCell cell : row) {
            System.out.print(BOARD_VERTICAL);
            System.out.print(getCellChar(cell));
        }
        System.out.println(BOARD_VERTICAL);
    }

    private void printSaperatorRow() {
        System.out.print(BOARD_LEFT_VERTICAL_ROW_SAPERATOR);
        for (int i = 0; i < board.getBoardSize() - 1; i++) {
            System.out.print(BOARD_HORIZONTAL);
            System.out.print(BOARD_PLUS_ROW_SAPERATOR);
        }
        System.out.print(BOARD_HORIZONTAL);
        System.out.println(BOARD_RIGHT_VERTICAL_ROW_SAPERATOR);
    }

    private void printLastRowBorder() {
        System.out.print(BOARD_LEFT_DOWN_CORNER);
        for (int i = 0; i < board.getBoardSize() - 1; i++) {
            System.out.print(BOARD_HORIZONTAL);
            System.out.print(BOARD_HORIZONTAL_LAST_LINE);
        }
        System.out.print(BOARD_HORIZONTAL);
        System.out.println(BOARD_RIGHT_DOWN_CORNER);
    }

    private Character getCellChar(BoardCell boardCell){
        Character cellChar = null;
        if(boardCell.isHit()){
            cellChar = HIT;
        }
        else if(boardCell.isMiss()){
            cellChar = MISS;
        }
        // user didn't choose that cell yet
        if(boardCell.GetCellValue() instanceof AbstractShip){
            cellChar = SHIP;
        }
        else if(boardCell.GetCellValue() instanceof Water){
            cellChar = WATER;
        }
        else if(boardCell.GetCellValue() instanceof Mine){
            cellChar = MINE;
        }
        return cellChar;
    }
}
