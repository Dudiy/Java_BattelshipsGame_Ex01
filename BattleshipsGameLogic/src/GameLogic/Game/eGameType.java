package GameLogic.Game;

public enum eGameType {
    BASIC("Basic"),
    ADVANCE("Advanced");

    private String name;

    eGameType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
