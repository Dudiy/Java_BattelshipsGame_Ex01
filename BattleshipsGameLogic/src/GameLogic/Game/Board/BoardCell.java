package GameLogic.Game.Board;

import java.io.Serializable;
import GameLogic.Exceptions.InvalidGameObjectPlacementException;
import GameLogic.Game.GameObjects.GameObject;
import GameLogic.Game.GameObjects.Water;
import GameLogic.Game.eAttackResult;

public class BoardCell implements Serializable {
    private BoardCoordinates position;
    private GameObject cellValue;
    private boolean wasAttacked = false;

    public BoardCell(char col, int row) {
        position = new BoardCoordinates(col, row);
        cellValue = new Water(position);
    }

    // ======================================= setters =======================================
    public void SetCellValue(GameObject cellValue) throws InvalidGameObjectPlacementException {
        if (this.cellValue == null || this.cellValue instanceof Water) {
            this.cellValue = cellValue;
        } else {
            String objectTypeInCell = this.cellValue.getClass().getSimpleName();
            throw new InvalidGameObjectPlacementException(cellValue.getClass().getSimpleName(), position,
                    "Cannot place a game object, cell is already occupied by a " + objectTypeInCell + " object");
        }
    }

    // ======================================= getters =======================================
    public BoardCoordinates getPosition() {
        return position;
    }

    public GameObject getCellValue() {
        return cellValue;
    }

    public boolean wasAttacked() {
        return wasAttacked;
    }

    // ======================================= methods =======================================
    // sets the cell value to be water
    public void removeGameObjectFromCell() {
        if (!(cellValue instanceof Water)) {
            this.cellValue = new Water(this.cellValue.getPosition());
        }
    }

    // TODO board attack
    public eAttackResult attack() {
        eAttackResult attackResult;
        if (!wasAttacked) {
            attackResult = cellValue.attack();
            wasAttacked = true;
        } else {
            attackResult = eAttackResult.CELL_ALREADY_ATTACKED;
        }
        return attackResult;
    }
}