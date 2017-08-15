package GameLogic;

import GameLogic.Game.Game;
import GameLogic.Game.GameSettings;
import GameLogic.Game.eGameState;
import GameLogic.Users.Player;
import javafx.fxml.LoadException;

import java.util.HashMap;
import java.util.Map;

public class GamesManager implements IGamesLogic {
    private Map<String, Player> allPlayers = new HashMap<>();
    private Map<Integer, Game> allGames = new HashMap<>();


    public void addPlayer(Player newPlayer) {
        allPlayers.put(newPlayer.getID(), newPlayer);
    }

    @Override
    public Game loadGameFile(String path) throws LoadException {
        GameSettings gameSettings = GameSettings.LoadGameFile(path);
        Game newGame = new Game(gameSettings);
        allGames.put(newGame.getID(), newGame);

        return newGame;
    }

    @Override
    public void startGame(Game gameToStart, Player player1, Player player2) throws Exception {
        gameToStart.initGame(player1, player2);
        gameToStart.setGameState(eGameState.STARTED);
    }
}
