package com.serediuk.checkers.model;

import android.util.Log;

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

    private Player turn;

    public CheckersModel() {
        playerTurn = Player.WHITE;
        opponentTurn = Player.BLACK;
        turn = Player.WHITE;

        pieces = new HashSet<>();
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
        if (pieceAt(toRow, toCol) != null) {
            return false;
        }

        if (containMove(getCorrectMovesForPiece(pieceToMove), new BoardCell(toRow, toCol))) {
            pieceToMove.setPosition(toRow, toCol);
            // change if has no more moves;
            turn = turn == Player.WHITE ? Player.BLACK : Player.WHITE;

            if (pieceToMove.getPlayer() == playerTurn)
                if (pieceToMove.getPosition().getRow() == 0)
                    makeKing(pieceToMove);
            if (pieceToMove.getPlayer() == opponentTurn)
                if (pieceToMove.getPosition().getRow() == DECK_SIZE - 1)
                    makeKing(pieceToMove);
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
                        : R.drawable.black_king);
    }

    public Player getTurn() {
        return turn;
    }

    public ArrayList<BoardCell> getCorrectMovesForPiece(CheckersPiece piece) {
        if (turn != piece.getPlayer())
            return null;
        ArrayList<BoardCell> allMoves = piece.getAllPossibleMoves();
        ArrayList<BoardCell> correctMoves = new ArrayList<>();
        for (BoardCell move : allMoves) {
            if (pieceAt(move) != null) {
                continue;
            }
            correctMoves.add(move);
        }
        return correctMoves;
    }

    public boolean containMove(ArrayList<BoardCell> list, BoardCell cell) {
        if (list == null || list.size() == 0)
            return false;
        for (BoardCell l : list) {
            if (l.getRow() == cell.getRow() && l.getCol() == cell.getCol())
                return true;
        }
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder deck = new StringBuilder();
        for (int i = 0; i < DECK_SIZE; i++) {
            deck.append(DECK_SIZE - i);
            for (int j = 0; j < DECK_SIZE; j++) {
                CheckersPiece piece = pieceAt(i, j);
                if (piece == null)
                    deck.append(" .");
                else {
                    if (piece.getPieceRank() == PieceRank.KING)
                        deck.append(piece.getPlayer() == Player.WHITE ? " K" : " k");
                    else
                        deck.append(piece.getPlayer() == Player.WHITE ? " P" : " p");
                }
            }
            deck.append("\n");
        }
        deck.append("  A B C D E F G H");
        return deck.toString();
    }
}
