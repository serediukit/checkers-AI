package com.serediuk.checkers.util;

import android.util.Pair;

import com.serediuk.checkers.model.BoardCell;
import com.serediuk.checkers.model.CheckersPiece;

import java.util.ArrayList;

public class LevelData {
    private int levelNumber;
    private ArrayList<CheckersPiece> pieces;
    private ArrayList<Pair<BoardCell, BoardCell>> correctMoves;

    public LevelData(int levelNumber, ArrayList<CheckersPiece> pieces, ArrayList<Pair<BoardCell, BoardCell>> correctMoves) {
        this.levelNumber = levelNumber;
        this.pieces = pieces;
        this.correctMoves = correctMoves;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public ArrayList<CheckersPiece> getPieces() {
        return pieces;
    }

    public void setPieces(ArrayList<CheckersPiece> pieces) {
        this.pieces = pieces;
    }

    public ArrayList<Pair<BoardCell, BoardCell>> getCorrectMoves() {
        return correctMoves;
    }

    public void setCorrectMoves(ArrayList<Pair<BoardCell, BoardCell>> correctMoves) {
        this.correctMoves = correctMoves;
    }
}
