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
    private CheckersPiece lastMovingPiece = null;

    private Player turn;

    public CheckersModel() {
        playerTurn = Player.WHITE;
        opponentTurn = Player.BLACK;
        turn = Player.WHITE;

        pieces = new HashSet<>();
        takenPieces = new HashSet<>();
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

        ArrayList<Pair<BoardCell, BoardCell>> movesToTake = getAllMovesToTake();
//        if (movesToTake == null || movesToTake.size() == 0) {
//            ArrayList<BoardCell> correctMoves = getCorrectMovesForPiece(pieceToMove);
//            if (containMove(correctMoves, new BoardCell(toRow, toCol))) {
//                move(pieceToMove, toRow, toCol);
//                return true;
//            }
//        } else {
//            if (containMove(movesToTake, new BoardCell(toRow, toCol))) {
//                if (pieceToMove.getPieceRank() == PieceRank.PAWN) {
//                    int takeRow = (int) (pieceToMove.getPosition().getRow() + toRow) / 2;
//                    int takeCol = (int) (pieceToMove.getPosition().getCol() + toCol) / 2;
//                    CheckersPiece takenPiece = pieceAt(takeRow, takeCol);
//                    takenPieces.add(takenPiece);
//                    pieces.remove(takenPiece);
//                    move(pieceToMove, toRow, toCol);
//                    return true;
//                }
//            }
//        }

        return false;
    }

    private void move(CheckersPiece pieceToMove, int toRow, int toCol) {
        pieceToMove.setPosition(toRow, toCol);

        if (pieceToMove.getPlayer() == playerTurn)
            if (pieceToMove.getPosition().getRow() == 0)
                makeKing(pieceToMove);
        if (pieceToMove.getPlayer() == opponentTurn)
            if (pieceToMove.getPosition().getRow() == DECK_SIZE - 1)
                makeKing(pieceToMove);

        ArrayList<BoardCell> movesToTake = getMovesToTake(pieceToMove);
        if (movesToTake == null || movesToTake.size() == 0) {
            turn = turn == Player.WHITE ? Player.BLACK : Player.WHITE;
            lastMovingPiece = null;
        }
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

    private ArrayList<Pair<BoardCell, BoardCell>> getAllMovesToTake() {
        ArrayList<Pair<BoardCell, BoardCell>> allMoves = new ArrayList<>();
        for (CheckersPiece piece : pieces) {
            if (piece.getPlayer() == turn) {
                ArrayList<BoardCell> moves = getMovesToTake(piece);
                for (BoardCell m : moves) {
                    allMoves.add(new Pair<>(piece.getPosition(), m));
                }
            }
        }
        return allMoves;
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

    public ArrayList<BoardCell> getCorrectMovesForPiece(CheckersPiece piece) {
        if (turn != piece.getPlayer())
            return null;
        ArrayList<BoardCell> allMoves = piece.getAllPossibleMoves();
        ArrayList<BoardCell> correctMoves = new ArrayList<>();
        for (BoardCell move : allMoves) {
            if (pieceAt(move) == null) {
                if (piece.getPieceRank() == PieceRank.PAWN)
                    if (isPawnMove(piece, move))
                        correctMoves.add(move);
                else
                    if (isKingMove(piece, move))
                        correctMoves.add(move);
            }
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

    private boolean isPawnTakeMove(CheckersPiece pawn, BoardCell take) {
        int takeRow = (int) (pawn.getPosition().getRow() + take.getRow()) / 2;
        int takeCol = (int) (pawn.getPosition().getCol() + take.getCol()) / 2;
        CheckersPiece takePiece = pieceAt(takeRow, takeCol);
        if (takePiece == null)
            return false;
        if (takePiece.getPlayer() != pawn.getPlayer())
            return true;
        return false;
    }

    private boolean isKingTakeMove(CheckersPiece king, BoardCell take) {
        return false;
    }

    private boolean isPawnMove(CheckersPiece piece, BoardCell move) {
        if (playerTurn == piece.getPlayer()) {
            if (move.getRow() - piece.getPosition().getRow() == -1 && Math.abs(move.getCol() - piece.getPosition().getCol()) == 1)
                return true;
        } else {
            if (move.getRow() - piece.getPosition().getRow() == 1 && Math.abs(move.getCol() - piece.getPosition().getCol()) == 1)
                return true;
        }
        return false;
    }

    private boolean isKingMove(CheckersPiece king, BoardCell move) {
        return false;
    }

    public ArrayList<BoardCell> getTakenPieces() {
        return new ArrayList<>(takenPieces);
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
