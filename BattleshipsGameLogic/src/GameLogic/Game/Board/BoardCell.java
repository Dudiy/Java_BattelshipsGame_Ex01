package GameLogic.Game.Board;

import GameLogic.Game.GameObjects.GameObject;
import GameLogic.Game.GameObjects.Ship.AbstractShip;
import GameLogic.Game.GameObjects.Water;
import GameLogic.Users.eAttackResult;

public class BoardCell {
    private BoardCoordinates position;
    private GameObject cellValue;
    private boolean hit = false;

    public BoardCell(char col, int row) {
        position = new BoardCoordinates(col, row);
        cellValue = new Water(position);
    }

    public BoardCell(BoardCoordinates position) {
        this.position = position;
    }

    // ======================================= setters =======================================
    public void SetCellValue(GameObject cellValue) throws Exception {
        // TODO check surrounding cell
        if (this.cellValue == null || this.cellValue instanceof Water) {
            this.cellValue = cellValue;
        } else {
            String objectTypeInCell = this.cellValue.getClass().getSimpleName();
            throw new Exception("Cannot place a game object, cell is already occupied by a " + objectTypeInCell + " object");
        }
    }

    // ======================================= getters =======================================
    public BoardCoordinates GetPosition() {
        return position;
    }

    public GameObject GetCellValue() {
        return cellValue;
    }

    public boolean wasAttacked() {
        return hit;
    }

    // ======================================= methods =======================================
    public eAttackResult attack() {
        eAttackResult attackResult;
        if (!hit) {
            attackResult = cellValue.Attack();
        } else {
            attackResult = eAttackResult.CELL_ALREADY_ATTACKED;
        }

        return attackResult;
    }
}


/*    private void checkSurroundingCells() {
        // ships are only added at the beginning of the game when there are no other objects so we only need to check for ships in surrounding cells
        BoardCoordinates positionToCheck = this.GetPosition();
        positionToCheck.OffsetRow(-1);
    }*/
