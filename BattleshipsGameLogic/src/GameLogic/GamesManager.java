package GameLogic;

import GameLogic.Exceptions.CellNotOnBoardException;
import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.Game;
import GameLogic.Game.GameSettings;
import GameLogic.Game.eGameState;
import GameLogic.Users.Player;
import GameLogic.Game.eAttackResult;
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
        GameSettings gameSettings = GameSettings.loadGameFile(path);
        Game newGame = new Game(gameSettings);
        allGames.put(newGame.getID(), newGame);

        return newGame;
    }

    @Override
    public void startGame(Game gameToStart, Player player1, Player player2) throws Exception {
        gameToStart.initGame(player1, player2);
        gameToStart.setGameState(eGameState.STARTED);
    }

    @Override
    public eAttackResult makeMove(Game game, BoardCoordinates cellToAttack) throws CellNotOnBoardException {
        return game.attack(cellToAttack);
    }
}
