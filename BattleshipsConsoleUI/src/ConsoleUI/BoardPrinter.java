package ConsoleUI;

import GameLogic.Game.Board.BoardCell;
import GameLogic.Game.GameObjects.GameObject;
import GameLogic.Game.GameObjects.Mine;
import GameLogic.Game.GameObjects.Ship.AbstractShip;
import GameLogic.Game.GameObjects.Water;
import GameLogic.Game.eAttackResult;
import GameLogic.Users.Player;

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
    private final String PADDING_FROM_LEFT = "    ";
    // cell char values
    private final String HIT = "X";
    private final String MISS = "º";
    private final String SHIP = "█";
    private final String WATER = "~";
    private final String MINE = "Ω";
    // other variables
    public static final boolean PRINT_SINGLE_BOARD = true;
    private final String BOARDS_SEPARATOR = "          |          ";
    private byte currRowIndex;
    private boolean hideNonVisible;
    private int boardSize;
    private int numBoardsToPrint;

    public void printBoards(Player activePlayer, boolean printSingleBoard) {
        this.boardSize = activePlayer.getMyBoard().getBoardSize();
        numBoardsToPrint = printSingleBoard ? 1 : 2;
        currRowIndex = startRowIndex;

        if (printSingleBoard) {
            System.out.println("Current board state:\n");
        } else {
            printBoardTitles();
        }

        printColIndices();
        printFirstRowBorder();
        //print the body of the board
        for (int i = 0; i < boardSize; i++) {
            printRow(activePlayer.getMyBoard().getBoard()[i],
                    activePlayer.getOpponentBoard().getBoard()[i]);
            if (i < boardSize - 1) {
                printSeparatorRow();
            }

            currRowIndex++;
        }
        printLastRowBorder();
    }

    private void printBoardTitles() {
        String myBoardStr = "My Board";
        String opponentsBoardStr = "opponents's board";
        int numOfSpacesAfterMyBoardStr = (boardSize * 2) + 2 - myBoardStr.length();

        System.out.println("Current boards state:\n");
        System.out.print(PADDING_FROM_LEFT);
        System.out.print(myBoardStr);
        for (int i = 0; i < numOfSpacesAfterMyBoardStr; i++) {
            System.out.print(" ");
        }
        System.out.print(BOARDS_SEPARATOR);
        System.out.println(opponentsBoardStr);
        System.out.println();
    }

    private void printColIndices() {
        char localColIndex;

        System.out.print(PADDING_FROM_LEFT);
        for (int i = 0; i < numBoardsToPrint; i++) {
            System.out.print("\\ ");
            localColIndex = startColIndex;
            for (int j = 0; j < boardSize; j++) {
                System.out.print(localColIndex + " ");
                localColIndex++;
            }
            if (i == 0 && numBoardsToPrint == 2) {
                System.out.print(BOARDS_SEPARATOR);
            }
        }
        System.out.println();
    }

    private void printRow(BoardCell[] myRow, BoardCell[] opponentsRow) {
        System.out.print(PADDING_FROM_LEFT);
        BoardCell[] currRow = myRow;
        for (int i = 0; i < numBoardsToPrint; i++) {
            hideNonVisible = i != 0;
            // add another space in case there are more than 9 rows
            if (currRowIndex >= 10) {
                System.out.print('\b');
            }
            System.out.print(currRowIndex);

            for (BoardCell cell : currRow) {
                System.out.print(BOARD_VERTICAL);
                System.out.print(getCellChar(cell));
            }
            System.out.print(BOARD_VERTICAL);

            if (i == 0 && numBoardsToPrint == 2) {
                System.out.print(BOARDS_SEPARATOR);
                currRow = opponentsRow;
            }
        }
        System.out.println();
    }

    private void printBorderRow(char firstCell, char connectionCell, char lastCell) {
        System.out.print(PADDING_FROM_LEFT);
        for (int i = 0; i < numBoardsToPrint; i++) {
            System.out.print(" " + firstCell);
            for (int j = 0; j < boardSize - 1; j++) {
                System.out.print(BOARD_HORIZONTAL);
                System.out.print(connectionCell);
            }
            System.out.print(BOARD_HORIZONTAL);
            System.out.print(lastCell);

            if (i == 0 && numBoardsToPrint == 2) {
                System.out.print(BOARDS_SEPARATOR);
            }
        }
        System.out.println();
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

    private String getCellChar(BoardCell boardCell) {
        GameObject cellValue = boardCell.getCellValue();
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
//                eAttackResult mineExplosionResult = ((Mine) cellValue).getExplosionResult();
//                if (mineExplosionResult == eAttackResult.HIT_WATER) {
//                    cellChar = MISS;
//                } else if (mineExplosionResult == )
//                    cellChar = HIT;
    }
}
