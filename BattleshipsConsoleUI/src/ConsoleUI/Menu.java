package ConsoleUI;

import GameLogic.Game.eGameState;
import GameLogic.Users.Player;
import java.util.Scanner;

public class Menu {
    private final String MENU_TOP = "\n╔═══════════════════════ Menu ═══════════════════════╗";
    private final String MENU_BOTTOM = "╚════════════════════════════════════════════════════╝";
    private final String MENU_VERTICAL = "║";
    private final int menuWidth = MENU_BOTTOM.length();
    private eGameState gameState;
    /*public enum eMenuOption {
        LOAD_GAME(1, "Load game", EnumSet.of(eGameState.INVALID, eGameState.INITIALIZED, eGameState.LOADED)),
        START_GAME(2, "Start game", EnumSet.of(eGameState.INITIALIZED, eGameState.LOADED)),
        SHOW_GAME_STATE(3, "Show game state", EnumSet.of(eGameState.STARTED)),
        MAKE_MOVE(4, "Make a move", EnumSet.of(eGameState.STARTED)),
        SHOW_STATISTICS(5, "Show statistics", EnumSet.of(eGameState.STARTED)),
        END_GAME(6, "End game", EnumSet.of(eGameState.STARTED));

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
    }*/

    public eMenuOption display(eGameState gameState) {
        this.gameState = gameState;
        printMenu();
        eMenuOption userSelection = getUserSelection();
        return userSelection;
    }

    private void printMenu() {
        System.out.println(MENU_TOP);
        for (eMenuOption menuOption : eMenuOption.values()) {
            if (menuOption.sameGameState(gameState)) {
                System.out.print(MENU_VERTICAL);
                System.out.print(menuOption);
                for (int i = 0; i < menuWidth - menuOption.toString().length() - 2; i++) {
                    System.out.print(" ");
                }
                System.out.println(MENU_VERTICAL);
            }
        }
        System.out.println(MENU_BOTTOM);
    }

    private eMenuOption getUserSelection() {
        eMenuOption userSelection = null;
        boolean isValidSelection = false;
        int userIntSelection = 0;
        Scanner scanner = new Scanner(System.in);

        System.out.print("\nPlease select one of the options above: ");
        do {
            try {
                userIntSelection = scanner.nextInt();
                userSelection = eMenuOption.valueOf(userIntSelection);
                if (userSelection != null && userSelection.sameGameState(gameState)) {
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
