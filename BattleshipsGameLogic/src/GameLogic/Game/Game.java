package GameLogic.Game;

public class Game {
    private final static String JAXB_XML_GAME_PACKAGE_NAME = "GameLogic.jaxb.schema.generated";

    public class GameSettings {
        private String gameFilePath;
        private int boardSize;
        private eGameType gameType;
    }

    GameSettings gameSettings;

    public Game(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }
}
