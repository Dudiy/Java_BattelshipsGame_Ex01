package GameLogic.Game.Board;

import GameLogic.Exceptions.*;
import GameLogic.Game.GameObjects.GameObject;
import GameLogic.Game.GameObjects.Ship.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.LinkedList;

public class Board {
    private BoardCell[][] board;
    private final int boardSize;
    private LinkedList<AbstractShip> shipsOnBoard = new LinkedList<>();
    private int minesAvailable;

    public Board(int boardSize) {
        this.boardSize = boardSize;
        initBoard();
    }

    private void initBoard() {
        board = new BoardCell[ boardSize ][ boardSize ];

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                //ctor of new BoardCell points to a new "Water" object by default
                board[ row ][ col ] = new BoardCell((char) ('A' + col), row + 1);
            }
        }
    }

    // set the value of the BoardCell at the given coordinates to be value
    // throws InvalidGameObjectPlacementException if value cannot be placed in the given coordinates on this board
    private void setCellValue(BoardCoordinates position, GameObject value) throws InvalidGameObjectPlacementException {
        try {
            getBoardCellAtCoordinates(position).SetCellValue(value);
        } catch (CellNotOnBoardException cellNotOnBoardException) {
            throw new InvalidGameObjectPlacementException(position);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setMinesAvailable(int minesAvailable) {
        this.minesAvailable = minesAvailable;
    }

    public int getBoardSize() {
        return boardSize;
    }

    private BoardCell getCellByOffset(BoardCell srcCell, eDirection direction, int offset) throws CellNotOnBoardException {
        BoardCell res;
        BoardCoordinates requiredCoordinates = srcCell.GetPosition();

        switch (direction) {
            case DOWN:
                requiredCoordinates.OffsetRow(offset);
                break;
            case UP:
                requiredCoordinates.OffsetRow(-offset);
                break;
            case RIGHT:
                requiredCoordinates.OffsetCol(offset);
                break;
            case LEFT:
                requiredCoordinates.OffsetCol(-offset);
                break;
            default:
                throw new IllegalArgumentException("Invalid direction entered");
        }

        return getBoardCellAtCoordinates(requiredCoordinates);
    }

    // return a the BoardCell object on this board that is at the given coordinates
    private BoardCell getBoardCellAtCoordinates(BoardCoordinates coordinates) throws CellNotOnBoardException {
        BoardCell res;
        if (coordinatesAreOnBoard(coordinates)) {
            int col = coordinates.GetColAsInt();
            int row = coordinates.GetRow();

            res = board[ row ][ col ];
        } else {
            throw new CellNotOnBoardException();
        }

        return res;
    }

    // add a new ship to the board
    public void addShipToBoard(AbstractShip ship) throws InvalidShipPlacementException {
        try {
            if (ship instanceof RegularShip) {
                addRegularShipToBoard((RegularShip) ship);
            } else if (ship instanceof LShapeShip) {
                addLShapeShipToBoard((LShapeShip) ship);
            } else {
                throw new IllegalArgumentException("The ship type given is not supported");
            }
        } catch (Exception ex) {
            // runtime exception
            throw new InvalidShipPlacementException(ship.getCoordinates());
        }

        // if we get here then there was no exception thrown => ship was added
        shipsOnBoard.add(ship);
    }

    // add a new RegularShip to this board
    private void addRegularShipToBoard(RegularShip ship) throws InvalidGameObjectPlacementException {
        BoardCoordinates currCoordinates = ship.getCoordinates();
        RegularShip.eShipDirection shipDirection = (RegularShip.eShipDirection)ship.getDirection();

        // set the value of the first cell
        setCellValue(currCoordinates, ship);
        // set the values of the next size-1 cells
        for (int i = 0; i < ship.getLength() - 1; i++) {
            if (shipDirection == RegularShip.eShipDirection.COLUMN) {
                currCoordinates.OffsetCol(1);
            } else if (shipDirection == RegularShip.eShipDirection.ROW) {
                currCoordinates.OffsetRow(1);
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
        int col = i_Coordinates.GetColAsInt();
        int row = i_Coordinates.GetRow();

        return ((0 <= col && col <= boardSize - 1) && (0 <= row && row <= boardSize - 1));
    }
}
