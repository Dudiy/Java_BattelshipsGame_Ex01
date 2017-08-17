package ConsoleUI;

import GameLogic.Exceptions.*;
import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.Game;
import GameLogic.Game.eGameState;
import GameLogic.GamesManager;
import GameLogic.Users.Player;
import GameLogic.Users.eAttackResult;
import javafx.fxml.LoadException;

import java.util.Scanner;

public class ConsoleUIManager {
    GamesManager gamesManager = new GamesManager();
    // console application may have only 1 game
    Game activeGame;
    BoardPrinter boardPrinter = new BoardPrinter();
    Scanner scanner = new Scanner(System.in);

    Menu menu = new Menu();

    public void run() {
        do {
            Menu.eMenuOption menuItemSelected = menu.display(
                    activeGame == null ? eGameState.INVALID : activeGame.getGameState());
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
            case SHOW_GAME_STATE:
                showGameState();
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
            BoardPrinter bd = new BoardPrinter();
            showGameState();
            System.out.println("Game started");
        } catch (InvalidGameObjectPlacementException e) {
            String message = String.format("\nError while initializing board.\n" +
                    "Cannot place a given " + e.getGameObjectType() + " at position " + e.GetCoordinates() + ".\n" +
                    "reason: " + e.getReason()) + "\n";
            System.out.println(message);
        } catch (Exception e) {
            //TODO fix error handling
            System.out.println("Error while starting game: " + e.getMessage() + " please try again.");
        }
    }

    private void makeMove() {
        boolean moveSuccesfull = false;
        BoardCoordinates cellToAttack = getPositionFromUser();

        while (!moveSuccesfull) {
            try {
                if (gamesManager.makeMove(activeGame, cellToAttack) != eAttackResult.CELL_ALREADY_ATTACKED){
                    moveSuccesfull = true;
                }
            } catch (CellNotOnBoardException e) {
                System.out.println("The cell selected is not on the board, try again");
            }
        }
    }

    private void showGameState() {
        System.out.println("Game state:");
        System.out.println("Current player: " + activeGame.getActivePlayer().getName());
        System.out.println("Score: " + activeGame.getActivePlayer().getScore());
        boardPrinter.printBothBoards(activeGame);
    }

    public BoardCoordinates getPositionFromUser() {
        BoardCoordinates userSelection = null;
        boolean isValidSelection = false;

        while (!isValidSelection) {
            try {
                System.out.print("Please select cell to attack (format = \"A1\": ");
                userSelection = BoardCoordinates.Parse(scanner.nextLine());
                isValidSelection = true;
            } catch (Exception e) {
                System.out.println("Invalid input please try again (Format = \"A1\"");
            }
        }

        return userSelection;
    }
}
