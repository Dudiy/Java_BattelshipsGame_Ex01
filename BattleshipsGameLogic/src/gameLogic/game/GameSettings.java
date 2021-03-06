package gameLogic.game;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import javafx.fxml.LoadException;
import jaxb.generated.BattleShipGame;

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

    public int getMinimalShipSize() {
        //there must be at least a ship that is longer than 1
        int minimalSize = shipTypes.values().iterator().next().getLength();

        for (BattleShipGame.ShipTypes.ShipType shipType : shipTypes.values()) {
            if (shipType.getLength() < minimalSize) {
                minimalSize = shipType.getLength();
            }
        }

        return minimalSize;
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
        validateBoardSize(gameSettings);
        setShipType(gameSettings);
    }

    private static void validateBoardSize(GameSettings gameSettings) throws Exception {
        BattleShipGame objectImported = gameSettings.gameLoadedFromXml;
        gameSettings.boardSize = objectImported.getBoardSize();
        if (gameSettings.boardSize < MIN_BOARD_SIZE || gameSettings.boardSize > MAX_BOARD_SIZE) {
            throw new Exception("Invalid board size, the size must be between " + MIN_BOARD_SIZE + " and " + MAX_BOARD_SIZE);
        }
    }

    private static void setShipType(GameSettings gameSettings) throws Exception {
        BattleShipGame objectImported = gameSettings.gameLoadedFromXml;
        List<BattleShipGame.ShipTypes.ShipType> shipTypeList = objectImported.getShipTypes().getShipType();
        if (shipTypeList.isEmpty()) {
            throw new Exception("There is no ship type in file");
        }
        for (BattleShipGame.ShipTypes.ShipType shipType : objectImported.getShipTypes().getShipType()) {
            if (shipType.getLength() <= 0) {
                throw new Exception("ship type \"" + shipType.getId() + "\" has a negative length");
            }
            if (shipType.getScore() <= 0) {
                throw new Exception("ship type \"" + shipType.getId() + "\" has a negative score");
            }
            if (gameSettings.shipTypes.containsKey(shipType.getId())) {
                throw new Exception("ship type with the ID \"" + shipType.getId() + "\" exists more than once");
            }
            gameSettings.shipTypes.put(shipType.getId(), shipType);
            gameSettings.numShipsPerBoard.put(shipType, shipType.getAmount());
        }
    }

    private static BattleShipGame deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (BattleShipGame) u.unmarshal(in);
    }
}
