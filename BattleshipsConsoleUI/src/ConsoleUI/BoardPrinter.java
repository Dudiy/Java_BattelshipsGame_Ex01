package ConsoleUI;

import GameLogic.Game.Board.Board;
import GameLogic.Game.Board.BoardCell;
import GameLogic.Game.Game;
import GameLogic.Game.GameObjects.GameObject;
import GameLogic.Game.GameObjects.Mine;
import GameLogic.Game.GameObjects.Ship.AbstractShip;
import GameLogic.Game.GameObjects.Water;

public class BoardPrinter {
    private static final boolean HIDE_NON_VISIBLE = true;
    // border symbol
    private final char BOARD_HORIZONTAL = '═';
    private final char BOARD_VERTICAL = '║';
    private final char BOARD_TOP_LEFT_CORNER = '╔';
    private final char BOARD_RIGHT_UP_CORNER = '╗';
    private final char BOARD_BOTTOM_LEFT_CORNER = '╚';
    private final char BOARD_BOTTOM_RIGHT_CORNER = '╝';
    // special frame
    private final char BOARD_HORIZONTAL_FIRST_LINE = '╦';
    private final char BOARD_HORIZONTAL_LAST_LINE = '╩';
    private final char BOARD_LEFT_VERTICAL_ROW_SEPARATOR = '╠';
    private final char BOARD_RIGHT_VERTICAL_ROW_SEPARATOR = '╣';
    private final char BOARD_PLUS_ROW_SEPARATOR = '╬';
    // indices
    private final char startColIndex = 'A';
    private final byte startRowIndex = 1;
    private final String PADDING_FROM_LEFT = "              ";
    // cell char values
    private final String HIT = "X";
    private final String MISS = "º";
    private final String SHIP = "█";
    private final String WATER = "~";
    private final String MINE = "░";

    private byte currRowIndex;
    private Board board;
    private boolean hideNonVisible;


    public void printBothBoards(Game game) {
        try {
            System.out.println("************** " + game.getActivePlayer().getName() + "'s Board (active player) **************\n");
            printBoard(game.getActivePlayer().getMyBoard(), !HIDE_NON_VISIBLE);
            System.out.println("\n************** " + game.getOtherPlayer().getName() + "'s Board (opponent) **************\n");
            printBoard(game.getActivePlayer().getOpponentBoard(), HIDE_NON_VISIBLE);
        } catch (Exception e) {
            System.out.println("Error while trying to print boards: " + e.getMessage());
        }
    }

    // TODO changed to private, if we need we can change it back to public
    private void printBoard(Board board, boolean hideNonVisible) {
        this.hideNonVisible = hideNonVisible;
        int currRowNum = 0;
        currRowIndex = startRowIndex;
        this.board = board;
        printColIndices();
        printFirstRowBorder();
        for (BoardCell[] currRow : board.getBoard()) {
            printRow(currRow);
            currRowNum++;
            if (currRowNum != board.getBoardSize()) {
                printSeparatorRow();
            }
        }

        printLastRowBorder();
    }

    private void printColIndices() {
        char localColIndex = startColIndex;
        int boardSize = board.getBoardSize();

        System.out.print(PADDING_FROM_LEFT + "\\ ");
        for (int i = 0; i < boardSize; i++) {
            System.out.print(localColIndex + " ");
            localColIndex++;
        }

        System.out.println();
    }

    private void printRow(BoardCell[] row) {
        System.out.print(PADDING_FROM_LEFT);
        System.out.print(currRowIndex++);
        for (BoardCell cell : row) {
            System.out.print(BOARD_VERTICAL);
            System.out.print(getCellChar(cell));
        }
        System.out.println(BOARD_VERTICAL);
    }

    private void printBorderRow(char firstCell, char connectionCell, char lastCell) {
        int boardSize = board.getBoardSize();

        System.out.print(PADDING_FROM_LEFT + " ");
        System.out.print(firstCell);
        for (int i = 0; i < boardSize - 1; i++) {
            System.out.print(BOARD_HORIZONTAL);
            System.out.print(connectionCell);
        }

        System.out.print(BOARD_HORIZONTAL);
        System.out.println(lastCell);
    }

    private void printFirstRowBorder() {
        printBorderRow(BOARD_TOP_LEFT_CORNER, BOARD_HORIZONTAL_FIRST_LINE, BOARD_RIGHT_UP_CORNER);
    }

    private void printSeparatorRow() {
        printBorderRow(BOARD_LEFT_VERTICAL_ROW_SEPARATOR, BOARD_PLUS_ROW_SEPARATOR, BOARD_RIGHT_VERTICAL_ROW_SEPARATOR);
    }

    private void printLastRowBorder() {
        printBorderRow(BOARD_BOTTOM_LEFT_CORNER, BOARD_HORIZONTAL_LAST_LINE, BOARD_BOTTOM_RIGHT_CORNER);
    }

    private String getCellChar(BoardCell boardCell){
        GameObject cellValue = boardCell.GetCellValue();
        String cellChar;

        if (boardCell.wasAttacked()) {
            if (cellValue instanceof Water) {
                cellChar = MISS;
            } else if (cellValue instanceof Mine || cellValue instanceof AbstractShip) {
                cellChar = HIT;
            } else {
                cellChar = "¿";
            }
        } else {
            if (cellValue instanceof AbstractShip) {
                cellChar = hideNonVisible ? WATER : SHIP;
            } else if (cellValue instanceof Water) {
                cellChar = WATER;
            } else if (cellValue instanceof Mine) {
                cellChar = hideNonVisible ? WATER : MINE;
            } else {
                cellChar = "?";
            }
        }

        return cellChar;
    }
}
