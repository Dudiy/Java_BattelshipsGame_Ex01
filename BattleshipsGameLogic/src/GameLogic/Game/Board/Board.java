package GameLogic.Game.Board;

import GameLogic.Exceptions.*;
import GameLogic.Game.GameObjects.GameObject;
import GameLogic.Game.GameObjects.Mine;
import GameLogic.Game.GameObjects.Ship.*;
import GameLogic.Game.GameObjects.Water;
import GameLogic.Game.eAttackResult;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.Serializable;
import java.util.LinkedList;

public class Board implements Cloneable, Serializable {
    private BoardCell[][] board;
    private final int boardSize;
    private LinkedList<AbstractShip> shipsOnBoard = new LinkedList<>();
    private int minesAvailable;

    public Board(int boardSize) {
        this.boardSize = boardSize;
        initBoard();
    }

    private void initBoard() {
        board = new BoardCell[boardSize][boardSize];
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                //ctor of new BoardCell points to a new "Water" object by default
                board[row][col] = new BoardCell((char) ('A' + col), row + 1);
            }
        }
    }

    // ======================================= setters =======================================

    // set the value of the BoardCell at the given coordinates to be value
    private void setCellValue(BoardCoordinates position, GameObject value) throws InvalidGameObjectPlacementException, CloneNotSupportedException {
        try {
            BoardCell cell = getBoardCellAtCoordinates(position);
            if (value instanceof AbstractShip && !allSurroundingCellsClear(cell, value)) {
                throw new InvalidGameObjectPlacementException(value.getObjectTypeSimpleName(), position, "Surrounding cells are not clear.");
            } else {
                cell.SetCellValue(value);
            }
        } catch (CellNotOnBoardException cellNotOnBoardException) {
            throw new InvalidGameObjectPlacementException(value.getObjectTypeSimpleName(), position, "cannot place object on the cell because the cell is not on the board");
        }
    }

    // throws InvalidGameObjectPlacementException if value cannot be placed in the given coordinates on this board
    public void setMinesAvailable(int minesAvailable) {
        this.minesAvailable = minesAvailable;
    }

    // ======================================= getters =======================================
    public int getMinesAvailable() {
        return minesAvailable;
    }

    // TODO hide
    public BoardCell[][] getBoard() {
        return board;
    }

    public int getBoardSize() {
        return boardSize;
    }

    private BoardCell getCellByOffset(BoardCell srcCell, eBoardDirection direction, int offset) throws CellNotOnBoardException {
        BoardCell res;
        BoardCoordinates requiredCoordinates = srcCell.getPosition();

        switch (direction) {
            case DOWN:
                requiredCoordinates.OffsetRow(offset);
                break;
            case UP:
                requiredCoordinates.OffsetRow(-offset);
                break;
            case RIGHT:
                requiredCoordinates.offsetCol(offset);
                break;
            case LEFT:
                requiredCoordinates.offsetCol(-offset);
                break;
            default:
                throw new IllegalArgumentException("Invalid direction entered");
        }

        return getBoardCellAtCoordinates(requiredCoordinates);
    }

    // return a the BoardCell object on this board that is at the given coordinates
    public BoardCell getBoardCellAtCoordinates(BoardCoordinates coordinates) throws CellNotOnBoardException {
        BoardCell res;

        if (coordinatesAreOnBoard(coordinates)) {
            int col = coordinates.getColAsInt();
            int row = coordinates.getRow();
            res = board[row][col];
        } else {
            throw new CellNotOnBoardException();
        }

        return res;
    }

    // ======================================= methods =======================================
    public boolean allSurroundingCellsClear(BoardCell cell, GameObject objectBeingCheckedFor) {
        BoardCoordinates tempPosition = new BoardCoordinates(cell.getPosition());
        boolean allClear = true;

        //start from top left cell
        tempPosition.OffsetRow(-1);
        tempPosition.offsetCol(-1);
        for (int i = 0; i < 8; i++) {
            try {
                GameObject objectAtCell = this.getBoardCellAtCoordinates(tempPosition).getCellValue();
                if (!(objectAtCell instanceof Water) && (objectAtCell != objectBeingCheckedFor)) {
                    allClear = false;
                    break;
                }
            } catch (CellNotOnBoardException e) {
                // the given cell is on one of the edges, error while trying to fetch a surrounding cell not on the board
            } finally {
                // move 2 right
                if (i < 2) {
                    tempPosition.offsetCol(1);
                }
                // move 2 down
                else if (i < 4) {
                    tempPosition.OffsetRow(1);
                }
                // move 2 left
                else if (i < 6) {
                    tempPosition.offsetCol(-1);
                }
                // move 2 up
                else {
                    tempPosition.OffsetRow(-1);
                }
            }
        }

        return allClear;
    }

    // add a new ship to the board
    public void addShipToBoard(AbstractShip ship) throws Exception {
        if (ship instanceof RegularShip) {
            addRegularShipToBoard((RegularShip) ship);
        } else if (ship instanceof LShapeShip) {
            addLShapeShipToBoard((LShapeShip) ship);
        } else {
            throw new IllegalArgumentException("The ship type given is not supported");
        }
        // if we get here then there was no exception thrown => ship was added
        shipsOnBoard.add(ship);
    }

    public void addMineToBoard(BoardCoordinates position) throws CellNotOnBoardException, InvalidGameObjectPlacementException {
        if (minesAvailable > 0) {
            BoardCell boardCell = getBoardCellAtCoordinates(position);
            if (boardCell.wasAttacked()) {
                throw new InvalidGameObjectPlacementException(Mine.getObjectTypeSimpleName(), position, "Cannot place mine on a cell that was attacked");
            } else if (boardCell.getCellValue() instanceof Water) {
                boardCell.SetCellValue(new Mine(position));
                minesAvailable--;
            }
        } else {
            throw new InvalidGameObjectPlacementException(Mine.getObjectTypeSimpleName(), position, "No mines available");
        }
    }

    // add a new RegularShip to this board
    private void addRegularShipToBoard(RegularShip ship) throws Exception {
        BoardCoordinates currCoordinates = ship.getPosition();
        RegularShip.eShipDirection shipDirection = (RegularShip.eShipDirection) ship.getDirection();

        // set the value of the first cell
        setCellValue(currCoordinates, ship);
        // set the values of the next size-1 cells
        for (int i = 0; i < ship.getLength() - 1; i++) {
            if (shipDirection == RegularShip.eShipDirection.COLUMN) {
                currCoordinates.OffsetRow(1);
            } else if (shipDirection == RegularShip.eShipDirection.ROW) {
                currCoordinates.offsetCol(1);
            } else {
                throw new IllegalArgumentException("The given Ship has an unknown direction value");
            }
            setCellValue(currCoordinates, ship);
        }
    }

    // add a new LShapeShip to this board
    private void addLShapeShipToBoard(LShapeShip Ship) {
        //TODO implement
        throw new NotImplementedException();
    }

    // check if the given coordinates are on this board
    private boolean coordinatesAreOnBoard(BoardCoordinates i_Coordinates) {
        int col = i_Coordinates.getColAsInt();
        int row = i_Coordinates.getRow();
        return ((0 <= col && col <= boardSize - 1) && (0 <= row && row <= boardSize - 1));
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Board copiedBoard = new Board(boardSize);

        for (BoardCell[] row : board) {
            for (BoardCell cell : row) {
                BoardCoordinates position = cell.getPosition();
                try {
                    copiedBoard.setCellValue(position, (GameObject) cell.getCellValue().clone());
                } catch (Exception e) {
                    throw new CloneNotSupportedException("Error while cloning board");
                }
            }
        }

        return copiedBoard;
    }

    public eAttackResult attack(BoardCoordinates coordinatesToAttack) throws CellNotOnBoardException {
        return getBoardCellAtCoordinates(coordinatesToAttack).attack();
    }

    public void minePlanted() {
        minesAvailable--;
    }
}
