package GameLogic;

import GameLogic.Exceptions.CellNotOnBoardException;
import GameLogic.Exceptions.InvalidGameObjectPlacementException;
import GameLogic.Exceptions.NoMinesAvailableException;
import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.Game;
import GameLogic.Users.Player;
import GameLogic.Game.eAttackResult;
import javafx.fxml.LoadException;

import java.time.Duration;

public interface IGamesLogic {
    Game loadGameFile(String path) throws LoadException;
    void startGame(Game gameToStart, Player player1, Player player2) throws Exception;
    eAttackResult makeMove(Game game, BoardCoordinates cellToAttack) throws CellNotOnBoardException;
    Duration getGameDuration(Game game);
    void plantMine(Game game, BoardCoordinates cell) throws CellNotOnBoardException, InvalidGameObjectPlacementException, NoMinesAvailableException;
    void saveGameToFile(Game game, final String fileName) throws Exception;
    Game loadSavedGameFromFile(final String fileName) throws Exception;
    void endGame(Game game);
}
