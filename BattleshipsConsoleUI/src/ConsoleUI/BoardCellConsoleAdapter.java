package ConsoleUI;

import GameLogic.Game.Board.BoardCell;
import GameLogic.Game.GameObjects.GameObject;
import GameLogic.Game.GameObjects.Mine;
import GameLogic.Game.GameObjects.Ship.AbstractShip;
import GameLogic.Game.GameObjects.Water;

public class BoardCellConsoleAdapter {
    // cell symbol
    private final String HIT = "X";
    private final String MISS = "º";
    private final String SHIP = "█";
    private final String WATER = "~";
    private final String MINE = "░";
    private BoardCell boardCell;

    public BoardCellConsoleAdapter(BoardCell boardCell) {
        this.boardCell = boardCell;
    }

    @Override
    public String toString() {
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
                cellChar = SHIP;
            } else if (cellValue instanceof Water) {
                cellChar = WATER;
            } else if (cellValue instanceof Mine) {
                cellChar = MINE;
            } else {
                cellChar = "?";
            }
        }

        return cellChar;
    }
}
