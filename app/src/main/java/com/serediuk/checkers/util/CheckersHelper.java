package com.serediuk.checkers.util;

import com.serediuk.checkers.enums.Player;
import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersPiece;

import java.util.ArrayList;

public class CheckersHelper {
    private static final int DECK_SIZE = 8;

    public static boolean isPawnTakeMove(ArrayList<CheckersPiece> pieces, CheckersPiece pawn, BoardCell take) {
        int takeRow = (int) (pawn.getPosition().getRow() + take.getRow()) / 2;
        int takeCol = (int) (pawn.getPosition().getCol() + take.getCol()) / 2;
        if (takeRow == 0 || takeRow == DECK_SIZE - 1 || takeCol == 0 || takeCol == DECK_SIZE - 1)
            return false;
        CheckersPiece takePiece = pieceAt(pieces, takeRow, takeCol);
        if (takePiece == null)
            return false;
        return takePiece.getPlayer() != pawn.getPlayer();
    }

    public static boolean isKingTakeMove(ArrayList<CheckersPiece> pieces, CheckersPiece king, BoardCell take) {
        int kingRow = king.getPosition().getRow();
        int kingCol = king.getPosition().getCol();
        int takeRow = take.getRow();
        int takeCol = take.getCol();
        if (Math.abs(kingRow - takeRow) == Math.abs(kingCol - takeCol)) {
            if (kingRow > takeRow) {
                if (kingCol > takeCol) {
                    CheckersPiece nextPiece = pieceAt(pieces, takeRow + 1, takeCol + 1);
                    if (nextPiece != null && nextPiece.getPlayer() != king.getPlayer()) {
                        for (int i = takeRow + 2; i < kingRow; i++) {
                            int j = takeCol + i - takeRow;
                            if (pieceAt(pieces, i, j) != null)
                                return false;
                        }
                        return true;
                    }
                } else {
                    CheckersPiece nextPiece = pieceAt(pieces, takeRow + 1, takeCol - 1);
                    if (nextPiece != null && nextPiece.getPlayer() != king.getPlayer()) {
                        for (int i = takeRow + 2; i < kingRow; i++) {
                            int j = takeCol - i + takeRow;
                            if (pieceAt(pieces, i, j) != null)
                                return false;
                        }
                        return true;
                    }
                }
            } else {
                if (kingCol > takeCol) {
                    CheckersPiece nextPiece = pieceAt(pieces, takeRow - 1, takeCol + 1);
                    if (nextPiece != null && nextPiece.getPlayer() != king.getPlayer()) {
                        for (int i = takeRow - 2; i > kingRow; i--) {
                            int j = takeCol - i + takeRow;
                            if (pieceAt(pieces, i, j) != null)
                                return false;
                        }
                        return true;
                    }
                } else {
                    CheckersPiece nextPiece = pieceAt(pieces, takeRow - 1, takeCol - 1);
                    if (nextPiece != null && nextPiece.getPlayer() != king.getPlayer()) {
                        for (int i = takeRow - 2; i > kingRow; i--) {
                            int j = takeCol + i - takeRow;
                            if (pieceAt(pieces, i, j) != null)
                                return false;
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isPawnMove(ArrayList<CheckersPiece> pieces, CheckersPiece piece, BoardCell move, Player playerTurn) {
        if (pieceAt(pieces, move) != null)
            return false;
        if (playerTurn == piece.getPlayer())
            return move.getRow() - piece.getPosition().getRow() == -1 && Math.abs(move.getCol() - piece.getPosition().getCol()) == 1;
        else
            return move.getRow() - piece.getPosition().getRow() == 1 && Math.abs(move.getCol() - piece.getPosition().getCol()) == 1;
    }

    public static boolean isKingMove(ArrayList<CheckersPiece> pieces, CheckersPiece king, BoardCell move) {
        int kingRow = king.getPosition().getRow();
        int kingCol = king.getPosition().getCol();
        int moveRow = move.getRow();
        int moveCol = move.getCol();
        if (Math.abs(kingRow - moveRow) == Math.abs(kingCol - moveCol)) {
            if (kingRow > moveRow) {
                if (kingCol > moveCol) {
                    for (int i = moveRow + 1; i < kingRow; i++) {
                        int j = moveCol + i - moveRow;
                        if (pieceAt(pieces, i, j) != null)
                            return false;
                    }
                } else {
                    for (int i = moveRow + 1; i < kingRow; i++) {
                        int j = moveCol - i + moveRow;
                        if (pieceAt(pieces, i, j) != null)
                            return false;
                    }
                }
            } else {
                if (kingCol > moveCol) {
                    for (int i = moveRow - 1; i > kingRow; i--) {
                        int j = moveCol - i + moveRow;
                        if (pieceAt(pieces, i, j) != null)
                            return false;
                    }
                } else {
                    for (int i = moveRow - 1; i > kingRow; i--) {
                        int j = moveCol + i - moveRow;
                        if (pieceAt(pieces, i, j) != null)
                            return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    public static CheckersPiece pieceAt(ArrayList<CheckersPiece> pieces, BoardCell cell) {
        return pieceAt(pieces, cell.getRow(), cell.getCol());
    }

    public static CheckersPiece pieceAt(ArrayList<CheckersPiece> pieces, int row, int col) {
        for (CheckersPiece piece : pieces) {
            if (piece.getPosition().getRow() == row && piece.getPosition().getCol() == col) {
                return piece;
            }
        }
        return null;
    }
}
