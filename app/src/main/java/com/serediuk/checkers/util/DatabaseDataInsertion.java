package com.serediuk.checkers.util;

import android.util.Pair;

import com.serediuk.checkers.R;
import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersPiece;
import com.serediuk.checkers.enums.*;

import java.util.ArrayList;

public class DatabaseDataInsertion {
    public static LevelData getLevelData(int levelNumber) {
        switch (levelNumber) {
            case 1:
                return getFirstLevel();
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
}
