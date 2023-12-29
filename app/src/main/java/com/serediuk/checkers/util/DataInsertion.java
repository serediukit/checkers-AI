package com.serediuk.checkers.util;

import android.util.Pair;

import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersPiece;
import com.serediuk.checkers.enums.*;

import java.util.ArrayList;

public class DataInsertion {
    public static LevelData getLevelData(int levelNumber) {
        switch (levelNumber) {
            case 1:
                return getFirstLevel();
            case 2:
                return getSecondLevel();
            case 3:
                return getThirdLevel();
            default:
                return null;
        }
    }

    private static LevelData getFirstLevel() {
        int levelNumber = 1;
        ArrayList<CheckersPiece> pieces = new ArrayList<>();
        pieces.add(new CheckersPiece(new BoardCell(7, 0), Player.WHITE, PieceRank.PAWN));
        pieces.add(new CheckersPiece(new BoardCell(5, 0), Player.WHITE, PieceRank.PAWN));
        pieces.add(new CheckersPiece(new BoardCell(4, 1), Player.BLACK, PieceRank.KING));
        pieces.add(new CheckersPiece(new BoardCell(6, 1), Player.BLACK, PieceRank.PAWN));
        ArrayList<Pair<BoardCell, BoardCell>> correctMoves = new ArrayList<>();
        correctMoves.add(new Pair<>(new BoardCell(5, 0), new BoardCell(3, 2)));
        return new LevelData(levelNumber, pieces, correctMoves);
    }

    private static LevelData getSecondLevel() {
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

    private static LevelData getThirdLevel() {
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
}
