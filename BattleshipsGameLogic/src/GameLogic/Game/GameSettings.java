package GameLogic.Game;

import javafx.fxml.LoadException;
import jaxb.generated.BattleShipGame;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

public class GameSettings {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "GameLogic.jaxb.generated";
    private static final int MIN_BOARD_SIZE = 5;
    private static final int MAX_BOARD_SIZE = 20;
    private int boardSize;
    private eGameType gameType;
    private int minesPerPlayer;
    private BattleShipGame gameLoadedFromXml;

    // private ctor, GameSettings can only be created by calling LoadGameFile
    private GameSettings() {
        this.boardSize = 0;
        this.minesPerPlayer = 0;
        this.gameType = eGameType.BASIC;
        this.gameLoadedFromXml = null;
    }

    // creates a new GameSettings object from xml file, with validation
    public static GameSettings LoadGameFile(String gameFilePath) throws LoadException {
        GameSettings gameSettings = new GameSettings();
        InputStream inputStream = GameSettings.class.getResourceAsStream(gameFilePath);
        try {
            gameSettings.gameLoadedFromXml = deserializeFrom(inputStream);
            validateGameSettings(gameSettings);
        } catch (JAXBException e) {
            throw new LoadException("error loading xml file - JAXB error");
        } catch (Exception e) {
            throw new LoadException(e.getMessage());
        }

        return gameSettings;
    }

    private static void validateGameSettings(GameSettings gameSettings) throws Exception {
        BattleShipGame objectImported = gameSettings.gameLoadedFromXml;

        // validate board size
        gameSettings.boardSize = objectImported.getBoardSize();
        if (gameSettings.boardSize < MIN_BOARD_SIZE || gameSettings.boardSize > MAX_BOARD_SIZE) {
            throw new Exception("Invalid board size");
        }

        // validate game type
        String gameTypeStr = objectImported.getGameType();
        if (gameTypeStr.toUpperCase().equals("BASIC")) {
            gameSettings.gameType = eGameType.BASIC;
        } else if (gameTypeStr.toUpperCase().equals("ADVANCE")) {
            gameSettings.gameType = eGameType.ADVANCED;
        } else {
            throw new Exception("Invalid game type");
        }

        // validate minesPerPlayer
        // TODO can this be 0?
        gameSettings.minesPerPlayer = objectImported.getMine().getAmount();
        if (gameSettings.minesPerPlayer <= 0) {
            throw new Exception("Number of mines per player is not a non negative integer");
        }

        // validate number of boards
        if (objectImported.getBoards().getBoard().size() != 2) {
            throw new Exception("Invalid number of boards");
        }
    }

    private static BattleShipGame deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (BattleShipGame) u.unmarshal(in);
    }

    // Getters
    public int getBoardSize() {
        return boardSize;
    }

    public eGameType getGameType() {
        return gameType;
    }

    public int getMinesPerPlayer() {
        return minesPerPlayer;
    }

    public BattleShipGame getGameLoadedFromXml() {
        return gameLoadedFromXml;
    }
}
