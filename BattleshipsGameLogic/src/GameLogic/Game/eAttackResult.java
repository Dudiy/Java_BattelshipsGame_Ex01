package GameLogic.Game;

public enum eAttackResult {
    HIT_SHIP("Hit a ship"),
    HIT_AND_SUNK_SHIP("Sunk a ship"),
    CELL_ALREADY_ATTACKED("Cell already attacked"),
    HIT_MINE("Hit a mine"),
    HIT_WATER("Missed");

    private String description;

    eAttackResult(String description) {
        this.description = description;
    }

    public boolean moveEnded() {
        return this == HIT_MINE || this == HIT_WATER;
    }

    public boolean isValidMove(){
        return this != CELL_ALREADY_ATTACKED;
    }

    public boolean isScoreIncrementer(){
        return this == HIT_MINE || this == HIT_SHIP || this == HIT_AND_SUNK_SHIP;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
