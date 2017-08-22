package GameLogic.Users;

import GameLogic.Exceptions.CellNotOnBoardException;
import GameLogic.Game.Board.Board;
import GameLogic.Game.Board.BoardCoordinates;
import GameLogic.Game.eAttackResult;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;

public class ComputerPlayer extends Player {
    private List<Point> optionalMoves = new LinkedList<>();
    private List<Point> criticalMoves = new LinkedList<>();
    private boolean[][] hitBoard;
    int boardSize;

    public ComputerPlayer(String playerID, String name) {
        super(playerID, name);
    }

    // ============================================ init ============================================
    public void initOptionalMoves() {
        initDiagonalWithTwoSpace(0, 0);
        initDiagonalWithTwoSpace(0, 1);
    }

    // assume that the input is: (0,0) OR (0,1)
    private void initDiagonalWithTwoSpace(int currRow, int currCol) {
        // if currCol == 0, lower half diagonal start in currRow = 2.
        // if currCol == 1, lower half diagonal start in currRow = 1.
        int startRowInLowerHalf = currCol == 0 ? 2 : 1;

        // upper half diagonal
        while (currCol < boardSize) {
            getDiagonalValue(currRow, currCol);
            currCol += 2;
        }
        currRow = startRowInLowerHalf;
        currCol = 0;
        // lower half diagonal
        while (currRow < boardSize) {
            getDiagonalValue(currRow, currCol);
            currRow += 2;
        }
    }

    private void getDiagonalValue(int currRow, int currCol) {
        while (currRow < boardSize && currCol < boardSize) {
            optionalMoves.add(new Point(currRow, currCol));
            currRow++;
            currCol++;
        }
    }

    // ============================================ setter ============================================
    public void setMyBoard(Board board) {
        this.myBoard = board;
        boardSize= board.getBoardSize();
        hitBoard = new boolean[ boardSize ][ boardSize ];
        initOptionalMoves();
    }

    // ============================================ Next Move ============================================
    public BoardCoordinates getNextMove(){
        Point movePoint=null;
        boolean cellNeverHit = false;

        movePoint = getBestMove();
        while(!cellNeverHit){
            movePoint = getBestMove();
            cellNeverHit = checkCellAlreadyHit(movePoint);
        }

        return pointToCoordinates(movePoint);
    }

    private Point getBestMove() {
        Point bestMove=null;
        if(!criticalMoves.isEmpty()){
            bestMove = getPoint(criticalMoves);
        }else if(!optionalMoves.isEmpty()){
            bestMove = getPoint(optionalMoves);
        }
        return bestMove;
    }

    private Point getPoint(List<Point> moveList) {
        Point movePoint;
        movePoint = moveList.get(0);
        if(movePoint != null){
            moveList.remove(0);
        }
        return movePoint;
    }

    private boolean checkCellAlreadyHit(Point movePoint) {
        return hitBoard[(int)movePoint.getX()][(int)movePoint.getY()];
    }

    // ============================================ Other methods ============================================
    public eAttackResult attack(BoardCoordinates position) throws CellNotOnBoardException {
        eAttackResult attackResult = super.attack(position);
        Point movePoint = BoardCoordinates.coordinatesToPoint(position);

        if(attackResult == eAttackResult.HIT_SHIP){
            hitShip(movePoint, false);
        }else if(attackResult == eAttackResult.HIT_AND_SUNK_SHIP){
            hitShip(movePoint, true);
        }
        hitBoard[(int)movePoint.getX()][(int)movePoint.getY()] = true;

        return attackResult;
    }

    private void hitShip(Point movePoint, boolean sunkTheShip) {
        // 1 2 3
        // 4 5 6
        // 7 8 9
        //
        // if hit 5:
        // add to critical move: 2 4 6 8
        // assign as hit: 1 3 7 9
        // use dudi logic in allSurroundingCellsClear in class Board
    }

    private BoardCoordinates pointToCoordinates(Point point){
        return BoardCoordinates.Parse((int)point.getX(), (int)point.getY());
    }
}