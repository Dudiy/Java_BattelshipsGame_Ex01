package ConsoleUI;

import GameLogic.Game.Board.BoardCell;
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
        String cellChar;

        if (boardCell.isHit()) {
            cellChar = HIT;
        } else if (boardCell.isMiss()) {
            cellChar = MISS;
        }
        // user didn't attack the given cell yet
        if (boardCell.GetCellValue() instanceof AbstractShip) {
            cellChar = SHIP;
        } else if (boardCell.GetCellValue() instanceof Water) {
            cellChar = WATER;
        } else if (boardCell.GetCellValue() instanceof Mine) {
            cellChar = MINE;
        } else{
            cellChar = "?";
        }

        return cellChar;
    }
}
