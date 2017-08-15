package ConsoleUI;

import GameLogic.Game.Game;
import GameLogic.GamesManager;
import GameLogic.Users.Player;
import javafx.fxml.LoadException;

public class ConsoleUIManager {
    GamesManager gamesManager = new GamesManager();
    Menu menu = new Menu();

    public void run() {
        Menu.eMenuOptions menuItemSelected = menu.display();
//        invokeMenuItem(menuItemSelected);

        Game newGame = null;
        try {
            newGame = gamesManager.loadGameFile("/resources/battleShip_5_basic.xml");
            Player player1 = new Player("p1", "Player 1");
            Player player2 = new Player("p2", "Player 2");
            gamesManager.startGame(newGame, player1, player2);
        } catch (LoadException e) {
            System.out.println("Error Loading file, please try again");
        } catch (Exception e) {
            //TODO fix error handling
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void invokeMenuItem(Menu.eMenuOptions menuItemSelected) {
        switch (menuItemSelected) {
            case LOAD_GAME:
                try {
                    Game newGame = gamesManager.loadGameFile("/resources/battleShip_5_basic.xml");
                } catch (LoadException e) {
                    e.printStackTrace();
                }
                break;
            case START_GAME:

                break;
        }
    }
}
