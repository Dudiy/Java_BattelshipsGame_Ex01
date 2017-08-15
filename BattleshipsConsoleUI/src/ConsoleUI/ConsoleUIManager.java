package ConsoleUI;

import GameLogic.Game.Game;
import GameLogic.Game.eGameState;
import GameLogic.GamesManager;
import GameLogic.Users.Player;
import javafx.fxml.LoadException;

public class ConsoleUIManager {
    GamesManager gamesManager = new GamesManager();
    // console application may have only 1 game
    Game activeGame;

    Menu menu = new Menu();


    public void run() {
        do {
            Menu.eMenuOption menuItemSelected = menu.display();
            invokeMenuItem(menuItemSelected);
        } while (activeGame.getGameState() != eGameState.PLAYER_QUIT);
    }

    private void invokeMenuItem(Menu.eMenuOption menuItemSelected) {
        switch (menuItemSelected) {
            case LOAD_GAME:
                loadGame();
                break;
            case START_GAME:
                startGame();
                break;
        }
    }

    private void loadGame() {
        try {
            activeGame = gamesManager.loadGameFile("/resources/battleShip_5_basic.xml");
            System.out.println("Game loaded");
        } catch (LoadException e) {
            System.out.println("Error while loading game: " + e.getMessage() + " please try again.");
        }
    }

    private void startGame() {
        try {
            Player player1 = new Player("p1", "Player 1");
            Player player2 = new Player("p2", "Player 2");
            gamesManager.startGame(activeGame, player1, player2);
            System.out.println("Game started");
            // TODO print board
        } catch (Exception e) {
            //TODO fix error handling
            System.out.println("Error while starting game: " + e.getMessage() + " please try again.");
        }
    }
}
