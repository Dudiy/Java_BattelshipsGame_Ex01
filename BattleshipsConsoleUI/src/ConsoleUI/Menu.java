package ConsoleUI;

public class Menu {

    public enum eMenuOptions{
        LOAD_GAME("Load game"),
        START_GAME("Start game");

        private String name;

        eMenuOptions(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    public eMenuOptions display(){
        System.out.println("Displaying menu");

        return eMenuOptions.LOAD_GAME;
    }
}
