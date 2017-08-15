package GameLogic.Game;

import javafx.fxml.LoadException;
import jaxb.generated.BattleShipGame;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class GameSettings {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "GameLogic.jaxb.generated";
    private static final int MIN_BOARD_SIZE = 5;
    private static final int MAX_BOARD_SIZE = 20;
    private int boardSize;
    private eGameType gameType;
    private int minesPerPlayer;
    private BattleShipGame gameLoadedFromXml;
    private Map<String,BattleShipGame.ShipTypes.ShipType> shipTypes = new HashMap<>();

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

        // set shipTypes
        for (BattleShipGame.ShipTypes.ShipType shipType : objectImported.getShipTypes().getShipType()) {
            gameSettings.shipTypes.put(shipType.getId(),shipType);
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

    public Map<String, BattleShipGame.ShipTypes.ShipType> getShipTypes() {
        return shipTypes;
    }

    public BattleShipGame getGameLoadedFromXml() {
        return gameLoadedFromXml;
    }
}
