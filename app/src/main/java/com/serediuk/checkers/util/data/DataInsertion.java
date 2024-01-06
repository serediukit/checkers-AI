package com.serediuk.checkers.util.data;

import android.util.Pair;

import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersPiece;
import com.serediuk.checkers.enums.*;

import java.util.ArrayList;

public class DataInsertion {
    public static LevelData getLevelData(int levelNumber) {
        switch (levelNumber) {
            case 1:
                return get1Level();
            case 2:
                return get2Level();
            case 3:
                return get3Level();
            case 4:
                return get4Level();
            default:
                return null;
        }
    }

    private static LevelData get1Level() {
        int levelNumber = 1;
        ArrayList<CheckersPiece> pieces = new ArrayList<>();
        pieces.add(new CheckersPiece(new BoardCell(7, 0), Player.WHITE, PieceRank.PAWN));
        pieces.add(new CheckersPiece(new BoardCell(5, 0), Player.WHITE, PieceRank.PAWN));
        pieces.add(new CheckersPiece(new BoardCell(4, 1), Player.BLACK, PieceRank.KING));
        pieces.add(new CheckersPiece(new BoardCell(6, 1), Player.BLACK, PieceRank.PAWN));
        ArrayList<Pair<BoardCell, BoardCell>> correctMoves = new ArrayList<>();
        correctMoves.add(new Pair<>(new BoardCell(7, 0), new BoardCell(5, 2)));
        correctMoves.add(new Pair<>(new BoardCell(5, 2), new BoardCell(3, 0)));
        return new LevelData(levelNumber, pieces, correctMoves);
    }

    private static LevelData get2Level() {
        int levelNumber = 2;
        ArrayList<CheckersPiece> pieces = new ArrayList<>();
        pieces.add(new CheckersPiece(new BoardCell(7, 6), Player.WHITE, PieceRank.PAWN));
        pieces.add(new CheckersPiece(new BoardCell(5, 6), Player.WHITE, PieceRank.PAWN));
        pieces.add(new CheckersPiece(new BoardCell(6, 5), Player.BLACK, PieceRank.PAWN));
        pieces.add(new CheckersPiece(new BoardCell(5, 4), Player.BLACK, PieceRank.PAWN));
        ArrayList<Pair<BoardCell, BoardCell>> correctMoves = new ArrayList<>();
        correctMoves.add(new Pair<>(new BoardCell(5, 6), new BoardCell(7, 4)));
        return new LevelData(levelNumber, pieces, correctMoves);
    }

    private static LevelData get3Level() {
        int levelNumber = 3;
        ArrayList<CheckersPiece> pieces = new ArrayList<>();
        pieces.add(new CheckersPiece(new BoardCell(4, 3), Player.WHITE, PieceRank.PAWN));
        pieces.add(new CheckersPiece(new BoardCell(4, 5), Player.WHITE, PieceRank.PAWN));
        pieces.add(new CheckersPiece(new BoardCell(3, 4), Player.BLACK, PieceRank.PAWN));
        pieces.add(new CheckersPiece(new BoardCell(2, 5), Player.BLACK, PieceRank.PAWN));
        ArrayList<Pair<BoardCell, BoardCell>> correctMoves = new ArrayList<>();
        correctMoves.add(new Pair<>(new BoardCell(4, 5), new BoardCell(2, 3)));
        return new LevelData(levelNumber, pieces, correctMoves);
    }

    private static LevelData get4Level() {
        int levelNumber = 4;
        ArrayList<CheckersPiece> pieces = new ArrayList<>();
        pieces.add(new CheckersPiece(new BoardCell(7, 0), Player.WHITE, PieceRank.PAWN));
        pieces.add(new CheckersPiece(new BoardCell(5, 0), Player.WHITE, PieceRank.PAWN));
        pieces.add(new CheckersPiece(new BoardCell(4, 1), Player.BLACK, PieceRank.KING));
        pieces.add(new CheckersPiece(new BoardCell(6, 1), Player.BLACK, PieceRank.PAWN));
        pieces.add(new CheckersPiece(new BoardCell(0, 7), Player.BLACK, PieceRank.PAWN));
        ArrayList<Pair<BoardCell, BoardCell>> correctMoves = new ArrayList<>();
        correctMoves.add(new Pair<>(new BoardCell(7, 0), new BoardCell(5, 2)));
        correctMoves.add(new Pair<>(new BoardCell(5, 2), new BoardCell(3, 0)));
        correctMoves.add(new Pair<>(new BoardCell(0, 7), new BoardCell(1, 6)));
        correctMoves.add(new Pair<>(new BoardCell(1, 6), new BoardCell(2, 7)));
        correctMoves.add(new Pair<>(new BoardCell(3, 0), new BoardCell(2, 1)));
        return new LevelData(levelNumber, pieces, correctMoves);
    }
}
