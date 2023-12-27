package com.serediuk.checkers.model;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.serediuk.checkers.R;
import com.serediuk.checkers.model.emuns.PieceRank;
import com.serediuk.checkers.model.emuns.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class CheckersModel {
    private static CheckersModel instance;

    private static final int DECK_SIZE = 8;
    private static final int ROWS_COUNT = 3;

    private Player playerTurn;
    private Player opponentTurn;
    private final Set<CheckersPiece> pieces;
    private final Set<BoardCell> takenPieces;
    private final ArrayList<BoardCell> lastMoves;
    private CheckersPiece lastMovingPiece = null;
    private boolean lastMoveWasTake = false;

    private Player turn;

    public CheckersModel() {
        playerTurn = Player.WHITE;
        opponentTurn = Player.BLACK;
        turn = Player.WHITE;

        pieces = new HashSet<>();
        takenPieces = new HashSet<>();
        lastMoves = new ArrayList<>();
        initializePieces();
    }

    public static CheckersModel getInstance() {
        if (instance == null)
            instance = new CheckersModel();

        return instance;
    }

    private void initializePieces() {
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
        ArrayList<BoardCell> movesToTake = getMovesToTake(piece);
        if (movesToTake == null || movesToTake.size() == 0)
            return false;

        if (containMove(movesToTake, new BoardCell(toRow, toCol))) {
            if (piece.getPieceRank() == PieceRank.PAWN) {
                int takeRow = (int) (piece.getPosition().getRow() + toRow) / 2;
                int takeCol = (int) (piece.getPosition().getCol() + toCol) / 2;
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
                    if (isPawnTakeMove(piece, move)) {
                        movesToTake.add(move);
                    }
                }
                if (piece.getPieceRank() == PieceRank.KING) {
                    if (isKingTakeMove(piece, move)) {
                        movesToTake.add(move);
                    }
                }
            }
        }
        return movesToTake;
    }

    private boolean isPawnTakeMove(CheckersPiece pawn, BoardCell take) {
        int takeRow = (int) (pawn.getPosition().getRow() + take.getRow()) / 2;
        int takeCol = (int) (pawn.getPosition().getCol() + take.getCol()) / 2;
        if (takeRow == 0 || takeRow == DECK_SIZE - 1 || takeCol == 0 || takeCol == DECK_SIZE - 1)
            return false;
        CheckersPiece takePiece = pieceAt(takeRow, takeCol);
        if (takePiece == null)
            return false;
        return takePiece.getPlayer() != pawn.getPlayer();
    }

    private boolean isKingTakeMove(CheckersPiece king, BoardCell take) {
        int kingRow = king.getPosition().getRow();
        int kingCol = king.getPosition().getCol();
        int takeRow = take.getRow();
        int takeCol = take.getCol();
        if (takeRow == 0 || takeRow == DECK_SIZE - 1 || takeCol == 0 || takeCol == DECK_SIZE - 1)
            return false;
        if (Math.abs(kingRow - takeRow) == Math.abs(kingCol - takeCol)) {
            if (kingRow > takeRow) {
                if (kingCol > takeCol) {
                    CheckersPiece nextPiece = pieceAt(takeRow + 1, takeCol + 1);
                    if (nextPiece != null && nextPiece.getPlayer() != king.getPlayer()) {
                        for (int i = takeRow + 2; i < kingRow; i++) {
                            int j = takeCol + i - takeRow;
                            if (pieceAt(i, j) != null)
                                return false;
                        }
                        return true;
                    }
                } else {
                    CheckersPiece nextPiece = pieceAt(takeRow + 1, takeCol - 1);
                    if (nextPiece != null && nextPiece.getPlayer() != king.getPlayer()) {
                        for (int i = takeRow + 2; i < kingRow; i++) {
                            int j = takeCol - i + takeRow;
                            if (pieceAt(i, j) != null)
                                return false;
                        }
                        return true;
                    }
                }
            } else {
                if (kingCol > takeCol) {
                    CheckersPiece nextPiece = pieceAt(takeRow - 1, takeCol + 1);
                    if (nextPiece != null && nextPiece.getPlayer() != king.getPlayer()) {
                        for (int i = takeRow + 2; i < kingRow; i++) {
                            int j = takeCol + i - takeRow;
                            if (pieceAt(i, j) != null)
                                return false;
                        }
                        return true;
                    }
                } else {
                    CheckersPiece nextPiece = pieceAt(takeRow - 1, takeCol - 1);
                    if (nextPiece != null && nextPiece.getPlayer() != king.getPlayer()) {
                        for (int i = takeRow + 2; i < kingRow; i++) {
                            int j = takeCol - i + takeRow;
                            if (pieceAt(i, j) != null)
                                return false;
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private ArrayList<BoardCell> getCorrectMovesForPiece(CheckersPiece piece) {
        if (turn != piece.getPlayer())
            return null;
        ArrayList<BoardCell> allMoves = piece.getAllPossibleMoves();
        ArrayList<BoardCell> correctMoves = new ArrayList<>();
        for (BoardCell move : allMoves) {
            if (pieceAt(move) == null) {
                if (piece.getPieceRank() == PieceRank.PAWN)
                    if (isPawnMove(piece, move))
                        correctMoves.add(move);
                if (piece.getPieceRank() == PieceRank.KING)
                    if (isKingMove(piece, move))
                        correctMoves.add(move);
            }
        }
        return correctMoves;
    }

    private boolean isPawnMove(CheckersPiece piece, BoardCell move) {
        if (playerTurn == piece.getPlayer())
            return move.getRow() - piece.getPosition().getRow() == -1 && Math.abs(move.getCol() - piece.getPosition().getCol()) == 1;
        else
            return move.getRow() - piece.getPosition().getRow() == 1 && Math.abs(move.getCol() - piece.getPosition().getCol()) == 1;
    }

    private boolean isKingMove(CheckersPiece king, BoardCell move) {
        int kingRow = king.getPosition().getRow();
        int kingCol = king.getPosition().getCol();
        int moveRow = move.getRow();
        int moveCol = move.getCol();
        if (Math.abs(kingRow - moveRow) == Math.abs(kingCol - moveCol)) {
            if (kingRow > moveRow) {
                if (kingCol > moveCol) {
                    for (int i = moveRow + 1; i < kingRow; i++) {
                        int j = moveCol + i - moveRow;
                        if (pieceAt(i, j) != null)
                            return false;
                    }
                } else {
                    for (int i = moveRow + 1; i < kingRow; i++) {
                        int j = moveCol - i + moveRow;
                        if (pieceAt(i, j) != null)
                            return false;
                    }
                }
            } else {
                if (kingCol > moveCol) {
                    for (int i = moveRow + 1; i < kingRow; i++) {
                        int j = moveCol + i - moveRow;
                        if (pieceAt(i, j) != null)
                            return false;
                    }
                } else {
                    for (int i = moveRow + 1; i < kingRow; i++) {
                        int j = moveCol - i + moveRow;
                        if (pieceAt(i, j) != null)
                            return false;
                    }
                }
            }
            return true;
        }
        return false;
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
}
