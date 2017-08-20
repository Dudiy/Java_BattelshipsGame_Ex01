package ConsoleUI;

import GameLogic.Exceptions.*;
import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.Game;
import GameLogic.Game.eGameState;
import GameLogic.GamesManager;
import GameLogic.Users.Player;
import GameLogic.Game.eAttackResult;
import javafx.fxml.LoadException;

import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

public class ConsoleUIManager {
    private GamesManager gamesManager = new GamesManager();
    // console application may have only 1 game
    private Game activeGame;
    private BoardPrinter boardPrinter = new BoardPrinter();
    private Scanner scanner = new Scanner(System.in);
    private Menu menu = new Menu();
    private boolean exit = false;

    public void run() {
        eGameState gameState;
        do {
            gameState = activeGame == null ? eGameState.INVALID : activeGame.getGameState();
            // TODO ?
            try {
                eMenuOption menuItemSelected = menu.display(gameState);
                invokeMenuItem(menuItemSelected);
            } catch (Exception e) {
                System.out.println("Error: while invoking menu item. Game will restart");
                activeGame = null;
            }
            gameState = activeGame == null ? eGameState.INVALID : activeGame.getGameState();
        } while (!exit);
    }

    private void invokeMenuItem(eMenuOption menuItemSelected) {
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
            case PLANT_MINE:
                plantMine();
                break;
            case END_GAME:
                endGame();
                break;
            case EXIT:
                exit();
                break;
        }
    }

    // ======================================= Load Game =======================================
    private void loadGame() {
        try {
            // TODO get path from user(uncomment)
            //String path = getFilePathFromUser();
            String path = "C:/battleShip_5_basic.xml";
            //C:\Or\Semester C\Java\Projects\Git\Java_BattelshipsGame_Ex01\BattleshipsGameLogic\src\resources\1.txt
            //C:\Or\Semester C\Java\Projects\Git\Java_BattelshipsGame_Ex01\BattleshipsGameLogic\src\resources\battleShip_5_basicBAD1.xml

            if (path != null) {
                activeGame = gamesManager.loadGameFile(path);
                System.out.println("Game loaded");
            }
        } catch (LoadException e) {
            System.out.println("Error while loading game: " + e.getMessage() + ". Please try again.");
        }
    }

    public String getFilePathFromUser() {
        Scanner scanner = new Scanner(System.in);
        String path;
        Boolean endOfInput = false;
        File file;

        do {
            System.out.println("Please enter a XML path file(Or 0 to return to main menu):");
            path = scanner.nextLine();
            if (path.equals("0")) {
                path = null;
                endOfInput = true;
            } else {
                file = openFileFromPath(path);
                if (file != null) {
                    if (checkFileType(file, "text/xml")) {
                        endOfInput = true;
                    } else {
                        System.out.println("Error: file type mismatch");
                    }
                } else {
                    System.out.println("Error: file doesn't exist");
                }
            }
        } while (!endOfInput);

        return path;
    }

    private File openFileFromPath(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || file.isDirectory()) {
            file = null;
        }
        return file;
    }

    public Boolean checkFileType(final File file, final String fileTypeToCompare) {
        String fileType;
        boolean sameType = false;
        try {
            fileType = Files.probeContentType(file.toPath());
            if (fileType.equals(fileTypeToCompare)) {
                sameType = true;
            }
        } catch (IOException ioException) {
            System.out.println("Error: Unable to determine file type for " + file.getName());
        }
        return sameType;
    }

    // ======================================= Start Game =======================================
    private void startGame() {
        try {
            Player player1 = new Player("p1", "Player 1");
            Player player2 = new Player("p2", "Player 2");
            gamesManager.startGame(activeGame, player1, player2);
            // give each player 2 mines
            player1.getMyBoard().setMinesAvailable(2);
            player2.getMyBoard().setMinesAvailable(2);
            showGameState();
            System.out.println("Game started");
        } catch (InvalidGameObjectPlacementException e) {
            String message = String.format("\nError while initializing board.\n" +
                    "Cannot place a given " + e.getGameObjectType() + " at position " + e.GetCoordinates() + ".\n" +
                    "reason: " + e.getReason()) + "\n";
            System.out.println(message);
            errorWhileStartGame();
        } catch (Exception e) {
            //TODO fix error handling
            System.out.println("Error while starting game. " + e.getMessage());
            errorWhileStartGame();
        }
    }

    private void errorWhileStartGame() {
        activeGame = null;
        System.out.println("Game file unloaded. Please load other file game in order to start the game.");
    }

    // ======================================= Show Game State =======================================
    private void showGameState() {
        System.out.println("Game state:");
        printPlayerBoard(activeGame.getActivePlayer());
    }

    private void printPlayerBoard(Player player) {
        System.out.println("Current player: " + player.getName());
        System.out.println("Score: " + activeGame.getActivePlayer().getScore());
        boardPrinter.printBoards(player);
    }

    // ======================================= Make Move =======================================
    private void makeMove() {
        // get active player before players are swapped
        Player activePlayer = activeGame.getActivePlayer();
        Instant startTime = Instant.now();
        BoardCoordinates positionToAttack;
        eAttackResult attackResult = null;
        boolean moveSuccessful = false;
        boolean printGameState = true;

        do {
            try {
                if (printGameState && attackResult != eAttackResult.CELL_ALREADY_ATTACKED) {
                    showGameState();
                }
                positionToAttack = getPositionFromUser();
                attackResult = gamesManager.makeMove(activeGame, positionToAttack);
                System.out.println("Attack result: " + attackResult);
                moveSuccessful = true;
            } catch (CellNotOnBoardException e) {
                System.out.println("The cell selected is not on the board, try again");
                printGameState = false;
            }
        } while (!moveSuccessful || attackResult == eAttackResult.CELL_ALREADY_ATTACKED);
//    } while (!attackResult.moveEnded());

        Duration turnTime = Duration.between(startTime, Instant.now());
        System.out.println(String.format("Total duration for this turn was: %d:%02d", turnTime.toMinutes(), turnTime.getSeconds() % 60));
        activePlayer.addTurnDurationToTotal(turnTime);
        pressAnyKeyToContinue();
        showGameState();
    }

    public BoardCoordinates getPositionFromUser() {
        BoardCoordinates userSelection = null;
        boolean isValidSelection = false;

        while (!isValidSelection) {
            try {
                System.out.print("Please select cell coordinates (format = \"A1\"): ");
                userSelection = BoardCoordinates.Parse(scanner.nextLine());
                isValidSelection = true;
            } catch (Exception e) {
                System.out.println("Invalid input please try again (Format = \"A1\")");
            }
        }
        return userSelection;
    }

    // ======================================= Show Statistics =======================================
    private void showStatistics() {
        System.out.println("***** Showing game statistics: *****");
        // total turns played
        System.out.println("\tTotal turns played: " + activeGame.getMovesCounter());
        // total game duration
        Duration gameDuration = gamesManager.getGameDuration(activeGame);
        String durationStr = String.format("%d:%02d", gameDuration.toMinutes(), gameDuration.getSeconds() % 60);
        System.out.println("\tTotal game time: " + durationStr);
        // player info
        System.out.println("\n\t***** Player statistics *****");
        showPlayerStatistics(activeGame.getActivePlayer());
        System.out.println();
        showPlayerStatistics(activeGame.getOtherPlayer());
        System.out.println();

    }

    private void showPlayerStatistics(Player player) {
        System.out.println("\t\tShowing player statistics for " + player.getName());
        System.out.println("\t\tCurrent score: " + player.getScore());
        System.out.println("\t\tTimes missed: " + player.getTimesMissed());
        // avg turn duration
        Duration avgDuration = player.getAvgTurnDuration();
        System.out.println(String.format("\t\tAverage turn duration: %d:%02d", avgDuration.toMinutes(), avgDuration.getSeconds() % 60));
    }

    // ======================================= Plant mine =======================================

    private void plantMine() {
        boolean minePlantedOrNotAvailable = false;
        BoardCoordinates position = null;
        do {
            try {
                position = getPositionFromUser();
                gamesManager.plantMine(activeGame, position);
                minePlantedOrNotAvailable = true;
                System.out.println("You have successfully planted a mine at position " + position.toString() + " ;)");
            } catch (CellNotOnBoardException e) {
                System.out.println(e.getMessage());
            } catch (InvalidGameObjectPlacementException e) {
                System.out.println(e.getMessage());
            } catch (NoMinesAvailableException e) {
                minePlantedOrNotAvailable = true;
                System.out.println(e.getMessage());
            }
        } while (!minePlantedOrNotAvailable);

        pressAnyKeyToContinue();
    }

    // ======================================= End Game =======================================
    private void endGame() {
        eGameState gameStateBeforeEndGame = activeGame.getGameState();
        gamesManager.endGame(activeGame);
        if(gameStateBeforeEndGame.isGameStart()){
            System.out.println("The winner is: " + activeGame.getWinnerPlayer().getName() + "!!! :)");
            System.out.println("Game ended.");
            System.out.println("Players boards:");
            printPlayerBoard(activeGame.getActivePlayer());
            printPlayerBoard(activeGame.getOtherPlayer());
        }
        // it get the user to the first step of the application
        activeGame = null;
    }

    // ======================================= Exit =======================================
    private void exit() {
        if (activeGame != null) {
            saveActiveGameToFile();
            endGame();
        }
        exit = true;
        System.out.println("Game will close. Goodbye !");
    }

    private void saveActiveGameToFile() {
        try {
            FileOutputStream fileOutputStreamOfGame = new FileOutputStream(new File("./game.xml"));
            XMLEncoder xmlEncoder = new XMLEncoder(fileOutputStreamOfGame);
            xmlEncoder.writeObject(activeGame);
            xmlEncoder.close();
            fileOutputStreamOfGame.close();
        } catch (Exception e) {

        }
    }

    // ======================================= Other methods =======================================
    private void pressAnyKeyToContinue() {
        System.out.println("\n--- Press enter to continue ---\n");
        scanner.reset();
        try {
            System.in.read();
        } catch (IOException e) {
            System.out.println("IO Error caught, resuming as if key was pressed");
        }
    }
}