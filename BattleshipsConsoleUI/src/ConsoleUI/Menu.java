package ConsoleUI;

import java.util.Scanner;

public class Menu {
    public enum eMenuOption {
        LOAD_GAME(1, "Load game"),
        START_GAME(2, "Start game"),
        SHOW_GAME_STATE(3, "Show game state");

        private String description;
        private int ID;

        eMenuOption(int optionID, String description) {
            this.ID = optionID;
            this.description = description;
        }

        public static eMenuOption valueOf(int optionID) {
            eMenuOption value = null;
            for (eMenuOption menuOption : eMenuOption.values()) {
                if (optionID == menuOption.ID) {
                    value = menuOption;
                }
            }

            return value;
        }

        @Override
        public String toString() {
            return ID + ") " + description;
        }
    }

    public eMenuOption display() {
        boolean isValidSelection = false;
        int userIntSelection = 0;
        Scanner scanner = new Scanner(System.in);
        eMenuOption userSelection = null;

        for (eMenuOption menuOption : eMenuOption.values()) {
            System.out.println(menuOption);
        }

        System.out.print("Please select one of the options above: ");
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
