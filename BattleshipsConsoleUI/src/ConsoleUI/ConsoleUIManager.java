package ConsoleUI;

import GameLogic.Exceptions.*;
import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.Game;
import GameLogic.Game.GameSettings;
import GameLogic.Game.eGameState;
import GameLogic.Game.eAttackResult;
import GameLogic.GamesManager;
import GameLogic.Users.ComputerPlayer;
import GameLogic.Users.Player;
import GameLogic.Users.RegularPlayer;
import javafx.fxml.LoadException;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

import static java.nio.file.Paths.get;

public class ConsoleUIManager {

    private GamesManager gamesManager = new GamesManager();
    // console application may have only 1 game
    private Game activeGame;
    private BoardPrinter boardPrinter = new BoardPrinter();
    private Scanner scanner = new Scanner(System.in);
    private Menu menu = new Menu();
    private boolean exitGameSelected = false;

    public void run() {
        printWelcomeScreen();

        do {
            try {
                if(activeGame != null && activeGame.getActivePlayer() instanceof ComputerPlayer){
                    makeMove();
                }else{
                    eMenuOption menuItemSelected = menu.display(activeGame);
                    invokeMenuItem(menuItemSelected);
                }
            } catch (Exception e) {
                System.out.println("Error: while invoking menu item. Game will restart");
                activeGame = null;
            } finally {
                pressAnyKeyToContinue();
            }
        } while (!exitGameSelected);
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
            case END_GAME:
                endGame();
                break;
            case PLANT_MINE:
                plantMine();
                break;
            case SAVE_GAME:
                saveGame();
                break;
            case LOAD_SAVED_GAME:
                loadSavedGame();
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
//            String path = getFilePathFromUser();
            String path = "C:/battleShip_5_basic.xml";
            if (path != null) {
                activeGame = gamesManager.loadGameFile(path);
                System.out.println("Game loaded");
            }
        } catch (LoadException e) {
            System.out.println("Error while loading game: " + e.getMessage() + ". Please try again.");
        }
    }

    public String getFilePathFromUser() {
        String path;
        File file;
        boolean endOfInput = false;

        do {
            path = getInputFromUser("Please enter an XML path file (0 to return to main menu):");

            if (path != null) {
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
            } else {
                endOfInput = true;
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

    public boolean checkFileType(final File file, final String fileTypeToCompare) {
        String fileType;
        boolean sameType = false;

        try {
            fileType = Files.probeContentType(file.toPath());
            sameType = fileType.equals(fileTypeToCompare);
        } catch (IOException ioException) {
            System.out.println("Error: Unable to determine file type for " + file.getName());
        }

        return sameType;
    }

    // ======================================= Start Game =======================================
    private void startGame() {
        try {
            Player player1 = new RegularPlayer("p1", "Player 1");
            Player player2 = new RegularPlayer("p2", "Player 2");
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
            errorWhileStartingGame();
        } catch (Exception e) {
            //TODO fix error handling
            System.out.println("Error while starting game. " + e.getMessage());
            errorWhileStartingGame();
        }
    }

    private void errorWhileStartingGame() {
        activeGame = null;
        System.out.println("Game file given was invalid therefor it was not loaded. \nPlease check the file and try again.");
    }

    // ======================================= Show Game State =======================================
    private void showGameState() {
        System.out.println("Game state:");
        printPlayerBoard(activeGame.getActivePlayer(), !BoardPrinter.PRINT_SINGLE_BOARD);
    }

    private void printPlayerBoard(Player player, boolean printSingleBoard) {
        System.out.println("Player: " + player.getName());
        System.out.println("Score: " + activeGame.getActivePlayer().getScore());
        boardPrinter.printBoards(player, printSingleBoard);
    }

    // ======================================= Make Move =======================================
    private void makeMove() {
        Instant startTime = Instant.now();
        BoardCoordinates positionToAttack = null;
        eAttackResult attackResult = null;
        boolean moveEnded = false;
        boolean printGameState = true;

        // get active player before players are swapped
        Player activePlayer = activeGame.getActivePlayer();
        do {
            try {
                if (printGameState && attackResult != eAttackResult.CELL_ALREADY_ATTACKED) {
                    showGameState();
                }

                if(activePlayer instanceof ComputerPlayer){
                    positionToAttack = ((ComputerPlayer)activePlayer).getNextMove();
                }else if(activePlayer instanceof RegularPlayer){
                    positionToAttack = getPositionFromUser();
                }
                // TODO positionToAttack can be null ? there is exception ?
                attackResult = gamesManager.makeMove(activeGame, positionToAttack);
                System.out.println("Attack result: " + attackResult);
                moveEnded = attackResult.moveEnded() || activeGame.getGameState() == eGameState.PLAYER_WON;
                if (!moveEnded) {
                    pressAnyKeyToContinue();
                }
            } catch (CellNotOnBoardException e) {
                System.out.println("The cell selected is not on the board, try again");
                printGameState = false;
            }
        } while (!moveEnded);

        Duration turnTime = Duration.between(startTime, Instant.now());
        activePlayer.addTurnDurationToTotal(turnTime);
        System.out.println(String.format("Total duration for this turn was: %d:%02d", turnTime.toMinutes(), turnTime.getSeconds() % 60));

        if (activeGame.getGameState() == eGameState.PLAYER_WON) {
            onGameEnded(eGameState.STARTED);
        }
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

    // ======================================= End Game =======================================
    private void endGame() {
        eGameState gameStateBeforeEndGame = activeGame.getGameState();
        gamesManager.endGame(activeGame);
        onGameEnded(gameStateBeforeEndGame);
    }

    private void onGameEnded(eGameState stateBeforeEndingGame) {
        if (stateBeforeEndingGame.gameHasStarted()) {
            printPlayerWonScreen(activeGame.getWinnerPlayer().getName());
            pressAnyKeyToContinue();
            showStatistics();
            System.out.println("Player boards:");
            printPlayerBoard(activeGame.getActivePlayer(), BoardPrinter.PRINT_SINGLE_BOARD);
            printPlayerBoard(activeGame.getOtherPlayer(), BoardPrinter.PRINT_SINGLE_BOARD);
            System.out.println("The current game has ended, load a new game file if you wold like to play again");
        }

        // set the game to be as if it was just started
        activeGame = null;
    }

    // ======================================= Plant mine =======================================
    private void plantMine() {
        boolean minePlantedOrNotAvailable = false;
        BoardCoordinates position = null;
        printPlayerBoard(activeGame.getActivePlayer(), !BoardPrinter.PRINT_SINGLE_BOARD);

        while (!minePlantedOrNotAvailable) {
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
        }
    }

    // ======================================= Save game =======================================
    private void saveGame() {
        //String fileName = "aa.dat";
        String fileName = getInputFromUser("Please enter a file name for saving:");
        // TODO check validation name

        if (fileName != null && !fileName.isEmpty()) {
            fileName = GameSettings.SAVED_GAME_DIR + fileName + GameSettings.SAVED_GAME_EXTENSION;
            // TODO IOException ?
            try {
                gamesManager.saveGameToFile(activeGame, fileName);
                System.out.println("Game saved successfully!");
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // ======================================= Load saved game=======================================
    private void loadSavedGame() {
//        String fileName = getInputFromUser("Please enter a file name for the loading the saving file:");
        String fileName;

        try {
            HashMap<Integer, String> savedGamesList = getSavedGamesList();
            fileName = getGameToLoadFromUser(savedGamesList);
            activeGame = gamesManager.loadSavedGameFromFile(fileName);
            System.out.println("Game loaded from file!");
        } catch (Exception e) {
            System.out.println("Error while loading saved games: " + e.getMessage());
        }
    }

    private HashMap<Integer, String> getSavedGamesList() throws LoadException {
        HashMap<Integer, String> savedGamesList = new HashMap<>();
        File savedGamesDir = new File(GameSettings.SAVED_GAME_DIR);
        int fileCounter = 0;

        if (!savedGamesDir.exists()) {
            throw new LoadException("No \"Saved Games\" directory found");
        } else {
            for (File file : savedGamesDir.listFiles(File::isFile)) {
                // TODO do we want to check filetype?
//                if (checkFileType(file, GameSettings.SAVED_GAME_EXTENSION)) {
                fileCounter++;
                savedGamesList.put(fileCounter, file.getName());
//                }
            }
        }

        if (fileCounter == 0) {
            throw new LoadException("No saved game file found in \"Saved Games\" directory");
        }

        return savedGamesList;
    }

    private String getGameToLoadFromUser(HashMap<Integer, String> savedGamesList) {
        boolean isValidSelection = false;
        String selectedFileName = null;
        System.out.println("Saved games available :");

        for (Map.Entry<Integer, String> savedGame : savedGamesList.entrySet()) {
            System.out.println(savedGame.getKey() + ") " + savedGame.getValue());
        }

        while (!isValidSelection) {
            try {
                String inputStr = getInputFromUser("Please select an index of a game to load: ");
                int input = Integer.parseInt(inputStr);
                selectedFileName = savedGamesList.get(input);
                if (selectedFileName == null) {
                    System.out.println("Invalid selection, please select the index of one of the files above");
                } else {
                    isValidSelection = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input format please input a number");
            } catch (Exception e) {
                System.out.println("Invalid selection, please select the index of one of the files above");
            }
        }

        return GameSettings.SAVED_GAME_DIR + selectedFileName;
    }

    // ======================================= Exit =======================================
    private void exit() {
        if (activeGame != null) {
            endGame();
        }
        exitGameSelected = true;
        printGoodbyeScreen();
    }

    // ======================================= Other methods =======================================
    private void pressAnyKeyToContinue() {
        System.out.println("\n--- Press enter to continue ---\n");
        scanner.reset();
        scanner.nextLine();
    }

    private String getInputFromUser(String title) {
        System.out.print(title + "(Or 0 to return to main menu): ");
        String inputFromUser = scanner.nextLine();

        return inputFromUser.equals("0") ? null : inputFromUser;
    }

    public void printWelcomeScreen() {
        System.out.println(" ~~~~~ WELCOME TO THE MOST AMAZING BATTLESHIP GAME OF ALL! ~~~~~\n\n");
        System.out.println("                      ,:',:`,:'");
        System.out.println("                   __||_||_||_||__");
        System.out.println("              ____[\"\"\"\"\"\"\"\"\"\"\"\"\"\"\"]____");
        System.out.println("              \\ \" '''''''''''''''''''' |");
        System.out.println("       ~~~~~~^~^~^^~^~^~^~^~^~^~^~~^~^~^^~~^~^");
/*
                   ,:',:`,:'
                __||_||_||_||__
           ____["""""""""""""""]____
           \ " '''''''''''''''''''' |
    ~~~~~~^~^~^^~^~^~^~^~^~^~^~~^~^~^^~~^~^
*/
    }

    public void printGoodbyeScreen() {
        System.out.println("Thank you for playing, goodbye!\n");
        System.out.println("                  /\\/\\,\\,\\ ,");
        System.out.println("                 /        ` \\'\\,");
        System.out.println("                /               '/|_");
        System.out.println("               /                   /");
        System.out.println("              /                   /");
        System.out.println("             /                   ;");
        System.out.println("             ;-\"\"-.  ____       ,");
        System.out.println("            /      )'    `.     '");
        System.out.println("           (    o |        )   ;");
        System.out.println("            ),'\"\"\"\\    o   ;  :");
        System.out.println("            ;\\___  `._____/ ,-:");
        System.out.println("           ;                 @ )");
        System.out.println("          /                `;-'");
        System.out.println("       ,. `-.______________,|");
        System.out.println("  ,(`._||         \\__\\__\\__)|");
        System.out.println(" ,`.`-   \\        '.        |");
        System.out.println("  `._  ) :          )______,;\\_");
        System.out.println("     \\    \\_   _,--/       ,   `.");
        System.out.println("      \\     `--\\   :      /      `.");
        System.out.println("       \\        \\  ;     |         \\");
        System.out.println("        `-._____ ;|      |       _,'");
        System.out.println("                \\/'      `-.----' \\");
        System.out.println("                 /          \\      \\");
        System.out.println();
    }

    private void printPlayerWonScreen(String winner) {
        System.out.println("\n~~~~~ Good job " + winner + " you have won the battle! ~~~~~\n");
        System.out.println("                  _,----.");
        System.out.println("               ,-'     __`.");
        System.out.println("              /    .  /--\\`)");
        System.out.println("             /  .  )\\/_,--\\");
        System.out.println("            /  ,'\\/,-'    _\\_");
        System.out.println("           |  /  ,' ,---'  __\\");
        System.out.println("          ,' / ,:     _,-\\'_,(");
        System.out.println("           (/ /  \\ \\,'   |'  _)         ,. ,.,.");
        System.out.println("            \\/   |          '  \\        \\ ,. \\ )");
        System.out.println("             \\, ,-              \\       /,' )//");
        System.out.println("              ; \\'`      _____,-'      _|`  ,'");
        System.out.println("               \\ `\"\\    (_,'_)     _,-'    ,'");
        System.out.println("                \\   \\       \\  _,-'       ,'");
        System.out.println("                |, , )       `'       _,-'");
        System.out.println("                /`/ Y    ,    \\   _,-'");
        System.out.println("                   :    /      \\-'");
        System.out.println("                   |     `--.__\\___");
        System.out.println("                   |._           __)");
        System.out.println("                   |  `--.___    _)");
        System.out.println("                   |         `----'");
        System.out.println("                  /                \\");
        System.out.println("                 '                . )");
        System.out.println();
    }
}