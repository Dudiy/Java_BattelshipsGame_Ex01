package GameLogic.Game;

public enum eGameState {
    INVALID("Game not loaded from xml file"),
    LOADED("Game successfully loaded from xml file"),
    INITIALIZED("Game loaded and initialized"),
    STARTED("Game Started");

    private String name;

    eGameState(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
