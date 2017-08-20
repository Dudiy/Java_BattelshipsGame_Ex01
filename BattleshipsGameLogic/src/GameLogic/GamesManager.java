package GameLogic;

import GameLogic.Exceptions.CellNotOnBoardException;
import GameLogic.Exceptions.InvalidGameObjectPlacementException;
import GameLogic.Exceptions.NoMinesAvailableException;
import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.Game;
import GameLogic.Game.GameSettings;
import GameLogic.Game.eGameState;
import GameLogic.Users.Player;
import GameLogic.Game.eAttackResult;
import javafx.fxml.LoadException;

import java.io.*;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class GamesManager implements IGamesLogic, Serializable{
    private Map<String, Player> allPlayers = new HashMap<>();
    private Map<Integer, Game> allGames = new HashMap<>();
    private static final String SAVE_FILE_NAME = "SaveMe.dat";

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

    @Override
    public Duration getGameDuration(Game game) {
        return game.getTotalGameDuration();
    }

    @Override
    public void endGame(Game game) {
        game.endGame();
    }

    @Override
    public void plantMine(Game game, BoardCoordinates cell) throws CellNotOnBoardException, InvalidGameObjectPlacementException, NoMinesAvailableException {
        game.plantMineOnActivePlayersBoard(cell);
    }

/*
    private static void readPersonsFromFile() throws IOException, ClassNotFoundException {
        // Read the array list  from the file
        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(FILE_NAME))) {
            // we know that we read array list of Persons
            ArrayList<Person> survivorsFromFile =
                    (ArrayList<Person>) in.readObject();
            System.out.println("survivorsFromFile:"*//* +
                    survivorsFromFile*//*);
            for (Person person : survivorsFromFile){
                System.out.println(person);
            }
        }
    }*/

    public void writeToFile() throws IOException {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(SAVE_FILE_NAME))) {
            out.writeObject(this);
            out.flush();
        }
    }
}
