package com.serediuk.checkers.util;

import android.util.Pair;

import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersPiece;

import java.util.ArrayList;

public class LevelLoader {
    public static int getLevelNumber() {
        return 1;
    }

    public static ArrayList<CheckersPiece> getPieces() {
        int levelNumber = getLevelNumber();
        return null;
    }

    public static ArrayList<Pair<BoardCell, BoardCell>> getCorrectMoves() {
        int levelNumber = getLevelNumber();
        return null;
    }
}
