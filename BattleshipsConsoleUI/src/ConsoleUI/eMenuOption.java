package ConsoleUI;

import GameLogic.Game.eGameState;
import jdk.internal.org.objectweb.asm.Type;

import java.util.EnumSet;

public enum eMenuOption {
    LOAD_GAME(1, "Load game", EnumSet.of(eGameState.INVALID, eGameState.INITIALIZED, eGameState.LOADED)),
    START_GAME(2, "Start game", EnumSet.of(eGameState.INITIALIZED, eGameState.LOADED)),
    SHOW_GAME_STATE(3, "Show game state", EnumSet.of(eGameState.STARTED)),
    MAKE_MOVE(4, "Make a move", EnumSet.of(eGameState.STARTED)),
    SHOW_STATISTICS(5, "Show statistics", EnumSet.of(eGameState.STARTED)),
    END_GAME(6, "End game", EnumSet.of(eGameState.STARTED)),
    PLANT_MINE(7,"Plant mine",EnumSet.of(eGameState.STARTED)),
    EXIT(8, "Exit", EnumSet.allOf(eGameState.class));

    private String description;
    private int ID;
    private boolean isDisplayed = true;
    private EnumSet<eGameState> displayedConditions;

    eMenuOption(int optionID, String description, EnumSet<eGameState> displayConditions) {
        this.ID = optionID;
        this.description = description;
        this.displayedConditions = displayConditions;
    }

    public Boolean sameGameState(eGameState gameState){
        return displayedConditions.contains(gameState);
    }

    public static eMenuOption valueOf(int optionID) {
        eMenuOption value = null;
        for (eMenuOption menuOption : eMenuOption.values()) {
            if (optionID == menuOption.ID) {
                value = menuOption;
                break;
            }
        }

        return value;
    }

    @Override
    public String toString() {
        return ID + ") " + description;
    }
}
