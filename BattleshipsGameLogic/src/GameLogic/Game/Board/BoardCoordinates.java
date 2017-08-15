package GameLogic.Game.Board;

public class BoardCoordinates {
    private char col;
    private int row;

    public BoardCoordinates(char col, int row) {
        this.col = col;
        this.row = row;
    }

    // convert a string of type "A1" or "1A" to coordinates
    public static BoardCoordinates Parse(String stringToParse) throws IllegalArgumentException {
        BoardCoordinates res;

        if (stringToParse.length() != 2) {
            throw new IllegalArgumentException("Input str must be exactly 2 characters long");
        } else {
            char firstCharInString = stringToParse.charAt(0);
            char secondCharInString = stringToParse.charAt(1);
            char upperChar;
            int digit;

            if (Character.isAlphabetic(firstCharInString) && Character.isDigit(secondCharInString)) {
                upperChar = Character.toUpperCase(firstCharInString);
                digit = secondCharInString - '0';
            } else if (Character.isDigit(firstCharInString) && Character.isAlphabetic(secondCharInString)) {
                upperChar = Character.toUpperCase(secondCharInString);
                digit = secondCharInString - '0';
            } else {
                throw new IllegalArgumentException("Input str must be a digit and a char (in any order)");
            }

            res = new BoardCoordinates(upperChar, digit);
        }

        return res;
    }

    public char GetCol() {
        return col;
    }

    public int GetColAsInt() {
        return col - 'A';
    }

    public int GetRow() {
        return row - 1;
    }

    // updates the value of row, does not check if the value is on the board!
    public void OffsetRow(int offset) {
        col += offset;
    }

    // updates the value of col, does not check if the value is on the board!
    public void OffsetCol(int offset){
        row += offset;
    }

    @Override
    public String toString() {
        return String.format("(%c,%d)", col, row);
    }
}
