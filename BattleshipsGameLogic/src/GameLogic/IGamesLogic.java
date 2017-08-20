package GameLogic;

import GameLogic.Exceptions.CellNotOnBoardException;
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
}
