package GameLogic.Game;

// TODO all class in logic not public ?
public enum eGameType {
    BASIC("Basic"),
    ADVANCED("Advanced");

    private String name;

    eGameType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
