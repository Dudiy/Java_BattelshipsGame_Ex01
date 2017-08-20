package GameLogic.Game.Board;

import GameLogic.Exceptions.InvalidGameObjectPlacementException;
import GameLogic.Game.GameObjects.GameObject;
import GameLogic.Game.GameObjects.Water;
import GameLogic.Game.eAttackResult;

public class BoardCell {
    private BoardCoordinates position;
    private GameObject cellValue;
    private boolean wasAttacked = false;

    public BoardCell(char col, int row) {
        position = new BoardCoordinates(col, row);
        cellValue = new Water(position);
    }

    public BoardCell(BoardCoordinates position) {
        this.position = position;
    }

    // ======================================= setters =======================================
    public void SetCellValue(GameObject cellValue) throws InvalidGameObjectPlacementException {
        // TODO check surrounding cell
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

    public GameObject GetCellValue() {
        return cellValue;
    }

    public boolean wasAttacked() {
        return wasAttacked;
    }

    // ======================================= methods =======================================
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


/*    private void checkSurroundingCells() {
        // ships are only added at the beginning of the game when there are no other objects so we only need to check for ships in surrounding cells
        BoardCoordinates positionToCheck = this.getPosition();
        positionToCheck.OffsetRow(-1);
    }*/
