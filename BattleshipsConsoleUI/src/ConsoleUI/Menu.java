package ConsoleUI;

import GameLogic.Game.eGameState;

import java.util.EnumSet;
import java.util.Scanner;

public class Menu {
    private final String MENU_SEPERATOR = "\n\n======================== Menu ========================";
    public enum eMenuOption {
        LOAD_GAME(1, "Load game", EnumSet.of(eGameState.INVALID, eGameState.INITIALIZED, eGameState.LOADED)),
        START_GAME(2, "Start game", EnumSet.of(eGameState.INITIALIZED, eGameState.LOADED)),
        SHOW_GAME_STATE(3, "Show game state", EnumSet.of(eGameState.STARTED));

        private String description;
        private int ID;
        private boolean isDisplayed = true;
        private EnumSet<eGameState> displayedConditions;

        eMenuOption(int optionID, String description, EnumSet<eGameState> displayConditions) {
            this.ID = optionID;
            this.description = description;
            this.displayedConditions = displayConditions;
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

    public eMenuOption display(eGameState gameState) {
        boolean isValidSelection = false;
        int userIntSelection = 0;
        Scanner scanner = new Scanner(System.in);
        eMenuOption userSelection = null;
        System.out.println(MENU_SEPERATOR);
        for (eMenuOption menuOption : eMenuOption.values()) {
            if (menuOption.displayedConditions.contains(gameState)) {
                System.out.println(menuOption);
            }
        }

        System.out.print("\nPlease select one of the options above: ");
        do {
            try {
                userIntSelection = scanner.nextInt();
                userSelection = eMenuOption.valueOf(userIntSelection);
                if (userSelection != null) {
                    isValidSelection = true;
                } else {
                    System.out.print("Invalid selection please select one of the options above: ");
                }
            } catch (Exception ex) {
                System.out.print("Invalid input, please input an integer representing one of the options above: ");
                scanner.nextLine();
            }
        } while (!isValidSelection);

        return userSelection;
    }
}
