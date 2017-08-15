package GameLogic.Game;

public class Game {
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
