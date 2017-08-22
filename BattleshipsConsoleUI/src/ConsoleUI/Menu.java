package ConsoleUI;

import GameLogic.Game.Game;
import GameLogic.Game.eGameState;

import java.security.MessageDigest;
import java.util.Scanner;

public class Menu {
    private final String MENU_TOP = "\n╔═══════════════════════ Menu ═══════════════════════╗";
    private final String MENU_BOTTOM = "╚════════════════════════════════════════════════════╝";
    private final String MENU_VERTICAL = "║";
    private final int menuWidth = MENU_BOTTOM.length();
    private eGameState gameState;

    public eMenuOption display(Game game) {
        this.gameState = game == null ? eGameState.INVALID : game.getGameState();

        printMenu();
        String activePlayerName =
                (game == null || game.getActivePlayer() == null) ?
                        "" :
                        game.getActivePlayer().getName();
        eMenuOption userSelection = getUserSelection(activePlayerName);

        return userSelection;
    }

    private void printMenu() {
        System.out.println(MENU_TOP);

        for (eMenuOption menuOption : eMenuOption.values()) {
            if (menuOption.isVisibleAtGameState(gameState)) {
                int numSpaces = menuWidth - menuOption.toString().length() - 2;
                String spacesAfter = String.format("%" + numSpaces + "s", " ");
                System.out.println(MENU_VERTICAL + menuOption + spacesAfter + MENU_VERTICAL);
            }
        }

        System.out.println(MENU_BOTTOM);
    }

    private eMenuOption getUserSelection(String activePlayerName) {
        Scanner scanner = new Scanner(System.in);
        eMenuOption userSelection = null;
        boolean isValidSelection = false;
        int userIntSelection;

        String title = "Please select one of the options above" + (activePlayerName.isEmpty() ? ":" : "(active player is " + activePlayerName + "):");
        System.out.print(title);
        do {
            try {
                userIntSelection = scanner.nextInt();
                userSelection = eMenuOption.valueOf(userIntSelection);
                if (userSelection != null && userSelection.isVisibleAtGameState(gameState)) {
                    isValidSelection = true;
                } else {
                    System.out.print("Invalid selection please select one of the options above: ");
                    scanner.nextLine();
                }
            } catch (Exception ex) {
                System.out.print("Invalid input, please input an integer representing one of the options above: ");
                scanner.nextLine();
            }
        } while (!isValidSelection);

        System.out.println();
        return userSelection;
    }
}
