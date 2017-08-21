package GameLogic.Game;

import javafx.fxml.LoadException;
import jaxb.generated.BattleShipGame;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameSettings implements Serializable {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "jaxb.generated";
    public final static String SAVED_GAME_EXTENSION = ".dat";
    public final static String SAVED_GAME_DIR = "Saved Games/";
    private static final int MIN_BOARD_SIZE = 5;
    private static final int MAX_BOARD_SIZE = 20;
    private int boardSize;
    private int minesPerPlayer;
    private eGameType gameType;
    private transient BattleShipGame gameLoadedFromXml;
    private transient Map<String, BattleShipGame.ShipTypes.ShipType> shipTypes = new HashMap<>();
    private transient Map<BattleShipGame.ShipTypes.ShipType, Integer> numShipsPerBoard = new HashMap<>();

    // private ctor, GameSettings can only be created by calling LoadGameFile
    private GameSettings() {
        this.boardSize = 0;
        this.minesPerPlayer = 0;
        this.gameType = eGameType.BASIC;
        this.gameLoadedFromXml = null;
    }

    // ======================================= getters =======================================
    public int getBoardSize() {
        return boardSize;
    }

    public int getMinesPerPlayer() {
        return minesPerPlayer;
    }

    public eGameType getGameType() {
        return gameType;
    }

    public BattleShipGame getGameLoadedFromXml() {
        return gameLoadedFromXml;
    }

    // TODO del ?
    public Map<String, BattleShipGame.ShipTypes.ShipType> getShipTypes() {
        return shipTypes;
    }

    public Map<String, Integer> getShipTypesAmount() {
        Map<String, Integer> shipTypesAmount = new HashMap<>();

        for (Map.Entry<String, BattleShipGame.ShipTypes.ShipType> shipType : shipTypes.entrySet()) {
            shipTypesAmount.put(shipType.getKey(), shipType.getValue().getAmount());
        }

        return shipTypesAmount;
    }

    // ======================================= file methods =======================================
    // creates a new GameSettings object from xml file, with validation
    // assume: file exist, file is XML
    public static GameSettings loadGameFile(String gameFilePath) throws LoadException {
        GameSettings gameSettings = new GameSettings();

        try {
            InputStream fileInputStream = new FileInputStream(gameFilePath);
            gameSettings.gameLoadedFromXml = deserializeFrom(fileInputStream);
            validateGameSettings(gameSettings);
        } catch (JAXBException e) {
            throw new LoadException("Error loading xml file - JAXB error");
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
            throw new Exception("Invalid board size, the size must be between " + MIN_BOARD_SIZE + " and " + MAX_BOARD_SIZE);
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
            gameSettings.shipTypes.put(shipType.getId(), shipType);
            gameSettings.numShipsPerBoard.put(shipType, shipType.getAmount());
        }
    }

    private static BattleShipGame deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (BattleShipGame) u.unmarshal(in);
    }

    // TODO delete?
    public void shipAddedToBoard(BattleShipGame.ShipTypes.ShipType shipType) throws Exception {
        int shipsRemainingToAdd = numShipsPerBoard.get(shipType);
        if (shipsRemainingToAdd <= 0) {
            throw new Exception("Cannot add more ships of type " + shipType.getId() + " to the board");
        } else {
            numShipsPerBoard.put(shipType, numShipsPerBoard.get(shipType) - 1);
        }
    }
}
