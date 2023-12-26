package com.serediuk.checkers.model;

import androidx.annotation.NonNull;

import com.serediuk.checkers.R;
import com.serediuk.checkers.model.emuns.PieceRank;
import com.serediuk.checkers.model.emuns.Player;

import java.util.HashSet;
import java.util.Set;

public class CheckersModel {
    private static final int DECK_SIZE = 8;
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
        for (int i = DECK_SIZE - 1; i >= DECK_SIZE - ROWS_COUNT; i--)
            for (int j = 0; j < DECK_SIZE; j++)
                if ((i + j) % 2 == 0)
                    if (playerTurn == Player.WHITE)
                        pieces.add(new CheckersPiece(i, j, opponentTurn, PieceRank.PAWN, R.drawable.black));
                    else
                        pieces.add(new CheckersPiece(i, j, opponentTurn, PieceRank.PAWN, R.drawable.white));



        for (int i = 0; i < ROWS_COUNT; i++)
            for (int j = 0; j < DECK_SIZE; j++)
                if ((i + j) % 2 == 0)
                    if (playerTurn == Player.WHITE)
                        pieces.add(new CheckersPiece(i, j, playerTurn, PieceRank.PAWN, R.drawable.white));
                    else
                        pieces.add(new CheckersPiece(i, j, playerTurn, PieceRank.PAWN, R.drawable.black));
    }

    public void changeTurn() {
        Player temp = playerTurn;
        playerTurn = opponentTurn;
        opponentTurn = temp;
        initializePieces();
    }

    public CheckersPiece pieceAt(int row, int col) {
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
        String deck = "";
        for (int i = 0; i < DECK_SIZE; i++) {
            deck += DECK_SIZE - i;
            for (int j = 0; j < DECK_SIZE; j++) {
                CheckersPiece piece = pieceAt(i, j);
                if (piece == null)
                    deck += " .";
                else {
                    if (piece.getPieceRank() == PieceRank.KING)
                        deck += piece.getPlayer() == Player.WHITE ? " K" : " k";
                    else
                        deck += piece.getPlayer() == Player.WHITE ? " P" : " p";
                }
            }
            deck += "\n";
        }
        deck += "  A B C D E F G H";
        return deck;
    }
}
