package ConsoleUI;

import GameLogic.Exceptions.*;
import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.Game;
import GameLogic.Game.eGameState;
import GameLogic.GamesManager;
import GameLogic.Users.Player;
import GameLogic.Game.eAttackResult;
import com.sun.corba.se.impl.oa.NullServantImpl;
import javafx.fxml.LoadException;


import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
//public class CheckValidXML {
//    public static void main(String[] args) {
//
//
//        File xmlFile = new File("XML File Location");
//        if (xmlFile.exists()) {
//            if (isValidXMLFile(xmlFile.getAbsolutePath().toString())) {
//                System.out.println("Valid XML");
//            }
//        }
//    }

import java.nio.file.Files;
import java.util.Scanner;

public class ConsoleUIManager {
    GamesManager gamesManager = new GamesManager();
    // console application may have only 1 game
    Game activeGame;
    BoardPrinter boardPrinter = new BoardPrinter();
    Scanner scanner = new Scanner(System.in);

    Menu menu = new Menu();

    public void run() {
        eGameState gameState;
        do {
            gameState = activeGame == null ? eGameState.INVALID : activeGame.getGameState();
            Menu.eMenuOption menuItemSelected = menu.display(gameState);
            invokeMenuItem(menuItemSelected);
            gameState = activeGame == null ? eGameState.INVALID : activeGame.getGameState();
        } while (gameState != eGameState.PLAYER_QUIT);
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
    // ======================================= Load Game =======================================
    private void loadGame() {
        try {
            // TODO get path from user(uncomment)
            //String path = getFilePathFromUser();
            //C:\Or\Semester C\Java\Projects\Git\Java_BattelshipsGame_Ex01\BattleshipsGameLogic\src\resources\battleShip_5_basic.xml
            String path  = "/resources/battleShip_5_basic.xml";
            if (path != null) {
                activeGame = gamesManager.loadGameFile(path);
                System.out.println("Game loaded");
            }
        } catch (LoadException e) {
            System.out.println("Error while loading game: " + e.getMessage() + " please try again.");
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

    // ======================================= Show Game State =======================================
    private void showGameState() {
        System.out.println("Game state:");
        System.out.println("Current player: " + activeGame.getActivePlayer().getName());
        System.out.println("Score: " + activeGame.getActivePlayer().getScore());
        boardPrinter.printBothBoards(activeGame);
    }

    // ======================================= Make Move =======================================
    private void makeMove() {
        showGameState();
        BoardCoordinates positionToAttack;
        eAttackResult attackResult = null;
        do {
            try {
                positionToAttack = getPositionFromUser();
                attackResult = gamesManager.makeMove(activeGame, positionToAttack);
                System.out.println("Attack result: " + attackResult);
            } catch (CellNotOnBoardException e) {
                System.out.println("The cell selected is not on the board, try again");
            }
        } while (!attackResult.moveEnded());

        showGameState();
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

    // ======================================= Show Statistics =======================================
    private void showStatistics() {
        // TODO
    }

    // ======================================= End Game =======================================
    private void endGame() {
        // TODO

    }
}