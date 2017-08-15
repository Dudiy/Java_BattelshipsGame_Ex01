package GameLogic.Game.Board;

import GameLogic.Game.GameObjects.GameObject;
import GameLogic.Game.GameObjects.Water;

public class BoardCell {
    private BoardCoordinates position;
    private GameObject cellValue = new Water();

    public BoardCell(char col, int row) {
        position = new BoardCoordinates(col,row);
    }

    public BoardCell(BoardCoordinates position) {
        this.position = position;
    }

    public void SetCellValue(GameObject cellValue) throws Exception {
        // TODO check surrounding cell
        if (this.cellValue == null || this.cellValue instanceof Water) {
            this.cellValue = cellValue;
        }
        else{
            String objectTypeInCell =  this.cellValue.getClass().getSimpleName();
            throw new Exception("Cannot place a game object, cell is already occupied by a " + objectTypeInCell + " object");
        }
    }

    public BoardCoordinates GetPosition() {
        return position;
    }

    public GameObject GetCellValue() {
        return cellValue;
    }
}
