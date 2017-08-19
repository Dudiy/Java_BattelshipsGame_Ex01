package ConsoleUI;

import GameLogic.Game.eGameState;

import java.util.Scanner;

public class Menu {
    private final String MENU_SEPERATOR = "\n\n======================== Menu ========================";
    private eGameState gameState;

    public eMenuOption display(eGameState gameState) {
        this.gameState = gameState;
        printMenu();
        eMenuOption userSelection = getUserSelection();
        return userSelection;
    }

    private void printMenu() {
        System.out.println(MENU_SEPERATOR);
        for (eMenuOption menuOption : eMenuOption.values()) {
            if (menuOption.sameGameState(gameState)) {
                System.out.println(menuOption);
            }
        }
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
