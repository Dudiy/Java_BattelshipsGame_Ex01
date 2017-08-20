package GameLogic.Exceptions;

public class NoMinesAvailableException extends Exception {

    public NoMinesAvailableException() {
        super("No mines available");
    }
}
