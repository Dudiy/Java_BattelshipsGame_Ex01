package GameLogic.Game;

public enum eAttackResult {
    // information inside logic
    HIT_SHIP("Hit a ship"),
    HIT_AND_SUNK_SHIP("Sunk a ship"),
    CELL_ALREADY_ATTACKED("Cell already attacked"),
    HIT_MINE("Hit a mine"),
    HIT_WATER("Missed");
    // information outside logic
//    GET_ANOTHER_MOVE(
//            HIT_SHIP.ID +
//                    HIT_AND_SUNK_SHIP.ID +
//                    CELL_ALREADY_ATTACKED.ID),
//    MOVE_ENDED(
//            HIT_MINE.ID +
//                    HIT_WATER.ID
//    );

    private String description;

    eAttackResult(String description) {
        this.description = description;
    }

//    public Boolean contain(eAttackResult attackResult) {
//        return ((this.ID & attackResult.ID) != 0);
//    }

    public boolean moveEnded() {
        return this == HIT_MINE || this == HIT_WATER;
    }

    @Override
    public String toString() {
        return this.description;
    }
}