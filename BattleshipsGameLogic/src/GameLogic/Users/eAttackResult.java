package GameLogic.Users;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.EnumSet;

public enum eAttackResult {
    // information inside logic
    HIT_SHIP(1),
    HIT_AND_SUNK_SHIP(2),
    CELL_ALREADY_ATTACKED(4),
    HIT_MINE(8),
    HIT_WATER(16),
    // information outside logic
    GET_ANOTHER_MOVE(
            HIT_SHIP.ID +
            HIT_AND_SUNK_SHIP.ID +
            CELL_ALREADY_ATTACKED.ID),
    MOVE_ENDED(
            HIT_MINE.ID +
            HIT_WATER.ID
    );

    private int ID;

    eAttackResult(int ID) {
        this.ID = ID;
    }

    public Boolean contain(eAttackResult attackResult){
        return ((this.ID & attackResult.ID) !=0);
    }
}
