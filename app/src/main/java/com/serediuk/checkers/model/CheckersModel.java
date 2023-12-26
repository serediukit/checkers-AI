package com.serediuk.checkers.model;

import androidx.annotation.NonNull;

import com.serediuk.checkers.model.emuns.PieceRank;
import com.serediuk.checkers.model.emuns.Player;

import java.util.HashSet;
import java.util.Set;

public class CheckersModel {
    private static final int DESC_SIZE = 8;
    private static final int ROWS_COUNT = 3;

    private Player playerTurn;
    private Player opponentTurn;
    private Set<CheckersPiece> pieces;

    public CheckersModel() {
        playerTurn = Player.WHITE;
        opponentTurn = Player.BLACK;

        pieces = new HashSet<>();
        initializePieces();
    }

    private void initializePieces() {
        pieces.clear();
        for (int i = 0; i < ROWS_COUNT; i++)
            for (int j = 0; j < DESC_SIZE; j++)
                if ((i + j) % 2 == 1)
                    pieces.add(new CheckersPiece(i, j, opponentTurn, PieceRank.PAWN));

        for (int i = DESC_SIZE - 1; i >= DESC_SIZE - ROWS_COUNT; i--)
            for (int j = 0; j < DESC_SIZE; j++)
                if ((i + j) % 2 == 1)
                    pieces.add(new CheckersPiece(i, j, playerTurn, PieceRank.PAWN));
    }

    public void changeTurn() {
        Player temp = playerTurn;
        playerTurn = opponentTurn;
        opponentTurn = temp;
        initializePieces();
    }

    private CheckersPiece pieceAt(int row, int col) {
        for (CheckersPiece piece : pieces) {
            if (piece.getRow() == row && piece.getCol() == col) {
                return piece;
            }
        }
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        String desc = "";
        for (int i = 0; i < DESC_SIZE; i++) {
            desc += (char) ('H' - i);
            for (int j = 0; j < DESC_SIZE; j++) {
                CheckersPiece piece = pieceAt(i, j);
                if (piece == null)
                    desc += " .";
                else {
                    if (piece.getPieceRank() == PieceRank.KING)
                        desc += piece.getPlayer() == Player.WHITE ? " K" : " k";
                    else
                        desc += piece.getPlayer() == Player.WHITE ? " P" : " p";
                }
            }
            desc += "\n";
        }
        desc += "  1 2 3 4 5 6 7 8";
        return desc;
    }
}
