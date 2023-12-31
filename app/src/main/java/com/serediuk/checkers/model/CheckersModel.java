package com.serediuk.checkers.model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.serediuk.checkers.R;
import com.serediuk.checkers.enums.PieceRank;
import com.serediuk.checkers.enums.Player;
import com.serediuk.checkers.util.CheckersHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CheckersModel {
    private static final int DECK_SIZE = 8;
    private static final int ROWS_COUNT = 3;

    protected Player playerTurn;
    protected Player opponentTurn;
    protected ArrayList<CheckersPiece> pieces;
    protected ArrayList<BoardCell> takenPieces;
    protected ArrayList<BoardCell> lastMoves;
    protected CheckersPiece lastMovingPiece = null;
    protected boolean lastMoveWasTake = false;

    protected Player turn;

    public CheckersModel() {
        playerTurn = Player.WHITE;
        opponentTurn = Player.BLACK;
        turn = Player.WHITE;

        pieces = new ArrayList<>();
        takenPieces = new ArrayList<>();
        lastMoves = new ArrayList<>();
        initializePieces();
    }

    protected void initializePieces() {
        takenPieces.clear();
        lastMoves.clear();
        lastMovingPiece = null;
        lastMoveWasTake = false;
        turn = Player.WHITE;
        pieces.clear();

        for (int i = 0; i < ROWS_COUNT; i++)
            for (int j = 0; j < DECK_SIZE; j++)
                if ((i + j) % 2 == 1)
                    if (playerTurn == Player.WHITE)
                        pieces.add(new CheckersPiece(new BoardCell(i, j), opponentTurn, PieceRank.PAWN, R.drawable.black));
                    else
                        pieces.add(new CheckersPiece(new BoardCell(i, j), opponentTurn, PieceRank.PAWN, R.drawable.white));

        for (int i = DECK_SIZE - ROWS_COUNT; i < DECK_SIZE; i++)
            for (int j = 0; j < DECK_SIZE; j++)
                if ((i + j) % 2 == 1)
                    if (playerTurn == Player.WHITE)
                        pieces.add(new CheckersPiece(new BoardCell(i, j), playerTurn, PieceRank.PAWN, R.drawable.white));
                    else
                        pieces.add(new CheckersPiece(new BoardCell(i, j), playerTurn, PieceRank.PAWN, R.drawable.black));
    }

    public ArrayList<CheckersPiece> getPieces() {
        return pieces;
    }

    public int getWhiteCount() {
        int count = 0;
        for (CheckersPiece piece : pieces)
            if (piece.getPlayer() == Player.WHITE)
                count++;
        return count;
    }

    public int getBlackCount() {
        int count = 0;
        for (CheckersPiece piece : pieces)
            if (piece.getPlayer() == Player.BLACK)
                count++;
        return count;
    }

    public void changeTurn() {
        Player temp = playerTurn;
        playerTurn = opponentTurn;
        opponentTurn = temp;
        initializePieces();
    }

    public void restart() {
        initializePieces();
    }

    public boolean movePiece(BoardCell from, BoardCell to) {
        return movePiece(from.getRow(), from.getCol(), to.getRow(), to.getCol());
    }

    public boolean movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        Log.d("MOVE", "1");
        if ((toRow + toCol) % 2 == 0)
            return false;
        CheckersPiece pieceToMove = pieceAt(fromRow, fromCol);
        if (pieceToMove == null)
            return false;
        if (pieceToMove.getPlayer() != turn)
            return false;
        if (pieceAt(toRow, toCol) != null)
            return false;
        if (lastMovingPiece != null && lastMovingPiece != pieceToMove && lastMoveWasTake)
            return false;
        if (lastMovingPiece == null)
            takenPieces.clear();

        if (canBeTakenMove()) {
            return makeTakenMove(pieceToMove, toRow, toCol);
        }
        return makeMove(pieceToMove, toRow, toCol);
    }

    private void move(CheckersPiece pieceToMove, int toRow, int toCol) {
        if (!lastMoveWasTake) {
            lastMoves.clear();
        }
        lastMoves.add(pieceToMove.getPosition());
        lastMoves.add(new BoardCell(toRow, toCol));
        pieceToMove.setPosition(toRow, toCol);

        if (pieceToMove.getPlayer() == playerTurn)
            if (pieceToMove.getPosition().getRow() == 0)
                makeKing(pieceToMove);
        if (pieceToMove.getPlayer() == opponentTurn)
            if (pieceToMove.getPosition().getRow() == DECK_SIZE - 1)
                makeKing(pieceToMove);


        if (!lastMoveWasTake || lastMovingPiece == null) {
            turn = turn == Player.WHITE ? Player.BLACK : Player.WHITE;
            lastMovingPiece = null;
            lastMoveWasTake = false;
        } else {
            ArrayList<BoardCell> movesToTake = getMovesToTake(lastMovingPiece);
            if (movesToTake == null || movesToTake.size() == 0) {
                turn = turn == Player.WHITE ? Player.BLACK : Player.WHITE;
                lastMovingPiece = null;
                lastMoveWasTake = false;
            }
        }
    }

    private boolean makeTakenMove(CheckersPiece piece, int toRow, int toCol) {
        Log.d("MOVE", "2");
        ArrayList<BoardCell> movesToTake = getMovesToTake(piece);
        if (movesToTake == null || movesToTake.size() == 0)
            return false;

        if (containMove(movesToTake, new BoardCell(toRow, toCol))) {
            if (piece.getPieceRank() == PieceRank.PAWN) {
                int takeRow = (piece.getPosition().getRow() + toRow) / 2;
                int takeCol = (piece.getPosition().getCol() + toCol) / 2;
                CheckersPiece takenPiece = pieceAt(takeRow, takeCol);
                if (takenPiece.getPosition().getRow() != piece.getPosition().getRow()
                        && takenPiece.getPosition().getCol() != piece.getPosition().getCol()
                ) {
                    takenPieces.add(takenPiece.getPosition());
                    pieces.remove(takenPiece);
                    if (!lastMoveWasTake) {
                        lastMoves.clear();
                    }
                    lastMoveWasTake = true;
                    lastMovingPiece = piece;
                    move(piece, toRow, toCol);
                    Log.d("MOVE", "3");
                    return true;
                }
            }
            if (piece.getPieceRank() == PieceRank.KING) {
                int takeRow;
                int takeCol;
                int kingRow = piece.getPosition().getRow();
                int kingCol = piece.getPosition().getCol();
                if (kingRow > toRow)
                    takeRow = toRow + 1;
                else
                    takeRow = toRow - 1;
                if (kingCol > toCol)
                    takeCol = toCol + 1;
                else
                    takeCol = toCol - 1;
                CheckersPiece takenPiece = pieceAt(takeRow, takeCol);
                if (takenPiece.getPosition().getRow() != piece.getPosition().getRow()
                        && takenPiece.getPosition().getCol() != piece.getPosition().getCol()
                ) {
                    takenPieces.add(takenPiece.getPosition());
                    pieces.remove(takenPiece);
                    if (!lastMoveWasTake) {
                        lastMoves.clear();
                    }
                    lastMoveWasTake = true;
                    lastMovingPiece = piece;
                    move(piece, toRow, toCol);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean makeMove(CheckersPiece piece, int toRow, int toCol) {
        ArrayList<BoardCell> moves = getCorrectMovesForPiece(piece);
        if (moves == null || moves.size() == 0)
            return false;

        if (containMove(moves, new BoardCell(toRow, toCol))) {
            move(piece, toRow, toCol);
            lastMoveWasTake = false;
            lastMovingPiece = piece;
            return true;
        }
        return false;
    }

    public CheckersPiece pieceAt(BoardCell cell) {
        return pieceAt(cell.getRow(), cell.getCol());
    }

    public CheckersPiece pieceAt(int row, int col) {
        for (CheckersPiece piece : pieces) {
            if (piece.getPosition().getRow() == row && piece.getPosition().getCol() == col) {
                return piece;
            }
        }
        return null;
    }

    private void makeKing(CheckersPiece piece) {
        piece.setPieceRank(PieceRank.KING);
        piece.setImageId(
                piece.getPlayer() == Player.WHITE
                        ? R.drawable.white_king
                        : R.drawable.black_king
        );
    }

    public Player getTurn() {
        return turn;
    }

    public Player getPlayerTurn() {
        return playerTurn;
    }

    private boolean canBeTakenMove() {
        for (CheckersPiece piece : pieces) {
            if (piece.getPlayer() == turn) {
                ArrayList<BoardCell> moves = getMovesToTake(piece);
                if (moves != null && moves.size() > 0)
                    return true;
            }
        }
        return false;
    }

    private ArrayList<BoardCell> getMovesToTake(CheckersPiece piece) {
        if (turn != piece.getPlayer())
            return null;
        ArrayList<BoardCell> allMoves = piece.getAllPossibleMoves();
        ArrayList<BoardCell> movesToTake = new ArrayList<>();
        for (BoardCell move : allMoves) {
            if (pieceAt(move) == null) {
                if (piece.getPieceRank() == PieceRank.PAWN) {
                    if (CheckersHelper.isPawnTakeMove(pieces, piece, move)) {
                        movesToTake.add(move);
                    }
                }
                if (piece.getPieceRank() == PieceRank.KING) {
                    if (CheckersHelper.isKingTakeMove(pieces, piece, move)) {
                        movesToTake.add(move);
                    }
                }
            }
        }
        return movesToTake;
    }

    private ArrayList<BoardCell> getCorrectMovesForPiece(CheckersPiece piece) {
        if (turn != piece.getPlayer())
            return null;
        ArrayList<BoardCell> allMoves = piece.getAllPossibleMoves();
        ArrayList<BoardCell> correctMoves = new ArrayList<>();
        for (BoardCell move : allMoves) {
            if (pieceAt(move) == null) {
                if (piece.getPieceRank() == PieceRank.PAWN)
                    if (CheckersHelper.isPawnMove(pieces, piece, move, playerTurn))
                        correctMoves.add(move);
                if (piece.getPieceRank() == PieceRank.KING)
                    if (CheckersHelper.isKingMove(pieces, piece, move))
                        correctMoves.add(move);
            }
        }
        return correctMoves;
    }

    public boolean containMove(ArrayList<BoardCell> list, BoardCell cell) {
        if (list == null || list.size() == 0)
            return false;
        for (BoardCell l : list)
            if (l.getRow() == cell.getRow() && l.getCol() == cell.getCol())
                return true;
        return false;
    }

    public ArrayList<BoardCell> getTakenPieces() {
        return new ArrayList<>(takenPieces);
    }

    public ArrayList<BoardCell> getHighlightMovesForPiece(CheckersPiece piece) {
        if (canBeTakenMove())
            return getMovesToTake(piece);
        return getCorrectMovesForPiece(piece);
    }

    public ArrayList<BoardCell> getLastMoves() {
        return lastMoves;
    }

    public boolean checkTie() {
        for (CheckersPiece piece : pieces) {
            if (piece.getPlayer() == turn) {
                ArrayList<BoardCell> moves = getHighlightMovesForPiece(piece);
                if (moves != null && moves.size() > 0)
                    return false;
            }
        }
        return getWhiteCount() != 0 && getBlackCount() != 0;
    }

    @NonNull
    @Override
    public String toString() {
        String res = "";
        for (int i = 0; i < 8; i++) {
            String d = "";
            for (int j = 0; j < 8; j++) {
                if (pieceAt(i, j) != null) {
                    if (pieceAt(i, j).getPieceRank() == PieceRank.PAWN) {
                        d += pieceAt(i, j).getPlayer() == Player.WHITE ? " P" : " p";
                    }
                    if (pieceAt(i, j).getPieceRank() == PieceRank.KING) {
                        d += pieceAt(i, j).getPlayer() == Player.WHITE ? " K" : " k";
                    }
                } else {
                    d += " .";
                }
            }
            res += d + "\n";
        }
        return res;
    }
}
