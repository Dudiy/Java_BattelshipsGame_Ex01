package GameLogic;

import GameLogic.Game.Game;
import GameLogic.Users.Player;
import javafx.fxml.LoadException;

public interface IGamesLogic {
    Game loadGameFile(String path) throws LoadException;
    void startGame(Game gameToStart, Player player1, Player player2) throws Exception;
}
