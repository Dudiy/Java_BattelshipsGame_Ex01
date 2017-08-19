package ConsoleUI;

import GameLogic.Exceptions.*;
import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.Game;
import GameLogic.Game.eGameState;
import GameLogic.GamesManager;
import GameLogic.Users.Player;
import GameLogic.Game.eAttackResult;
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
            eGameState gameState = activeGame == null ? eGameState.INVALID : activeGame.getGameState();
            Menu.eMenuOption menuItemSelected = menu.display(gameState);
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
            case MAKE_MOVE:
                makeMove();
                break;
            case SHOW_STATISTICS:
                showStatistics();
                break;
            case END_GAME:
                endGame();
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

    private void showGameState() {
        System.out.println("Game state:");
        System.out.println("Current player: " + activeGame.getActivePlayer().getName());
        System.out.println("Score: " + activeGame.getActivePlayer().getScore());
        boardPrinter.printBothBoards(activeGame);
    }

    private void makeMove() {
        showGameState();
//        boolean moveEnded = false;
        BoardCoordinates positionToAttack;
        eAttackResult attackResult = null;
//        while (!moveEnded) {
        do{
            try {
                positionToAttack = getPositionFromUser();
                attackResult = gamesManager.makeMove(activeGame, positionToAttack);
//                moveEnded = attackResult.contain(eAttackResult.MOVE_ENDED);
                System.out.println("Attack result: " + attackResult);
//                printAttackResult(attackResult);
//                if (gamesManager.makeMove(activeGame, cellToAttack) != eAttackResult.CELL_ALREADY_ATTACKED) {
//                    moveEnd = true;
//                }
            } catch (CellNotOnBoardException e) {
                System.out.println("The cell selected is not on the board, try again");
            }
        } while (!attackResult.moveEnded());
        showGameState();
    }

    private void printAttackResult(eAttackResult attackResult) {
//        if (attackResult.contain(eAttackResult.MOVE_ENDED)) {
//            if(attackResult == eAttackResult.HIT_MINE){
//                System.out.println("You hit a mine :( ");
//            }
//            System.out.println("Move end");
//        } else if (attackResult.contain(eAttackResult.GET_ANOTHER_MOVE)) {
//            if(attackResult == eAttackResult.HIT_SHIP){
//                System.out.println("you hit a ship !");
//                System.out.println("You get another move ! :) ");
//
//            }
//            else if(attackResult == eAttackResult.HIT_AND_SUNK_SHIP){
//                System.out.println("Congratulations you hit a whole ship !");
//                System.out.println("You get another move ! :) ");
//            }
//            else if(attackResult == eAttackResult.CELL_ALREADY_ATTACKED){
//                System.out.println("You already try to attack that cell.");
//                System.out.println("Please try again");
//            }
//        }
        if (attackResult.moveEnded()) {
            if (attackResult == eAttackResult.HIT_MINE) {
                System.out.println("You hit a mine :( ");
            }
            System.out.println("Move ended");
        } else {
            if (attackResult == eAttackResult.HIT_SHIP) {
                System.out.println("you hit a ship !");
                System.out.println("You get another move ! :) ");
            } else if (attackResult == eAttackResult.HIT_AND_SUNK_SHIP) {
                System.out.println("Congratulations you just sunk a ship !");
                System.out.println("You get another move ! :) ");
            } else if (attackResult == eAttackResult.CELL_ALREADY_ATTACKED) {
                System.out.println("You have already tried to attack that cell.");
                System.out.println("Try again");
            }
        }
    }

    public BoardCoordinates getPositionFromUser() {
        BoardCoordinates userSelection = null;
        boolean isValidSelection = false;

        while (!isValidSelection) {
            try {
                System.out.print("Please select cell to attack (format = \"A1\"): ");
                userSelection = BoardCoordinates.Parse(scanner.nextLine());
                isValidSelection = true;
            } catch (Exception e) {
                System.out.println("Invalid input please try again (Format = \"A1\")");
            }
        }
        return userSelection;
    }

    private void showStatistics() {
        // TODO
    }

    private void endGame() {
        // TODO

    }
}